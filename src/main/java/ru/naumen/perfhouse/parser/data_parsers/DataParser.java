package ru.naumen.perfhouse.parser.data_parsers;

import ru.naumen.perfhouse.parser.data.DataSet;

public interface DataParser {
    void parseLine(DataSet dataSet, String line);
}
