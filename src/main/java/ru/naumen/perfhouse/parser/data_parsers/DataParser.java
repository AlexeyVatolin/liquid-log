package ru.naumen.perfhouse.parser.data_parsers;

import ru.naumen.perfhouse.parser.data.DataSet;

public interface DataParser {
    public void parseLine(String line);
    public void setDataSet(DataSet dataSet);
}
