package ru.naumen.perfhouse.controllers;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.influx.InfluxDAOImpl;
import ru.naumen.perfhouse.parser.LogsParser;
import ru.naumen.perfhouse.parser.factories.ParserFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dkirpichenkov on 26.10.16.
 */
@Controller
public class ClientsController
{
    private Logger LOG = LoggerFactory.getLogger(ClientsController.class);
    private InfluxDAO influxDAO;
    private LogsParser logsParser;
    private AbstractApplicationContext applicationContext;

    @Autowired
    public ClientsController(InfluxDAOImpl influxDAO, LogsParser logsParser, AbstractApplicationContext applicationContext)
    {
        this.influxDAO = influxDAO;
        this.logsParser = logsParser;
        this.applicationContext = applicationContext;
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
        String[] availableParsers = applicationContext.getBeanNamesForType(ParserFactory.class);

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
        model.put("last3Dayslinks", clientLast3DaysLinks);
        model.put("lastWeeklinks", clientLastWeekLinks);
        model.put("prevMonthLinks", clientPreviousMonthLinks);
        model.put("availableParsers", availableParsers);

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
    @ResponseBody
    public void parseSendedFile(@RequestParam("DBName") String dbName,
                                      @RequestParam("parsingMode") String parsingMode,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam("timeZone") String timeZone,
                                      @RequestParam("needLog") Boolean needLog) throws IOException, ParseException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream()))
        {
            String fileName = file.getOriginalFilename();
            logsParser.parse(dbName, parsingMode, timeZone, needLog, fileName, inputStreamReader);
        }
        catch (ParseException | IOException ex)
        {
            LOG.error(ex.toString(), ex);
            throw ex;
        }
    }
}
