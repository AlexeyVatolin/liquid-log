package ru.naumen.perfhouse.parser.dataset_factory;

import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.TopData;

public class TopDataSetFactory implements DataSet {
    private TopData topData;

    public TopDataSetFactory() {
        this.topData = new TopData();
    }

    @Override
    public Data get() {
        return topData;
    }
}
