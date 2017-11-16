package ru.naumen.perfhouse.parser;

import org.influxdb.dto.BatchPoints;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data.ActionDoneData;
import ru.naumen.perfhouse.parser.data_parsers.*;
import ru.naumen.perfhouse.parser.data.ErrorData;
import ru.naumen.perfhouse.parser.data.GCData;
import ru.naumen.perfhouse.parser.time_parsers.GCTimeParser;
import ru.naumen.perfhouse.parser.data.TopData;
import ru.naumen.perfhouse.parser.time_parsers.SdngTimeParser;
import ru.naumen.perfhouse.parser.time_parsers.TimeParser;
import ru.naumen.perfhouse.parser.time_parsers.TopTimeParser;
import ru.naumen.perfhouse.parser.data.DataSet;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;

@Service
public class Parser {

    private final InfluxDAO influxDAO;

    @Inject
    public Parser(InfluxDAO influxDAO) {
        this.influxDAO = influxDAO;
    }

    public void parse(String dbName, String parsingMode, String timeZone, Boolean needLog, String fileName,
                      InputStreamReader logStreamReader) throws IOException, ParseException {

        final String finalInfluxDb = dbName.replaceAll("-", "_");
        influxDAO.connectToDB(finalInfluxDb);

        BatchPoints points = influxDAO.startBatchPoints(finalInfluxDb);

        HashMap<Long, DataSet> data = new HashMap<>();

        TimeParser timeParser;
        DataParser dataParser;

        switch (parsingMode) {
            case "sdng":
                timeParser = new SdngTimeParser(timeZone);
                dataParser = new GenericParser(new ErrorParser(), new ActionDoneParser());
                break;
            case "gc":
                //parse gc log
                timeParser = new GCTimeParser(timeZone);
                dataParser = new GCParser();
                break;
            case "top":
                timeParser = new TopTimeParser(fileName);
                dataParser = new TopParser();
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown parse mode! Availiable modes: sdng, gc, top. Requested mode: " + parsingMode);
        }

        try (BufferedReader br = new BufferedReader(logStreamReader)) {
            String line;
            while ((line = br.readLine()) != null) {
                long time = timeParser.parseLine(line);

                if (time == 0) {
                    continue;
                }

                int min5 = 5 * 60 * 1000;
                long count = time / min5;
                long key = count * min5;

                DataSet dataSet = data.computeIfAbsent(key, k -> new DataSet());
                dataParser.parseLine(dataSet, line);
            }
        }

        if (needLog) {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }

        data.forEach((k, set) ->
        {
            ActionDoneData dones = set.getActionsDone();
            dones.calculate();
            ErrorData erros = set.getErrors();
            if (needLog) {
                System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", k, dones.getCount(),
                        dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                        dones.getPercent99(), dones.getPercent999(), dones.getMax(), erros.getErrorCount()));
            }
            if (!dones.isEmpty()) {
                influxDAO.storeActionsFromLog(points, finalInfluxDb, k, dones, erros);
            }

            GCData gc = set.getGc();
            if (!gc.isEmpty()) {
                influxDAO.storeGc(points, finalInfluxDb, k, gc);
            }

            TopData cpuData = set.cpuData();
            if (!cpuData.isEmpty()) {
                influxDAO.storeTop(points, finalInfluxDb, k, cpuData);
            }
        });
        influxDAO.writeBatch(points);
    }
}
