package ru.naumen.perfhouse.parser.time_parsers;

import java.text.ParseException;

public interface TimeParser {
    public long parseLine(String line) throws ParseException;
}