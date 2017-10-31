package ru.naumen.perfhouse.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import org.influxdb.dto.BatchPoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.GCParser.GCTimeParser;

import javax.inject.Inject;

public class Parser {

    public static void parse(String dbName, String parsingMode, String pathToLog, String timeZone, Boolean needLog, InfluxDAO storage)
            throws IOException, ParseException {
        String influxDb = dbName;
        influxDb = influxDb.replaceAll("-", "_");

        if (influxDb != null) {
            storage.init();
            storage.connectToDB(influxDb);
        }
        String finalInfluxDb = influxDb;
        BatchPoints points = null;

        if (storage != null) {
            points = storage.startBatchPoints(influxDb);
        }

        HashMap<Long, DataSet> data = new HashMap<>();

        TimeParser timeParser = new TimeParser(timeZone);
        GCTimeParser gcTime = new GCTimeParser(timeZone);

        switch (parsingMode) {
            case "sdng":
                //parse sdng
                try (BufferedReader br = new BufferedReader(new FileReader(pathToLog), 32 * 1024 * 1024)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        long time = timeParser.parseLine(line);

                        if (time == 0) {
                            continue;
                        }

                        int min5 = 5 * 60 * 1000;
                        long count = time / min5;
                        long key = count * min5;

                        data.computeIfAbsent(key, k -> new DataSet()).parseLine(line);
                    }
                }
                break;
            case "gc":
                //parse gc log
                try (BufferedReader br = new BufferedReader(new FileReader(pathToLog))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        long time = gcTime.parseTime(line);

                        if (time == 0) {
                            continue;
                        }

                        int min5 = 5 * 60 * 1000;
                        long count = time / min5;
                        long key = count * min5;
                        data.computeIfAbsent(key, k -> new DataSet()).parseGcLine(line);
                    }
                }
                break;
            case "top":
                TopParser topParser = new TopParser(pathToLog, data);
                topParser.configureTimeZone(timeZone);
                //parse top
                topParser.parse();
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown parse mode! Availiable modes: sdng, gc, top. Requested mode: " + parsingMode);
        }

        if (needLog) {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }
        BatchPoints finalPoints = points;
        data.forEach((k, set) ->
        {
            ActionDoneParser dones = set.getActionsDone();
            dones.calculate();
            ErrorParser erros = set.getErrors();
            if (needLog) {
                System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", k, dones.getCount(),
                        dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                        dones.getPercent99(), dones.getPercent999(), dones.getMax(), erros.getErrorCount()));
            }
            if (!dones.isNan()) {
                storage.storeActionsFromLog(finalPoints, finalInfluxDb, k, dones, erros);
            }

            GCParser gc = set.getGc();
            if (!gc.isNan()) {
                storage.storeGc(finalPoints, finalInfluxDb, k, gc);
            }

            TopData cpuData = set.cpuData();
            if (!cpuData.isNan()) {
                storage.storeTop(finalPoints, finalInfluxDb, k, cpuData);
            }
        });
        storage.writeBatch(points);
    }
}
