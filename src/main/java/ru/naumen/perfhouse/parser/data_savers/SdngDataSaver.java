package ru.naumen.perfhouse.parser.data_savers;

import org.influxdb.dto.BatchPoints;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data.ActionDoneData;
import ru.naumen.perfhouse.parser.data.ErrorData;
import ru.naumen.perfhouse.parser.data.SdngData;

@Service
public class SdngDataSaver implements DataSaver<SdngData> {

    @Override
    public void store(SdngData dataSet, InfluxDAO influxDAO, BatchPoints batchPoints, String dbName, long currentKey,
                      boolean isPrintLog) {
        ActionDoneData dones = dataSet.getActionDoneData();
        dones.calculate();
        ErrorData erros = dataSet.getErrorData();
        if (isPrintLog) {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", currentKey, dones.getCount(),
                    dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                    dones.getPercent99(), dones.getPercent999(), dones.getMax(), erros.getErrorCount()));
        }
        if (!dones.isEmpty()) {
            influxDAO.storeActionsFromLog(batchPoints, dbName, currentKey, dones, erros);
        }
    }
}
