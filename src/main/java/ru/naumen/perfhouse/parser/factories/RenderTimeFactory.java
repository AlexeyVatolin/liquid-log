package ru.naumen.perfhouse.parser.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.RenderTimeData;
import ru.naumen.perfhouse.parser.data_parsers.DataParser;
import ru.naumen.perfhouse.parser.data_parsers.RenderTimeParser;
import ru.naumen.perfhouse.parser.data_savers.DataSaver;
import ru.naumen.perfhouse.parser.data_savers.RenderTimeDataSaver;
import ru.naumen.perfhouse.parser.time_parsers.RenderTimeTimeParser;
import ru.naumen.perfhouse.parser.time_parsers.TimeParser;

@Service("RenderTime")
@Scope("request")
public class RenderTimeFactory implements ParserFactory {
    private RenderTimeTimeParser timeParser;
    private RenderTimeParser parser;
    private RenderTimeDataSaver dataSaver;

    @Autowired
    public RenderTimeFactory(RenderTimeTimeParser timeParser, RenderTimeParser parser, RenderTimeDataSaver dataSaver) {
        this.timeParser = timeParser;
        this.parser = parser;
        this.dataSaver = dataSaver;

    }

    @Override
    public Data getDataSet() {
        return new RenderTimeData();
    }

    @Override
    public TimeParser getTimeParser(String fileName, String timeZone) {
        return timeParser;
    }

    @Override
    public DataSaver getDataSaver() {
        return dataSaver;
    }

    @Override
    public DataParser getDataParser() {
        return parser;
    }
}
