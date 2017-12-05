package ru.naumen.perfhouse.parser.data_parsers;

import ru.naumen.perfhouse.parser.data.Data;

public interface DataParser<T extends Data> {
    void parseLine(T dataSet, String line);
}
