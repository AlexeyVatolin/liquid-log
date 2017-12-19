package ru.naumen.perfhouse.parser.constants;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import java.util.List;
import static ru.naumen.perfhouse.parser.constants.DefaultConstants.TIME;

@Service
public class ResponseTimes implements ParserDataForGUI {
    public static final String PERCENTILE50 = "percent50";
    public static final String PERCENTILE95 = "percent95";
    public static final String PERCENTILE99 = "percent99";
    public static final String PERCENTILE999 = "percent999";
    public static final String MAX = "max";
    public static final String COUNT = "count";
    public static final String ERRORS = "errors";
    public static final String MEAN = "mean";
    public static final String STDDEV = "stddev";

    @Override
    public List<String> getTypeProperties() {
        return Lists.newArrayList(TIME, COUNT, ERRORS, MEAN, STDDEV, PERCENTILE50, PERCENTILE95, PERCENTILE99,
                PERCENTILE999, MAX);
    }

    @Override
    public String getName() {
        return "Responses";
    }

    @Override
    public String getChartTitle() {
        return "Response times";
    }

    @Override
    public String getYAxisTitle() {
        return "Response times";
    }

    @Override
    public List<SeriesInfo> getSeriesInfo() {
        return Lists.newArrayList(new SeriesInfo("50%", PERCENTILE50),
                new SeriesInfo("95%", PERCENTILE95),
                new SeriesInfo("99%", PERCENTILE99),
                new SeriesInfo("99.9%", PERCENTILE999),
                new SeriesInfo("max%", MAX));
    }

    @Override
    public List<TableTitle> getTableTitles() {
        return Lists.newArrayList(new TableTitle("Count", COUNT),
                new TableTitle("Errors", ERRORS),
                new TableTitle("Mean", MEAN),
                new TableTitle("Stddev", STDDEV),
                new TableTitle("50%", PERCENTILE50),
                new TableTitle("95%", PERCENTILE95),
                new TableTitle("99%", PERCENTILE99),
                new TableTitle("99.9%", PERCENTILE999),
                new TableTitle("Max", MAX));
    }
}
