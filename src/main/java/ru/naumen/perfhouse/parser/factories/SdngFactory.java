package ru.naumen.perfhouse.parser.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.SdngData;
import ru.naumen.perfhouse.parser.data_parsers.DataParser;
import ru.naumen.perfhouse.parser.data_parsers.SdngParser;
import ru.naumen.perfhouse.parser.data_savers.DataSaver;
import ru.naumen.perfhouse.parser.data_savers.SdngDataSaver;
import ru.naumen.perfhouse.parser.time_parsers.SdngTimeParser;
import ru.naumen.perfhouse.parser.time_parsers.TimeParser;

@Service("Sdng")
public class SdngFactory implements ParserFactory {
    private SdngTimeParser sdngTimeParser;
    private SdngDataSaver sdngDataSaver;
    private SdngParser sdngParser;

    @Autowired
    public SdngFactory(SdngTimeParser sdngTimeParser, SdngDataSaver sdngDataSaver, SdngParser sdngParser) {
        this.sdngTimeParser = sdngTimeParser;
        this.sdngDataSaver = sdngDataSaver;
        this.sdngParser = sdngParser;
    }

    @Override
    public Data getDataSet() {
        return new SdngData();
    }

    @Override
    public TimeParser getTimeParser(String fileName, String timeZone) {
        return sdngTimeParser;
    }

    @Override
    public DataSaver getDataSaver() {
        return sdngDataSaver;
    }

    @Override
    public DataParser getDataParser() {
        return sdngParser;
    }
}
