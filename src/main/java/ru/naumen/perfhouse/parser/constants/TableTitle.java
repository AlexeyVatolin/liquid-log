package ru.naumen.perfhouse.parser.constants;

public class TableTitle {
    private String name;
    private String dataName;

    public TableTitle(String name, String dataName) {
        this.name = name;
        this.dataName = dataName;
    }

    public String getName() {
        return name;
    }

    public String getDataName() {
        return dataName;
    }
}
