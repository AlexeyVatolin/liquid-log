package ru.naumen.perfhouse.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;

import org.influxdb.dto.BatchPoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.GCParser.GCTimeParser;

import javax.inject.Inject;

@Service
public class Parser {

    private final InfluxDAO influxDAO;

    @Inject
    public Parser(InfluxDAO influxDAO) {
        this.influxDAO = influxDAO;
    }

    public void parse(String dbName, String parsingMode, String timeZone, Boolean needLog, String fileName,
                      InputStreamReader logStreamReader) throws IOException, ParseException {
        dbName = dbName.replaceAll("-", "_");
        influxDAO.connectToDB(dbName);

        BatchPoints points = influxDAO.startBatchPoints(dbName);

        HashMap<Long, DataSet> data = new HashMap<>();

        TimeParser timeParser = new TimeParser(timeZone);
        GCTimeParser gcTime = new GCTimeParser(timeZone);

        switch (parsingMode) {
            case "sdng":
                //parse sdng
                try (BufferedReader br = new BufferedReader(logStreamReader, 32 * 1024 * 1024)) {
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
                try (BufferedReader br = new BufferedReader(logStreamReader)) {
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
                TopParser topParser = new TopParser(fileName, data);
                topParser.configureTimeZone(timeZone);
                //parse top
                topParser.parse(logStreamReader);
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown parse mode! Availiable modes: sdng, gc, top. Requested mode: " + parsingMode);
        }

        if (needLog) {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }
        final String finalInfluxDb = dbName;
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
                influxDAO.storeActionsFromLog(points, finalInfluxDb, k, dones, erros);
            }

            GCParser gc = set.getGc();
            if (!gc.isNan()) {
                influxDAO.storeGc(points, finalInfluxDb, k, gc);
            }

            TopData cpuData = set.cpuData();
            if (!cpuData.isNan()) {
                influxDAO.storeTop(points, finalInfluxDb, k, cpuData);
            }
        });
        influxDAO.writeBatch(points);
    }
}
