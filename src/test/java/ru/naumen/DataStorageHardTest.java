package ru.naumen;

import org.influxdb.dto.BatchPoints;
import org.junit.Before;
import org.junit.Test;
import ru.naumen.perfhouse.influx.DataStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data_parsers.*;
import ru.naumen.perfhouse.parser.data_savers.DataSaver;
import ru.naumen.perfhouse.parser.dataset_factory.GCDataSetFactory;
import ru.naumen.perfhouse.parser.dataset_factory.SdngDataSetFactory;
import ru.naumen.perfhouse.parser.dataset_factory.TopDataSetFactory;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DataStorageHardTest
{
    private InfluxDAO influxDAOMock;
    private DataSaver dataSaver;
    private BatchPoints batchPoints;
    private final static String dbName= "logTest";

    @Before
    public void initializationBeforeEachTest()
    {
        influxDAOMock = mock(InfluxDAO.class);
        dataSaver = mock(DataSaver.class);
        batchPoints = BatchPoints.database(dbName).build();
        when(influxDAOMock.startBatchPoints(dbName)).thenReturn(batchPoints);
    }

    @Test
    public void storeCountSDNGTest() {
        //given
        DataParser dataParser = new SdngParser(new ErrorParser(), new ActionDoneParser());
        String errorLogLine = "10126 [localhost-startStop-1 - -] (07 сен 2017 04:58:16,761) WARN  server.SpringPropertyPlaceholderConfigurer - Could not load properties from URL [file:////home/administrator/.naumen/sd/conf/dbaccess.properties";
        String actionLogLine = "Done(10): AddObjectAction";
        DataStorage dataStorage = new DataStorage(influxDAOMock, new SdngDataSetFactory(), dataSaver);
        dataStorage.init(dbName, false);

        //when
        Data dataSet = dataStorage.get(1);
        dataParser.parseLine(dataSet, errorLogLine);
        dataParser.parseLine(dataSet, actionLogLine);
        Data dataSetWithKeyTwo = dataStorage.get(2);
        dataParser.parseLine(dataSetWithKeyTwo, errorLogLine);
        dataParser.parseLine(dataSetWithKeyTwo, actionLogLine);
        dataStorage.save();

        //then
        verify(dataSaver, times(1)).store(dataSet, influxDAOMock, batchPoints, dbName, 1,
                false);
        verify(dataSaver, times(1)).store(dataSetWithKeyTwo, influxDAOMock,batchPoints, dbName, 2,
                false);
    }

    @Test
    public void storeGCTest() {
        //given
        DataParser dataParser = new GCParser();
        String gcLogLine = "2017-11-03T10:41:03.724+0000: 6.549: [GC (Allocation Failure) [PSYoungGen: 655360K->58520K(764416K)] 655360K->58592K(2512384K), 0.0612895 secs] [Times: user=0.08 sys=0.01, real=0.06 secs] ";
        DataStorage dataStorage = new DataStorage(influxDAOMock, new GCDataSetFactory(), dataSaver);
        dataStorage.init(dbName, false);

        //when
        Data dataSet = dataStorage.get(1);
        dataParser.parseLine(dataSet, gcLogLine);
        Data dataSetWithKeyTwo = dataStorage.get(2);

        //then
        verify(dataSaver).store(dataSet, influxDAOMock, batchPoints, dbName, 1, false);
    }

    @Test
    public void storeCountGCTest() {
        //given
        DataParser dataParser = new GCParser();
        String gcLogLine = "2017-11-03T10:41:03.724+0000: 6.549: [GC (Allocation Failure) [PSYoungGen: 655360K->58520K(764416K)] 655360K->58592K(2512384K), 0.0612895 secs] [Times: user=0.08 sys=0.01, real=0.06 secs] ";
        DataStorage dataStorage = new DataStorage(influxDAOMock, new GCDataSetFactory(), dataSaver);
        dataStorage.init(dbName, false);

        //when
        Data dataSet = dataStorage.get(1);
        dataParser.parseLine(dataSet, gcLogLine);
        Data dataSetWithKeyTwo = dataStorage.get(2);
        dataParser.parseLine(dataSetWithKeyTwo, gcLogLine);
        dataStorage.save();

        //then
        verify(dataSaver, times(1)).store(dataSet, influxDAOMock, batchPoints, dbName,
                1, false);
        verify(dataSaver, times(1)).store(dataSetWithKeyTwo, influxDAOMock, batchPoints, dbName,
                2, false);
    }

    @Test
    public void storeTopTest() {
        //given
        DataParser dataParser = new TopParser();
        String topLogLine = "top - 00:00:01 up 219 days, 17:42,  0 users,  load average: 0.00, 0.01, 0.05";
        DataStorage dataStorage = new DataStorage(influxDAOMock, new TopDataSetFactory(), dataSaver);
        dataStorage.init(dbName, false);

        //when
        Data dataSet = dataStorage.get(1);
        dataParser.parseLine(dataSet, topLogLine);
        Data dataSetWithKeyTwo = dataStorage.get(2);

        //then
        verify(dataSaver).store(dataSet, influxDAOMock, batchPoints, dbName, 1, false);
    }

    @Test
    public void storeCountTopTest() {
        //given
        DataParser dataParser = new TopParser();
        String topLogLine = "top - 00:00:01 up 219 days, 17:42,  0 users,  load average: 0.00, 0.01, 0.05";
        DataStorage dataStorage = new DataStorage(influxDAOMock, new TopDataSetFactory(), dataSaver);
        dataStorage.init(dbName, false);

        //when
        Data dataSet = dataStorage.get(1);
        dataParser.parseLine(dataSet, topLogLine);
        Data dataSetWithKeyTwo = dataStorage.get(2);
        dataParser.parseLine(dataSetWithKeyTwo, topLogLine);
        dataStorage.save();

        //then
        verify(dataSaver, times(1)).store(dataSet, influxDAOMock, batchPoints, dbName,
                1, false);
        verify(dataSaver, times(1)).store(dataSetWithKeyTwo, influxDAOMock,batchPoints, dbName,
                2, false);
    }

}
