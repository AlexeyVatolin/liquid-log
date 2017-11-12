package ru.naumen.perfhouse.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.Parser;

/**
 * Created by dkirpichenkov on 26.10.16.
 */
@Controller
public class ClientsController
{
    private Logger LOG = LoggerFactory.getLogger(ClientsController.class);
    private InfluxDAO influxDAO;
    private Parser parser;

    @Inject
    public ClientsController(InfluxDAO influxDAO, Parser parser)
    {
        this.influxDAO = influxDAO;
        this.parser = parser;

    }

    @RequestMapping(path = "/")
    public ModelAndView index()
    {
        List<String> clients = influxDAO.getDbList();
        HashMap<String, Object> clientLast3DaysLinks = new HashMap<>();
        HashMap<String, Object> clientLinks = new HashMap<>();
        HashMap<String, Object> clientMonthLinks = new HashMap<>();
        HashMap<String, Object> clientLastWeekLinks = new HashMap<>();
        HashMap<String, Object> clientPreviousMonthLinks = new HashMap<>();

        DateTime now = DateTime.now();
        DateTime prevMonth = now.minusMonths(1);
        DateTime yesterday = now.minusDays(1);

        clients.forEach(it -> {
            clientLinks.put(it, "/history/" + it + "/" + yesterday.getYear() + "/" + yesterday.getMonthOfYear() + "/"
                    + yesterday.getDayOfMonth());

            clientMonthLinks.put(it, "/history/" + it + "/" + now.getYear() + "/" + now.getMonthOfYear());
            clientPreviousMonthLinks.put(it,
                    "/history/" + it + "/" + prevMonth.getYear() + "/" + prevMonth.getMonthOfYear());
            clientLast3DaysLinks.put(it, "/history/" + it + "?count=864");
            clientLastWeekLinks.put(it, "/history/" + it + "?count=2016");
        });

        HashMap<String, Object> model = new HashMap<>();
        model.put("clients", clients);
        model.put("links", clientLinks);
        model.put("monthlinks", clientMonthLinks);
        model.put("last864links", clientLast3DaysLinks);
        model.put("last2016links", clientLastWeekLinks);
        model.put("prevMonthLinks", clientPreviousMonthLinks);

        return new ModelAndView("clients", model, HttpStatus.OK);
    }

    @RequestMapping(path = "{client}", method = RequestMethod.POST)
    public void postClientStatFormat1(@PathVariable("client") String client, HttpServletRequest request,
            HttpServletResponse response) throws IOException
    {
        try
        {
            client = client.replaceAll("-", "_");
            influxDAO.connectToDB(client);
            String data = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject measure = new JSONObject(data);
            influxDAO.storeFromJSon(null, client, measure);
            response.sendError(HttpServletResponse.SC_OK);
        }
        catch (Exception ex)
        {
            LOG.error(ex.toString(), ex);
            throw ex;
        }
    }

    @RequestMapping(path = "/parser/parse", method = RequestMethod.POST)
    public void postClientStatFormat1(@RequestParam("DBName") String dbName,
                                      @RequestParam("parsingMode") String parsingMode,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam("timeZone") String timeZone,
                                      @RequestParam("needLog") Boolean needLog,
                                      HttpServletResponse response){
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
            parser.parse(dbName, parsingMode, timeZone, needLog, inputStreamReader);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
