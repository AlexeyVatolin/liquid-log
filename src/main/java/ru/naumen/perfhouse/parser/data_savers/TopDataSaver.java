package ru.naumen.perfhouse.parser.data_savers;

import org.influxdb.dto.BatchPoints;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data.TopData;

@Service
public class TopDataSaver implements DataSaver<TopData> {
    @Override
    public void store(TopData dataSet, InfluxDAO influxDAO, BatchPoints batchPoints, String dbName, long currentKey,
                      boolean isPrintLog) {
        if (!dataSet.isEmpty()) {
            influxDAO.storeTop(batchPoints, dbName, currentKey, dataSet);
        }
    }
}
