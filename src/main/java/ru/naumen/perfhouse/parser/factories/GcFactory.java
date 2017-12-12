package ru.naumen.perfhouse.parser.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.GCData;
import ru.naumen.perfhouse.parser.data_parsers.DataParser;
import ru.naumen.perfhouse.parser.data_parsers.GCParser;
import ru.naumen.perfhouse.parser.data_savers.DataSaver;
import ru.naumen.perfhouse.parser.data_savers.GcDataSaver;
import ru.naumen.perfhouse.parser.time_parsers.GCTimeParser;
import ru.naumen.perfhouse.parser.time_parsers.TimeParser;

@Service("Gc")
@Scope("request")
public class GcFactory implements ParserFactory {
    private GCTimeParser timeParser;
    private GcDataSaver gcDataSaver;
    private GCParser gcParser;

    @Autowired
    public GcFactory(GCTimeParser timeParser, GcDataSaver gcDataSaver, GCParser gcParser) {
        this.timeParser = timeParser;
        this.gcDataSaver = gcDataSaver;
        this.gcParser = gcParser;
    }

    @Override
    public Data getDataSet() {
        return new GCData();
    }

    @Override
    public TimeParser getTimeParser(String fileName, String timeZone) {
        return timeParser;
    }

    @Override
    public DataSaver getDataSaver() {
        return gcDataSaver;
    }

    @Override
    public DataParser getDataParser() {
        return gcParser;
    }
}
