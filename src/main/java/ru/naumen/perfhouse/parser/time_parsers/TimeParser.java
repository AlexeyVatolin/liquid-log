package ru.naumen.perfhouse.parser.time_parsers;

import java.text.ParseException;

public interface TimeParser {
    void configure(String fileName, String timeZone);
    long parseLine(String line) throws ParseException;
}
