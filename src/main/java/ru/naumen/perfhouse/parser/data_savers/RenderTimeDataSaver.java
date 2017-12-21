package ru.naumen.perfhouse.parser.data_savers;

import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.constants.DefaultConstants;
import ru.naumen.perfhouse.parser.data.RenderTimeData;

import java.util.concurrent.TimeUnit;

import static ru.naumen.perfhouse.parser.constants.RenderTime.RENDER_TIME;

@Service
public class RenderTimeDataSaver implements DataSaver<RenderTimeData> {

    @Override
    public void store(RenderTimeData dataSet, InfluxDAO influxDAO, BatchPoints batchPoints, String dbName,
                      long currentKey, boolean isPrintLog) {
        if (!dataSet.isEmpty()) {
            Point point = Point.measurement(DefaultConstants.MEASUREMENT_NAME)
                    .time(currentKey, TimeUnit.MILLISECONDS)
                    .addField(RENDER_TIME, dataSet.getRenderTime())
                    .build();

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
