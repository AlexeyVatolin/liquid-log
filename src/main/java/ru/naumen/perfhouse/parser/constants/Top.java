package ru.naumen.perfhouse.parser.constants;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

import static ru.naumen.perfhouse.parser.constants.DefaultConstants.TIME;

@Service
public class Top implements ParserDataForGUI {
    public static final String AVG_LA = "avgLa";
    public static final String AVG_CPU = "avgCpu";
    public static final String AVG_MEM = "avgMem";
    public static final String MAX_LA = "maxLa";
    public static final String MAX_CPU = "maxCpu";
    public static final String MAX_MEM = "maxMem";

    @Override
    public List<String> getTypeProperties() {
        return Lists.newArrayList(TIME, AVG_LA, AVG_CPU, AVG_MEM, MAX_LA, MAX_CPU, MAX_MEM);
    }

    @Override
    public String getName() {
        return "Top data";
    }

    @Override
    public String getChartTitle() {
        return "Top data";
    }

    @Override
    public String getYAxisTitle() {
        return "Top data";
    }

    @Override
    public List<SeriesInfo> getSeriesInfo() {
        return Lists.newArrayList(new SeriesInfo("Average LA", AVG_LA),
                new SeriesInfo("Avarage CPU usage", AVG_CPU, "%"),
                new SeriesInfo("Average MEM usage", AVG_MEM, "%"),
                new SeriesInfo("Max LA", MAX_LA),
                new SeriesInfo("Max CPU usage", MAX_CPU, "%"),
                new SeriesInfo("Max MEM Usage", MAX_MEM, "%"));
    }

    @Override
    public List<TableTitle> getTableTitles() {
        return Lists.newArrayList(new TableTitle("Avg LA", AVG_LA),
                new TableTitle("Avg CPU,%", AVG_CPU),
                new TableTitle("Avg MEM,%", AVG_MEM),
                new TableTitle("Max LA", MAX_LA),
                new TableTitle("Max CPU, %", MAX_CPU),
                new TableTitle("Max MEM, %", MAX_MEM));
    }
}
