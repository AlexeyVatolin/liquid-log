package ru.naumen.perfhouse.parser.dataset_factory;

import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.SdngData;

public class SdngDataSetFactory implements DataSet {
    private SdngData sdngData;

    public SdngDataSetFactory() {
        this.sdngData = new SdngData();
    }

    @Override
    public Data get() {
        return new SdngData();
    }
}
