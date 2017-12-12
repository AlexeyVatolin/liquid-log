package ru.naumen.perfhouse.parser.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.TopData;
import ru.naumen.perfhouse.parser.data_parsers.DataParser;
import ru.naumen.perfhouse.parser.data_parsers.TopParser;
import ru.naumen.perfhouse.parser.data_savers.DataSaver;
import ru.naumen.perfhouse.parser.data_savers.TopDataSaver;
import ru.naumen.perfhouse.parser.time_parsers.TimeParser;
import ru.naumen.perfhouse.parser.time_parsers.TopTimeParser;

@Service("Top")
@Scope("request")
public class TopFactory implements ParserFactory {
    private TopTimeParser topTimeParser;
    private TopDataSaver topDataSaver;
    private TopParser topParser;

    @Autowired
    public TopFactory(TopTimeParser timeParser, TopDataSaver topDataSaver, TopParser topParser) {
        this.topTimeParser = timeParser;
        this.topDataSaver = topDataSaver;
        this.topParser = topParser;
    }

    @Override
    public Data getDataSet() {
        return new TopData();
    }

    @Override
    public TimeParser getTimeParser(String fileName, String timeZone) {
        topTimeParser.configure(fileName, timeZone);
        return topTimeParser;
    }

    @Override
    public DataSaver getDataSaver() {
        return topDataSaver;
    }

    @Override
    public DataParser getDataParser() {
        return topParser;
    }
}
