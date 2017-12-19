package ru.naumen.perfhouse.parser.constants;

public class SeriesInfo {
    private String name;
    private String unit;
    private String dataName;

    public SeriesInfo(String name, String dataName, String unit) {
        this.name = name;
        this.unit = unit;
        this.dataName = dataName;
    }

    public SeriesInfo(String name, String dataName) {
        this.name = name;
        this.dataName = dataName;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getDataName() {
        return dataName;
    }
}
