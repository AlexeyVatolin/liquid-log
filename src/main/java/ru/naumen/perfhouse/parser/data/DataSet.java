package ru.naumen.perfhouse.parser.data;

/**
 * Created by doki on 22.10.16.
 */
public class DataSet
{
    private ActionDoneData actionsDone;
    private ErrorData errors;
    private GCData gc;
    private TopData topData;

    public DataSet()
    {
        actionsDone = new ActionDoneData();
        errors = new ErrorData();
        gc = new GCData();
        topData = new TopData();
    }

    public ActionDoneData getActionsDone()
    {
        return actionsDone;
    }

    public ErrorData getErrors()
    {
        return errors;
    }

    public GCData getGc()
    {
        return gc;
    }

    public TopData getTopData()
    {
        return topData;
    }
}
