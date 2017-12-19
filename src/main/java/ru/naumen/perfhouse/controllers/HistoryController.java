package ru.naumen.perfhouse.controllers;

import java.text.ParseException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.naumen.perfhouse.parser.constants.ParserDataForGUI;
import ru.naumen.perfhouse.statdata.StatData;
import ru.naumen.perfhouse.statdata.StatDataService;

/**
 * Created by doki on 23.10.16.
 */
@Controller
public class HistoryController {

    private final StatDataService service;
    private List<ParserDataForGUI> constantsList;
    private String nameOfFirstConstant;

    private static final String NO_HISTORY_VIEW = "no_history";
    private static final String HISTORY_VIEW = "history";

    @Autowired
    public HistoryController(StatDataService service, List<ParserDataForGUI> constantsList) {
        this.service = service;
        this.constantsList = constantsList;
        this.constantsList.sort(Comparator.comparing(item -> item.getClass().getSimpleName()));
        this.nameOfFirstConstant = constantsList.get(0).getClass().getSimpleName();
    }

    @RequestMapping(path = "/history/{client}/{year}/{month}/{day}")
    public ModelAndView indexByDay(@PathVariable("client") String client,
                                   @PathVariable(name = "year", required = false) int year,
                                   @PathVariable(name = "month", required = false) int month,
                                   @PathVariable(name = "day", required = false) int day) throws ParseException {
        return getDataAndViewByDate(client, constantsList.get(0), year, month, day, nameOfFirstConstant);
    }

    @RequestMapping(path = "/history/{client}/{parserName}/{year}/{month}/{day}")
    public ModelAndView nonIndexByDay(@PathVariable("client") String client,
                                      @PathVariable(name = "year", required = false) int year,
                                      @PathVariable(name = "month", required = false) int month,
                                      @PathVariable(name = "day", required = false) int day,
                                      @PathVariable(name = "parserName", required = false) String parserName)
            throws ParseException {
        ParserDataForGUI parserDataForGUI = getConstantByName(parserName);
        return getDataAndViewByDate(client, parserDataForGUI, year, month, day, parserName);
    }

    @RequestMapping(path = "/history/{client}/{year}/{month}")
    public ModelAndView indexByMonth(@PathVariable("client") String client,
                                     @PathVariable(name = "year", required = false) int year,
                                     @PathVariable(name = "month", required = false) int month) throws ParseException {
        return getDataAndViewByDate(client, constantsList.get(0), year, month, 0, true, nameOfFirstConstant);
    }

    @RequestMapping(path = "/history/{client}/{parserName}/{year}/{month}")
    public ModelAndView nonIndexByMonth(@PathVariable("client") String client,
                                        @PathVariable(name = "year", required = false) int year,
                                        @PathVariable(name = "month", required = false) int month,
                                        @PathVariable(name = "parserName") String parserName) throws ParseException {
        ParserDataForGUI parserDataForGUI = getConstantByName(parserName);
        return getDataAndViewByDate(client, parserDataForGUI, year, month, 0, true, parserName);
    }

    @RequestMapping(path = "/history/{client}")
    public ModelAndView indexLast3Days(@PathVariable("client") String client,
                                       @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException {
        StatData d = service.getData(client, constantsList.get(0), count);

        if (d == null) {
            return new ModelAndView(NO_HISTORY_VIEW);
        }

        Map<String, Object> model = new HashMap<>(d.asModel());
        model.put("client", client);

        return new ModelAndView("history", model, HttpStatus.OK);
    }

    @RequestMapping(path = "/history/{client}/{parserName}")
    public ModelAndView nonIndexLast3Days(@PathVariable("client") String client,
                                          @RequestParam(name = "count", defaultValue = "864") int count,
                                          @PathVariable(name = "parserName") String parserName) throws ParseException {
        ParserDataForGUI parserDataForGUI = getConstantByName(parserName);
        return getDataAndView(client, parserDataForGUI, count, parserName);

    }

    private ModelAndView getDataAndView(String client, ParserDataForGUI parserDataForGUI, int count, String currentParserName)
            throws ParseException {
        StatData data = service.getData(client, parserDataForGUI, count);
        if (data == null) {
            return new ModelAndView(NO_HISTORY_VIEW);
        }
        Map<String, Object> model = new HashMap<>(data.asModel());
        model.put("client", client);
        model.put("constant", parserDataForGUI);
        model.put("currentParserName", currentParserName);
        model.put("constantsList", constantsList);

        return new ModelAndView(HISTORY_VIEW, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewByDate(String client, ParserDataForGUI parserDataForGUI, int year, int month, int day, String currentParserName)
            throws ParseException {
        return getDataAndViewByDate(client, parserDataForGUI, year, month, day, false, currentParserName);
    }

    private ModelAndView getDataAndViewByDate(String client, ParserDataForGUI parserDataForGUI, int year, int month, int day,
                                              boolean compress, String currentParserName) throws ParseException {
        StatData dataDate = service.getDataDate(client, parserDataForGUI, year, month, day);
        if (dataDate == null) {
            return new ModelAndView(NO_HISTORY_VIEW);
        }

        dataDate = compress ? service.compress(dataDate, 3 * 60 * 24 / 5) : dataDate;
        Map<String, Object> model = new HashMap<>(dataDate.asModel());
        model.put("client", client);
        model.put("year", year);
        model.put("month", month);
        model.put("day", day);
        model.put("constant", parserDataForGUI);
        model.put("currentParserName", currentParserName);
        model.put("constantsList", constantsList);
        return new ModelAndView(HISTORY_VIEW, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewCustom(String client, ParserDataForGUI parserDataForGUI, String from, String to, int maxResults,
                                              String currentParserName) throws ParseException {
        StatData data = service.getDataCustom(client, parserDataForGUI, from, to);
        if (data == null) {
            return new ModelAndView(NO_HISTORY_VIEW);
        }
        data = service.compress(data, maxResults);
        Map<String, Object> model = new HashMap<>(data.asModel());
        model.put("client", client);
        model.put("custom", true);
        model.put("from", from);
        model.put("to", to);
        model.put("maxResults", maxResults);
        model.put("constant", parserDataForGUI);
        model.put("currentParserName", currentParserName);
        model.put("constantsList", constantsList);
        return new ModelAndView(HISTORY_VIEW, model, HttpStatus.OK);
    }

    @RequestMapping(path = "/history/{client}/custom")
    public ModelAndView customIndex(@PathVariable("client") String client, @RequestParam("from") String from,
                                    @RequestParam("to") String to, @RequestParam("maxResults") int maxResults) throws ParseException {
        return getDataAndViewCustom(client, constantsList.get(0), from, to, maxResults, nameOfFirstConstant);
    }

    @RequestMapping(path = "/history/{client}/custom/{parserName}")
    public ModelAndView customActions(@PathVariable("client") String client, @RequestParam("from") String from,
                                      @RequestParam("to") String to, @RequestParam("maxResults") int count,
                                      @PathVariable(name = "parserName") String parserName) throws ParseException {
        ParserDataForGUI parserDataForGUI = getConstantByName(parserName);
        return getDataAndViewCustom(client, parserDataForGUI, from, to, count, parserName);
    }

    private ParserDataForGUI getConstantByName(String name) {
        return constantsList
                .stream()
                .filter(item -> item.getClass().getSimpleName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
