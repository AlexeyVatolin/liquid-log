package ru.naumen.perfhouse.parser.factories;

import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data_parsers.DataParser;
import ru.naumen.perfhouse.parser.data_savers.DataSaver;
import ru.naumen.perfhouse.parser.time_parsers.TimeParser;

public interface ParserFactory
{
    public Data getDataSet();
    public TimeParser getTimeParser(String fileName, String timeZone);
    public DataSaver getDataSaver();
    public DataParser getDataParser();
}
