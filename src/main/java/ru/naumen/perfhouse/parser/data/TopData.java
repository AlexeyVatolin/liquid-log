package ru.naumen.perfhouse.parser.data;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.perfhouse.parser.NumberUtils;

/**
 * Cpu usage data, acquired from top output
 * @author dkolmogortsev
 *
 */
public class TopData
{
    private DescriptiveStatistics laStat = new DescriptiveStatistics();
    private DescriptiveStatistics cpuStat = new DescriptiveStatistics();
    private DescriptiveStatistics memStat = new DescriptiveStatistics();

    public void addLa(double la)
    {
        laStat.addValue(la);
    }

    public void addCpu(double cpu)
    {
        cpuStat.addValue(cpu);
    }

    public void addMem(double mem)
    {
        memStat.addValue(mem);
    }

    public boolean isEmpty()
    {
        return laStat.getN() == 0 && cpuStat.getN() == 0 && memStat.getN() == 0;
    }

    public double getAvgLa()
    {
        return NumberUtils.roundToTwoPlaces(NumberUtils.getSafeDouble(laStat.getMean()));
    }

    public double getAvgCpuUsage()
    {
        return NumberUtils.roundToTwoPlaces(NumberUtils.getSafeDouble(cpuStat.getMean()));
    }

    public double getAvgMemUsage()
    {
        return NumberUtils.roundToTwoPlaces(NumberUtils.getSafeDouble(memStat.getMean()));
    }

    public double getMaxLa()
    {
        return NumberUtils.roundToTwoPlaces(NumberUtils.getSafeDouble(laStat.getMax()));
    }

    public double getMaxCpu()
    {
        return NumberUtils.roundToTwoPlaces(NumberUtils.getSafeDouble(cpuStat.getMax()));
    }

    public double getMaxMem()
    {
        return NumberUtils.roundToTwoPlaces(NumberUtils.getSafeDouble(memStat.getMax()));
    }
}