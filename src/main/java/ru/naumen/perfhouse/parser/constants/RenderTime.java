package ru.naumen.perfhouse.parser.constants;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenderTime implements ParserDataForGUI {
    public static final String RENDER_TIME = "render_time";
    @Override
    public List<String> getTypeProperties() {
        return Lists.newArrayList(RENDER_TIME);
    }

    @Override
    public String getName() {
        return "Render time";
    }

    @Override
    public String getChartTitle() {
        return "Render time";
    }

    @Override
    public String getYAxisTitle() {
        return "Render time";
    }

    @Override
    public List<SeriesInfo> getSeriesInfo() {
        return Lists.newArrayList(new SeriesInfo("Render time", RENDER_TIME));
    }

    @Override
    public List<TableTitle> getTableTitles() {
        return Lists.newArrayList(new TableTitle("Render time", RENDER_TIME));
    }
}
