package ru.naumen.perfhouse.parser.data_parsers;

import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.parser.data.TopData;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TopParser implements DataParser<TopData>
{
    private static final Pattern cpuAndMemPattren = Pattern
            .compile("^ *\\d+ \\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ \\S+ +(\\S+) +(\\S+) +\\S+ java");

    public void parseLine(TopData dataSet, String line)
    {
        //getDataSet la
        Matcher la = Pattern.compile(".*load average:(.*)").matcher(line);
        if (la.find())
        {
            dataSet.addLa(Double.parseDouble(la.group(1).split(",")[0].trim()));
            return;
        }

        //getDataSet cpu and mem
        Matcher cpuAndMemMatcher = cpuAndMemPattren.matcher(line);
        if (cpuAndMemMatcher.find())
        {
            dataSet.addCpu(Double.valueOf(cpuAndMemMatcher.group(1)));
            dataSet.addMem(Double.valueOf(cpuAndMemMatcher.group(2)));
        }
    }

}
