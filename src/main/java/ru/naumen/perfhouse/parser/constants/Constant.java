package ru.naumen.perfhouse.parser.constants;

import java.util.List;

public interface Constant {
    List<String> getTypeProperties();
    String getName();
    String getChartTitle();
    String getYAxisTitle();
    List<SeriesInfo> getSeriesInfo();
    List<TableTitle> getTableTitles();
}
