package ru.naumen.perfhouse.parser.data_parsers;

import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.parser.data.GCData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GCParser implements DataParser<GCData>
{
    private static final Pattern gcExecutionTime = Pattern.compile(".*real=(.*)secs.*");

    public void parseLine(GCData dataSet, String line)
    {
        Matcher matcher = gcExecutionTime.matcher(line);
        if (matcher.find())
        {
            dataSet.addValue(Double.parseDouble(matcher.group(1).trim().replace(',', '.')));
        }
    }
}
