package ru.naumen.perfhouse.parser.data_parsers;

import ru.naumen.perfhouse.parser.data.DataSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GCParser implements DataParser
{
    private static final Pattern gcExecutionTime = Pattern.compile(".*real=(.*)secs.*");

    public void parseLine(DataSet dataSet, String line)
    {
        Matcher matcher = gcExecutionTime.matcher(line);
        if (matcher.find())
        {
            dataSet.getGc().addValue(Double.parseDouble(matcher.group(1).trim().replace(',', '.')));
        }
    }
}
