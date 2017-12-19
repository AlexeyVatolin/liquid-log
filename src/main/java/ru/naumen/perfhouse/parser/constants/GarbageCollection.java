package ru.naumen.perfhouse.parser.constants;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static ru.naumen.perfhouse.parser.constants.DefaultConstants.TIME;

@Service
public class GarbageCollection implements Constant {
    public static final String GCTIMES = "gcTimes";
    public static final String AVARAGE_GC_TIME = "avgGcTime";
    public static final String MAX_GC_TIME = "maxGcTime";

    @Override
    public List<String> getTypeProperties() {
        return Lists.newArrayList(TIME, GCTIMES, AVARAGE_GC_TIME, MAX_GC_TIME);
    }

    @Override
    public String getName() {
        return "Garbage Collection";
    }

    @Override
    public String getChartTitle() {
        return "Garbage collection";
    }

    @Override
    public String getYAxisTitle() {
        return "GC";
    }

    @Override
    public List<SeriesInfo> getSeriesInfo() {
        return Lists.newArrayList(new SeriesInfo("Time", DefaultConstants.TIME),
                new SeriesInfo("GC Performed", GCTIMES, "times"),
                new SeriesInfo("Average GC Time", AVARAGE_GC_TIME, "ms"),
                new SeriesInfo("Max GC Time", MAX_GC_TIME, "ms"));
    }

    @Override
    public List<TableTitle> getTableTitles() {
        return Lists.newArrayList(new TableTitle("Time", DefaultConstants.TIME),
                new TableTitle("Number of performed GC", GCTIMES),
                new TableTitle("Avarage GC time, ms", AVARAGE_GC_TIME),
                new TableTitle("Max GC Time, ms", MAX_GC_TIME));
    }
}
