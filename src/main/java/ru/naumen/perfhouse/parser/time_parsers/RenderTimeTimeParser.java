package ru.naumen.perfhouse.parser.time_parsers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Scope("request")
public class RenderTimeTimeParser implements TimeParser {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",
            new Locale("ru", "RU"));

    private static final Pattern PATTERN = Pattern
            .compile("^\\d{9} (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}).*");

    public RenderTimeTimeParser()
    {
        this("GMT");
    }

    public RenderTimeTimeParser(String timeZone)
    {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    @Override
    public void configure(String fileName, String timeZone) {

    }

    @Override
    public long parseLine(String line) throws ParseException {
        Matcher matcher = PATTERN.matcher(line);

        if (matcher.find())
        {
            Date parse = DATE_FORMAT.parse(matcher.group(1));
            return parse.getTime();
        }
        return 0L;
    }
}
