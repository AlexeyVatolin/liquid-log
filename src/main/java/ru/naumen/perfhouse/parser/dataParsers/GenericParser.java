package ru.naumen.perfhouse.parser.dataParsers;

import ru.naumen.perfhouse.parser.data.DataSet;
import ru.naumen.perfhouse.parser.dataParsers.DataParser;

public class GenericParser implements DataParser {
    private DataParser[] dataParsers;

    public GenericParser(DataParser... dataParsers) {
        this.dataParsers = dataParsers;
    }

    @Override
    public void parseLine(String line) {
        for (DataParser dataParser: dataParsers)
        {
           dataParser.parseLine(line);
        }
    }

    @Override
    public void setDataSet(DataSet dataSet) {
        for (DataParser dataParser: dataParsers)
        {
            dataParser.setDataSet(dataSet);
        }
    }
}
