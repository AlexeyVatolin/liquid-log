package ru.naumen.perfhouse.influx;

import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.json.JSONObject;

import java.util.List;

public interface InfluxDAO {
    void connectToDB(String dbName);
    QueryResult.Series executeQuery(String dbName, String query);
    List<String> getDbList();
    BatchPoints startBatchPoints(String dbName);
    void write(String dbName, String s, Point point);
    void storeFromJSon(BatchPoints batch, String dbName, JSONObject data);
    void writeBatch(BatchPoints batch);
}
