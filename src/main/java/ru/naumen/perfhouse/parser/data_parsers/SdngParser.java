package ru.naumen.perfhouse.parser.data_parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.parser.data.SdngData;

@Service()
public class SdngParser implements DataParser<SdngData> {
    private DataParser[] dataParsers;

    @Autowired
    public SdngParser(ErrorParser errorParser, ActionDoneParser actionDoneParser) {
        this.dataParsers = new DataParser[] {errorParser, actionDoneParser};
    }

    @Override
    public void parseLine(SdngData dataSet, String line) {
        for (DataParser dataParser: dataParsers)
        {
           dataParser.parseLine(dataSet, line);
        }
    }
}
