package ru.naumen.perfhouse.parser.data_savers;

import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.constants.DefaultConstants;
import ru.naumen.perfhouse.parser.data.GCData;

import java.util.concurrent.TimeUnit;

import static ru.naumen.perfhouse.parser.constants.GarbageCollection.*;

@Service
public class GcDataSaver implements DataSaver<GCData> {

    @Override
    public void store(GCData dataSet, InfluxDAO influxDAO, BatchPoints batchPoints, String dbName, long currentKey,
                      boolean isPrintLog) {
        if (!dataSet.isEmpty()) {
            Point point = Point.measurement(DefaultConstants.MEASUREMENT_NAME).time(currentKey, TimeUnit.MILLISECONDS)
                    .addField(GCTIMES, dataSet.getGcTimes()).addField(AVARAGE_GC_TIME, dataSet.getCalculatedAvg())
                    .addField(MAX_GC_TIME, dataSet.getMaxGcTime()).build();

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
