package ru.naumen.perfhouse.parser.data_savers;

import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Service;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.constants.DefaultConstants;
import ru.naumen.perfhouse.parser.data.ActionDoneData;
import ru.naumen.perfhouse.parser.data.ErrorData;
import ru.naumen.perfhouse.parser.data.SdngData;

import java.util.concurrent.TimeUnit;

import static ru.naumen.perfhouse.parser.constants.PerformedActions.*;
import static ru.naumen.perfhouse.parser.constants.ResponseTimes.*;


@Service
public class SdngDataSaver implements DataSaver<SdngData> {

    @Override
    public void store(SdngData dataSet, InfluxDAO influxDAO, BatchPoints batchPoints, String dbName, long currentKey,
                      boolean isPrintLog) {
        ActionDoneData dones = dataSet.getActionDoneData();
        dones.calculate();
        ErrorData errors = dataSet.getErrorData();
        if (isPrintLog) {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", currentKey, dones.getCount(),
                    dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                    dones.getPercent99(), dones.getPercent999(), dones.getMax(), errors.getErrorCount()));
        }
        if (!dones.isEmpty()) {
            Point.Builder builder = Point.measurement(DefaultConstants.MEASUREMENT_NAME).time(currentKey, TimeUnit.MILLISECONDS)
                    .addField(COUNT, dones.getCount())
                    .addField("min", dones.getMin())
                    .addField(MEAN, dones.getMean())
                    .addField(STDDEV, dones.getStddev())
                    .addField(PERCENTILE50, dones.getPercent50())
                    .addField(PERCENTILE95, dones.getPercent95())
                    .addField(PERCENTILE99, dones.getPercent99())
                    .addField(PERCENTILE999, dones.getPercent999())
                    .addField(MAX, dones.getMax())
                    .addField(ERRORS, errors.getErrorCount())
                    .addField(ADD_ACTIONS, dones.getAddObjectActions())
                    .addField(EDIT_ACTIONS, dones.getEditObjectsActions())
                    .addField(LIST_ACTIONS, dones.geListActions())
                    .addField(COMMENT_ACTIONS, dones.getCommentActions())
                    .addField(GET_FORM_ACTIONS, dones.getFormActions())
                    .addField(GET_DT_OBJECT_ACTIONS, dones.getDtObjectActions())
                    .addField(SEARCH_ACTIONS, dones.getSearchActions())
                    .addField(GET_CATALOGS_ACTIONS, dones.getCatalogsAction());

            Point point = builder.build();

            if (batchPoints != null) {
                batchPoints.getPoints().add(point);
            } else {
                influxDAO.write(dbName, "autogen", point);
            }
        }
    }
}
