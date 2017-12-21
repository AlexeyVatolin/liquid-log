package ru.naumen.perfhouse.parser.data_parsers;

import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.parser.data.ErrorData;
import ru.naumen.perfhouse.parser.data.SdngData;

import java.util.regex.Pattern;

@Service
public class ErrorParser implements DataParser<SdngData>
{
    private static final Pattern warnRegEx = Pattern.compile("^\\d+ \\[.+?] \\(.+?\\) WARN");
    private static final Pattern errorRegEx = Pattern.compile("^\\d+ \\[.+?] \\(.+?\\) ERROR");
    private static final Pattern fatalRegEx = Pattern.compile("^\\d+ \\[.+?] \\(.+?\\) FATAL");

    @Override
    public void parseLine(SdngData dataSet, String line)
    {
        ErrorData errorData = dataSet.getErrorData();
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
