package ru.naumen.perfhouse.parser.dataset_factory;

import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.GCData;

public class GCDataSetFactory implements DataSet {
    private GCData gcData;

    public GCDataSetFactory() {
        this.gcData = new GCData();
    }

    @Override
    public Data get() {
        return new GCData();
    }
}
