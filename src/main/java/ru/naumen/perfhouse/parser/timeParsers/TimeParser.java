package ru.naumen.perfhouse.parser.timeParsers;

import java.text.ParseException;

public interface TimeParser {
    public long parseLine(String line) throws ParseException;
}
