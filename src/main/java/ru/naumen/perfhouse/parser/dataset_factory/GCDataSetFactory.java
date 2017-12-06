package ru.naumen.perfhouse.parser.dataset_factory;

import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.GCData;

public class GCDataSetFactory implements DataSet {

    @Override
    public Data get() {
        return new GCData();
    }
}
