package ru.naumen.perfhouse.parser.data_parsers;

import ru.naumen.perfhouse.parser.data.DataSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Top output parser
 * @author dkolmogortsev
 *
 */
public class TopParser implements DataParser
{
    private Pattern cpuAndMemPattren = Pattern
            .compile("^ *\\d+ \\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ \\S+ +(\\S+) +(\\S+) +\\S+ java");

    private DataSet dataSet;

    public void parseLine(String line)
    {
        //get la
        Matcher la = Pattern.compile(".*load average:(.*)").matcher(line);
        if (la.find())
        {
            dataSet.cpuData().addLa(Double.parseDouble(la.group(1).split(",")[0].trim()));
            return;
        }

        //get cpu and mem
        Matcher cpuAndMemMatcher = cpuAndMemPattren.matcher(line);
        if (cpuAndMemMatcher.find())
        {
            dataSet.cpuData().addCpu(Double.valueOf(cpuAndMemMatcher.group(1)));
            dataSet.cpuData().addMem(Double.valueOf(cpuAndMemMatcher.group(2)));
        }
    }

    @Override
    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

}
