package ru.naumen.perfhouse.parser.dataset_factory;

import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.TopData;

public class TopDataSetFactory implements DataSet {

    @Override
    public Data get() {
        return new TopData();
    }
}
