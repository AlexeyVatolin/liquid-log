package ru.naumen.perfhouse.parser.data;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import static ru.naumen.perfhouse.parser.NumberUtils.getSafeDouble;
import static ru.naumen.perfhouse.parser.NumberUtils.roundToTwoPlaces;

public class GCData extends Data{
    private DescriptiveStatistics ds = new DescriptiveStatistics();

    public double getCalculatedAvg()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getMean()));
    }

    public long getGcTimes()
    {
        return ds.getN();
    }

    public double getMaxGcTime()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getMax()));
    }

    public boolean isEmpty()
    {
        return getGcTimes() == 0;
    }

    public void addValue(double value)
    {
        ds.addValue(value);
    }

}
