package ru.naumen.perfhouse.influx;

import org.influxdb.dto.BatchPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.naumen.perfhouse.parser.data.*;

@Repository
public class DataStorage {
    private InfluxDAO influxDAO;
    private BatchPoints batchPoints;
    private String dbName;
    private Boolean isPrintLog;
    private long currentKey;
    private DataSet dataSet;

    @Autowired
    public DataStorage(InfluxDAO influxDAO) {
        this.influxDAO = influxDAO;
    }

    public DataSet get(long key) {
        if (key == currentKey) {
            return dataSet;
        }
        store(dataSet);
        currentKey = key;
        dataSet = new DataSet();
        return dataSet;
    }

    public void init(String dbName, Boolean isPrintLog) {
        this.dbName = dbName.replaceAll("-", "_");
        this.isPrintLog = isPrintLog;
        influxDAO.connectToDB(this.dbName);
        batchPoints = influxDAO.startBatchPoints(this.dbName);
    }

    private void store(DataSet dataSet) {
        ActionDoneData dones = dataSet.getActionsDone();
        dones.calculate();
        ErrorData erros = dataSet.getErrors();
        if (isPrintLog) {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", currentKey, dones.getCount(),
                    dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                    dones.getPercent99(), dones.getPercent999(), dones.getMax(), erros.getErrorCount()));
        }
        if (!dones.isEmpty()) {
            influxDAO.storeActionsFromLog(batchPoints, dbName, currentKey, dones, erros);
        }

        GCData gc = dataSet.getGc();
        if (!gc.isEmpty()) {
            influxDAO.storeGc(batchPoints, dbName, currentKey, gc);
        }

        TopData cpuData = dataSet.cpuData();
        if (!cpuData.isEmpty()) {
            influxDAO.storeTop(batchPoints, dbName, currentKey, cpuData);
        }
        influxDAO.writeBatch(batchPoints);
    }
}
