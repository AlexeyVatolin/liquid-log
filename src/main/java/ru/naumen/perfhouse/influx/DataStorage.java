package ru.naumen.perfhouse.influx;

import org.influxdb.dto.BatchPoints;
import ru.naumen.perfhouse.parser.data.*;
import ru.naumen.perfhouse.parser.data_savers.DataSaver;
import ru.naumen.perfhouse.parser.factories.ParserFactory;

public class DataStorage {
    private InfluxDAO influxDAO;
    private BatchPoints batchPoints;
    private String dbName;
    private Boolean isPrintLog;
    private long currentKey;
    private ParserFactory dataSetFactory;
    private Data dataSet;
    private DataSaver dataSaver;

    public DataStorage(InfluxDAO influxDAO, ParserFactory dataSetFactory, DataSaver dataSaver) {
        this.influxDAO = influxDAO;
        this.dataSetFactory = dataSetFactory;
        this.dataSaver = dataSaver;
    }

    public Data get(long key) {
        if (dataSet != null)
        {
            if (key == currentKey) {
                return dataSet;
            }
            store(dataSet);
        }
        currentKey = key;
        dataSet = dataSetFactory.getDataSet();
        return dataSet;
    }

    public void init(String dbName, Boolean isPrintLog) {
        this.dbName = dbName.replaceAll("-", "_");
        this.isPrintLog = isPrintLog;
        influxDAO.connectToDB(this.dbName);
        batchPoints = influxDAO.startBatchPoints(this.dbName);
    }

    public void save()
    {
        store(dataSet);
        influxDAO.writeBatch(batchPoints);
    }

    private void store(Data dataSet) {
        dataSaver.store(dataSet, influxDAO, batchPoints, dbName, currentKey, isPrintLog);
    }
}
