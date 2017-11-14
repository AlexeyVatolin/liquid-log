package ru.naumen.perfhouse.parser.data;

public class ErrorData {
    private long warnCount;
    private long errorCount;
    private long fatalCount;

    public void IncrementWarnCount()
    {
        warnCount++;
    }

    public void IncrementErrorCount()
    {
        errorCount++;
    }

    public void IncrementFatalCount()
    {
        fatalCount++;
    }


    public long getWarnCount()
    {
        return warnCount;
    }

    public long getErrorCount()
    {
        return errorCount;
    }

    public long getFatalCount()
    {
        return fatalCount;
    }
}
