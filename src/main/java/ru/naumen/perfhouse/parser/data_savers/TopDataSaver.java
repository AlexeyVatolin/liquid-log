package ru.naumen.perfhouse.parser.data_savers;

import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.constants.DefaultConstants;
import ru.naumen.perfhouse.parser.data.TopData;

import java.util.concurrent.TimeUnit;

import static ru.naumen.perfhouse.parser.constants.Top.*;

@Service
public class TopDataSaver implements DataSaver<TopData> {
    @Override
    public void store(TopData dataSet, InfluxDAO influxDAO, BatchPoints batchPoints, String dbName, long currentKey,
                      boolean isPrintLog) {
        if (!dataSet.isEmpty()) {
            Point point = Point.measurement(DefaultConstants.MEASUREMENT_NAME).time(currentKey, TimeUnit.MILLISECONDS)
                    .addField(AVG_LA, dataSet.getAvgLa()).addField(AVG_CPU, dataSet.getAvgCpuUsage())
                    .addField(AVG_MEM, dataSet.getAvgMemUsage()).addField(MAX_LA, dataSet.getMaxLa())
                    .addField(MAX_CPU, dataSet.getMaxCpu()).addField(MAX_MEM, dataSet.getMaxMem()).build();
            if (batchPoints != null)
            {
                batchPoints.getPoints().add(point);
            }
            else
            {
                influxDAO.write(dbName, "autogen", point);
            }
        }
    }
}
