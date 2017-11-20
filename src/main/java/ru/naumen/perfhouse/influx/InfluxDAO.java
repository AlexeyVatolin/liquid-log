package ru.naumen.perfhouse.influx;

import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.QueryResult;
import org.json.JSONObject;
import ru.naumen.perfhouse.parser.data.ActionDoneData;
import ru.naumen.perfhouse.parser.data.ErrorData;
import ru.naumen.perfhouse.parser.data.GCData;
import ru.naumen.perfhouse.parser.data.TopData;

import java.util.List;

public interface InfluxDAO {
    public void connectToDB(String dbName);
    public QueryResult.Series executeQuery(String dbName, String query);
    public List<String> getDbList();
    public BatchPoints startBatchPoints(String dbName);
    public void storeFromJSon(BatchPoints batch, String dbName, JSONObject data);
    public void storeActionsFromLog(BatchPoints batch, String dbName, long date, ActionDoneData dones,
                                    ErrorData errors);
    public void storeGc(BatchPoints batch, String dbName, long date, GCData gc);
    public void storeTop(BatchPoints batch, String dbName, long date, TopData data);
    public void writeBatch(BatchPoints batch);
}
