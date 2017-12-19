package ru.naumen.perfhouse.parser.constants;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import java.util.List;
import static ru.naumen.perfhouse.parser.constants.DefaultConstants.TIME;

@Service
public class PerformedActions implements Constant {
    public static final String ADD_ACTIONS = "addActions";
    public static final String EDIT_ACTIONS = "editActions";
    public static final String LIST_ACTIONS = "listActions";
    public static final String COMMENT_ACTIONS = "commentActions";
    public static final String GET_FORM_ACTIONS = "getFormActions";
    public static final String GET_DT_OBJECT_ACTIONS = "getDtObjectActions";
    public static final String SEARCH_ACTIONS = "searchActions";
    public static final String GET_CATALOGS_ACTIONS = "GetCatalogsActions";
    public static final String ACTIONS_COUNT = "count";

    @Override
    public List<String> getTypeProperties() {
        return Lists.newArrayList(TIME, ADD_ACTIONS, EDIT_ACTIONS, LIST_ACTIONS, COMMENT_ACTIONS, ACTIONS_COUNT,
                GET_FORM_ACTIONS, GET_DT_OBJECT_ACTIONS, SEARCH_ACTIONS, GET_CATALOGS_ACTIONS);
    }

    @Override
    public String getName() {
        return "Performed actions";
    }

    @Override
    public String getChartTitle() {
        return "Performed actions";
    }

    @Override
    public String getYAxisTitle() {
        return "Actions";
    }

    @Override
    public List<SeriesInfo> getSeriesInfo() {
        return Lists.newArrayList(new SeriesInfo("Time", DefaultConstants.TIME),
                new SeriesInfo("AddObject", ADD_ACTIONS),
                new SeriesInfo("EditObject", EDIT_ACTIONS),
                new SeriesInfo("GetList", LIST_ACTIONS),
                new SeriesInfo("Comment", COMMENT_ACTIONS),
                new SeriesInfo("GetForm", GET_FORM_ACTIONS),
                new SeriesInfo("GetDtObject", GET_DT_OBJECT_ACTIONS),
                new SeriesInfo("Search", SEARCH_ACTIONS),
                new SeriesInfo("Summary", ACTIONS_COUNT),
                new SeriesInfo("GetCatalogsActions", GET_CATALOGS_ACTIONS));
    }

    @Override
    public List<TableTitle> getTableTitles() {
        return Lists.newArrayList(new TableTitle("Time", DefaultConstants.TIME),
                new TableTitle("Summ", ACTIONS_COUNT),
                new TableTitle("AddObject", ADD_ACTIONS),
                new TableTitle("EditObject", EDIT_ACTIONS),
                new TableTitle("GetList", LIST_ACTIONS),
                new TableTitle("Comment", COMMENT_ACTIONS),
                new TableTitle("GetForm", GET_FORM_ACTIONS),
                new TableTitle("GetDtObject", GET_DT_OBJECT_ACTIONS),
                new TableTitle("Search", SEARCH_ACTIONS),
                new TableTitle("GetCatalogsActions", GET_CATALOGS_ACTIONS));
    }
}
