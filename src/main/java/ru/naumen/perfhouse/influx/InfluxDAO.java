package ru.naumen.perfhouse.influx;

import org.influxdb.dto.BatchPoints;
import ru.naumen.perfhouse.parser.data.ActionDoneData;
import ru.naumen.perfhouse.parser.data.ErrorData;
import ru.naumen.perfhouse.parser.data.GCData;
import ru.naumen.perfhouse.parser.data.TopData;

public interface InfluxDAO {
    public void connectToDB(String dbName);
    public BatchPoints startBatchPoints(String dbName);
    public void storeActionsFromLog(BatchPoints batch, String dbName, long date, ActionDoneData dones,
                                    ErrorData errors);
    public void storeGc(BatchPoints batch, String dbName, long date, GCData gc);
    public void storeTop(BatchPoints batch, String dbName, long date, TopData data);
    public void writeBatch(BatchPoints batch);
}
