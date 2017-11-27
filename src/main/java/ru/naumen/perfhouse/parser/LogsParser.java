package ru.naumen.perfhouse.parser;

import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.influx.DataStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data_parsers.*;
import ru.naumen.perfhouse.parser.time_parsers.GCTimeParser;
import ru.naumen.perfhouse.parser.time_parsers.SdngTimeParser;
import ru.naumen.perfhouse.parser.time_parsers.TimeParser;
import ru.naumen.perfhouse.parser.time_parsers.TopTimeParser;
import ru.naumen.perfhouse.parser.data.DataSet;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

@Service
public class LogsParser {

    private final InfluxDAO influxDAO;

    @Inject
    public LogsParser(InfluxDAO influxDAO) {
        this.influxDAO = influxDAO;
    }

    public void parse(String dbName, String parsingMode, String timeZone, Boolean needLog, String fileName,
                      InputStreamReader logStreamReader) throws IOException, ParseException {
        DataStorage dataStorage = new DataStorage(influxDAO);
        dataStorage.init(dbName, needLog);
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

                DataSet dataSet = dataStorage.get(key);
                dataParser.parseLine(dataSet, line);
            }
            dataStorage.save();
        }
    }
}
