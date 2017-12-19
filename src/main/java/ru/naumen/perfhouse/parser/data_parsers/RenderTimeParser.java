package ru.naumen.perfhouse.parser.data_parsers;

import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.parser.data.RenderTimeData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RenderTimeParser implements DataParser<RenderTimeData> {
    private static final Pattern renderTime = Pattern.compile("render time: (.*)");

    @Override
    public void parseLine(RenderTimeData dataSet, String line) {
        Matcher matcher = renderTime.matcher(line);
        if (matcher.find())
        {
            dataSet.setRenderTime(Integer.parseInt(matcher.group(1)));
        }
    }
}
