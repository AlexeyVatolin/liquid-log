package ru.naumen.perfhouse.parser.data_parsers;

import ru.naumen.perfhouse.parser.data.DataSet;

public class GenericParser implements DataParser {
    private DataParser[] dataParsers;

    public GenericParser(DataParser... dataParsers) {
        this.dataParsers = dataParsers;
    }

    @Override
    public void parseLine(DataSet dataSet, String line) {
        for (DataParser dataParser: dataParsers)
        {
           dataParser.parseLine(dataSet, line);
        }
    }
}
