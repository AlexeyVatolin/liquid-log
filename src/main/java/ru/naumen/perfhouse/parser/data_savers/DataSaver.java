package ru.naumen.perfhouse.parser.data_savers;

import org.influxdb.dto.BatchPoints;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data.Data;

public interface DataSaver<T extends Data> {
    public void store(T dataSet, InfluxDAO influxDAO, BatchPoints batchPoints, String dbName, long currentKey,
                      boolean isPrintLog);
}
