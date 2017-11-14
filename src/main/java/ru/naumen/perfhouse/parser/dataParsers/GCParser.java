package ru.naumen.perfhouse.parser.dataParsers;

import ru.naumen.perfhouse.parser.dataParsers.DataParser;
import ru.naumen.perfhouse.parser.data.DataSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GCParser implements DataParser
{
    private Pattern gcExecutionTime = Pattern.compile(".*real=(.*)secs.*");

    private DataSet dataSet;

    public void parseLine(String line)
    {
        Matcher matcher = gcExecutionTime.matcher(line);
        if (matcher.find())
        {
            dataSet.getGc().addValue(Double.parseDouble(matcher.group(1).trim().replace(',', '.')));
        }
    }

    @Override
    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }
}
