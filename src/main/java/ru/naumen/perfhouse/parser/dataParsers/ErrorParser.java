package ru.naumen.perfhouse.parser.dataParsers;

import ru.naumen.perfhouse.parser.dataParsers.DataParser;
import ru.naumen.perfhouse.parser.data.DataSet;
import ru.naumen.perfhouse.parser.data.ErrorData;

import java.util.regex.Pattern;

public class ErrorParser implements DataParser
{
    private Pattern warnRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) WARN");
    private Pattern errorRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) ERROR");
    private Pattern fatalRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) FATAL");

    private DataSet dataSet;

    public void parseLine(String line)
    {
        ErrorData errorData = dataSet.getErrors();
        if (warnRegEx.matcher(line).find())
        {
            errorData.IncrementWarnCount();
        }
        if (errorRegEx.matcher(line).find())
        {
            errorData.IncrementErrorCount();
        }
        if (fatalRegEx.matcher(line).find())
        {
            errorData.IncrementFatalCount();
        }
    }

    @Override
    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }


}
