package ru.naumen.perfhouse.parser.time_parsers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Scope("request")
public class SdngTimeParser implements TimeParser {

    private static final Pattern TIME_PATTERN = Pattern
            .compile("^\\d+ \\[.*?] \\((\\d{2} .{3} \\d{4} \\d{2}:\\d{2}:\\d{2},\\d{3})\\)");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss,SSS",
            new Locale("ru", "RU"));

    public SdngTimeParser()
    {
        this("GMT");
    }

    public SdngTimeParser(String timeZone)
    {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    @Override
    public void configure(String fileName, String timeZone) {

    }

    @Override
    public long parseLine(String line) throws ParseException {
        Matcher matcher = TIME_PATTERN.matcher(line);

        if (matcher.find())
        {
            String timeString = matcher.group(1);
            return DATE_FORMAT.parse(timeString).getTime();
        }
        return 0L;
    }
}
