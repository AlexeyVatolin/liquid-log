package ru.naumen.perfhouse.parser;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.influx.DataStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data_parsers.*;
import ru.naumen.perfhouse.parser.data_savers.DataSaver;
import ru.naumen.perfhouse.parser.time_parsers.TimeParser;
import ru.naumen.perfhouse.parser.factories.ParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

@Service
public class LogsParser {

    private final InfluxDAO influxDAO;
    private final BeanFactory beanFactory;

    @Autowired
    public LogsParser(InfluxDAO influxDAO, BeanFactory beanFactory) {
        this.influxDAO = influxDAO;
        this.beanFactory = beanFactory;
    }

    public void parse(String dbName, String parsingMode, String timeZone, Boolean needLog, String fileName,
                      InputStreamReader logStreamReader) throws IOException, ParseException {

        ParserFactory parserFactory = (ParserFactory)beanFactory.getBean(parsingMode);
        TimeParser timeParser = parserFactory.getTimeParser(fileName, timeZone);
        DataParser dataParser = parserFactory.getDataParser();
        DataSaver dataSaver = parserFactory.getDataSaver();

        DataStorage dataStorage = new DataStorage(influxDAO, parserFactory, dataSaver);
        dataStorage.init(dbName, needLog);

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

                Data dataSet = dataStorage.get(key);
                dataParser.parseLine(dataSet, line);
            }
            dataStorage.save();
        }
    }
}
