package ru.naumen.perfhouse.parser.data_savers;

import org.influxdb.dto.BatchPoints;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data.GCData;

@Service
public class GcDataSaver implements DataSaver<GCData> {
    @Override
    public void store(GCData dataSet, InfluxDAO influxDAO, BatchPoints batchPoints, String dbName, long currentKey,
                      boolean isPrintLog) {
        if (!dataSet.isEmpty()) {
            influxDAO.storeGc(batchPoints, dbName, currentKey, dataSet);
        }
        influxDAO.writeBatch(batchPoints);
    }
}
