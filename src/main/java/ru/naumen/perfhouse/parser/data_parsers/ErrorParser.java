package ru.naumen.perfhouse.parser.data_parsers;

import ru.naumen.perfhouse.parser.data.DataSet;
import ru.naumen.perfhouse.parser.data.ErrorData;

import java.util.regex.Pattern;

public class ErrorParser implements DataParser
{
    private static final Pattern warnRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) WARN");
    private static final Pattern errorRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) ERROR");
    private static final Pattern fatalRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) FATAL");

    public void parseLine(DataSet dataSet, String line)
    {
        ErrorData errorData = dataSet.getErrors();
        if (warnRegEx.matcher(line).find())
        {
            errorData.incrementWarnCount();
        }
        if (errorRegEx.matcher(line).find())
        {
            errorData.incrementErrorCount();
        }
        if (fatalRegEx.matcher(line).find())
        {
            errorData.incrementFatalCount();
        }
    }

}
