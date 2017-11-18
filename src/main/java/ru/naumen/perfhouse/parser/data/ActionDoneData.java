package ru.naumen.perfhouse.parser.data;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ActionDoneData {
    private Set<String> EXCLUDED_ACTIONS;
    private ArrayList<Integer> times = new ArrayList<>();
    private double min;
    private double mean;
    private double stddev;
    private double percent50;
    private double percent95;
    private double percent99;
    private double percent999;
    private double max;
    private long count;

    private int addObjectActions = 0;
    private int editObjectsActions = 0;
    private int getCatalogsAction = 0;
    private int getListActions = 0;
    private int commentActions = 0;
    private int getFormActions = 0;
    private int getDtObjectActions = 0;
    private int searchActions = 0;

    public ActionDoneData() {
        EXCLUDED_ACTIONS = new HashSet<>();
        EXCLUDED_ACTIONS.add("EventAction".toLowerCase());
    }

    public void calculate()
    {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        times.forEach(ds::addValue);
        min = ds.getMin();
        mean = ds.getMean();
        stddev = ds.getStandardDeviation();
        percent50 = ds.getPercentile(50.0);
        percent95 = ds.getPercentile(95.0);
        percent99 = ds.getPercentile(99.0);
        percent999 = ds.getPercentile(99.9);
        max = ds.getMax();
        count = ds.getN();
    }

    public Set<String> getExcludedActions() {
        return EXCLUDED_ACTIONS;
    }

    public int geListActions()
    {
        return getListActions;
    }

    public int getAddObjectActions()
    {
        return addObjectActions;
    }

    public int getCommentActions()
    {
        return commentActions;
    }

    public long getCount()
    {
        return count;
    }

    public int getDtObjectActions()
    {
        return getDtObjectActions;
    }

    public int getEditObjectsActions()
    {
        return editObjectsActions;
    }

    public int getFormActions()
    {
        return getFormActions;
    }

    public double getMax()
    {
        return max;
    }

    public double getMean()
    {
        return mean;
    }

    public double getMin()
    {
        return min;
    }

    public double getPercent50()
    {
        return percent50;
    }

    public double getPercent95()
    {
        return percent95;
    }

    public double getPercent99()
    {
        return percent99;
    }

    public double getPercent999()
    {
        return percent999;
    }

    public int getSearchActions()
    {
        return searchActions;
    }

    public int getCatalogsAction()
    {
        return getCatalogsAction;
    }

    public double getStddev()
    {
        return stddev;
    }

    public ArrayList<Integer> getTimes()
    {
        return times;
    }

    public void addTimes(int value)
    {
        times.add(value);
    }

    public void incrementAddObjectActions()
    {
        addObjectActions++;
    }

    public void incrementEditObjectsActions() {
        editObjectsActions++;
    }

    public void incrementGetCatalogsAction() {
        getCatalogsAction++;
    }

    public void incrementGetListActions() {
        getListActions++;
    }

    public void incrementCommentActions() {
        commentActions++;
    }

    public void incrementGetFormActions() {
        getFormActions++;
    }

    public void incrementGetDtObjectActions() {
        getDtObjectActions++;
    }

    public void incrementSearchActions()
    {
        searchActions++;
    }


    public boolean isEmpty()
    {
        return count == 0;
    }
}
