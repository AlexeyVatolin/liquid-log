package ru.naumen;

import org.influxdb.dto.BatchPoints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.naumen.perfhouse.influx.DataStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data.DataSet;
import ru.naumen.perfhouse.parser.data_parsers.*;

import static org.mockito.Mockito.*;

public class DataStorageTest {
    private InfluxDAO influxDAOMock;
    private DataStorage dataStorage;
    private BatchPoints batchPoints;
    private String dbName;

    public DataStorageTest()
    {
        dbName = "logTest";
        influxDAOMock = mock(InfluxDAO.class);
        batchPoints = BatchPoints.database(dbName).build();
        when(influxDAOMock.startBatchPoints(dbName)).thenReturn(batchPoints);
    }

    @Before
    public void initialization()
    {
        dataStorage = new DataStorage(influxDAOMock);
        dataStorage.init(dbName, false);
    }

    @Test
    public void getWithZeroTest() {
        //given

        //when
        DataSet dataSet = dataStorage.get(0);

        //then
        Assert.assertNotNull(dataSet);
    }

    @Test
    public void getWithNonZeroTest() {
        //given

        //when
        DataSet dataSet = dataStorage.get(1);

        //then
        Assert.assertNotNull(dataSet);
    }

    @Test
    public void simpleGetZeroTest() {
        //given

        //when
        DataSet dataSet1 = dataStorage.get(0);
        DataSet dataSet2 = dataStorage.get(0);

        //then
        Assert.assertEquals(dataSet1, dataSet2);
    }

    @Test
    public void simpleGetNonZeroTest() {
        //given

        //when
        DataSet dataSet1 = dataStorage.get(1);
        DataSet dataSet2 = dataStorage.get(1);

        //then
        Assert.assertEquals(dataSet1, dataSet2);
    }

    @Test
    public void getWithDifferentKeysTest() {
        //given

        //when
        DataSet dataSet1 = dataStorage.get(1);
        DataSet dataSet2 = dataStorage.get(2);

        //then
        Assert.assertNotEquals(dataSet1, dataSet2);
    }

    @Test
    public void getWithEditTest() {
        //given

        //when
        DataSet dataSet1 = dataStorage.get(0);
        dataSet1.getErrors().incrementErrorCount();
        DataSet dataSet2 = dataStorage.get(0);

        //then
        Assert.assertEquals(dataSet1.getErrors().getErrorCount(), dataSet2.getErrors().getErrorCount());
    }

    @Test
    public void storeSDNGTest() {
        //given
        DataParser dataParser = new GenericParser(new ErrorParser(), new ActionDoneParser());
        String errorLogLine = "10126 [localhost-startStop-1 - -] (07 сен 2017 04:58:16,761) WARN  server.SpringPropertyPlaceholderConfigurer - Could not load properties from URL [file:////home/administrator/.naumen/sd/conf/dbaccess.properties";
        String actionLogLine = "Done(10): AddObjectAction";

        //when
        DataSet dataSet1 = dataStorage.get(1);
        dataParser.parseLine(dataSet1, errorLogLine);
        dataParser.parseLine(dataSet1, actionLogLine);
        DataSet dataSet2 = dataStorage.get(2);

        //then
        verify(influxDAOMock).storeActionsFromLog(batchPoints, dbName, 1,
                dataSet1.getActionsDone(), dataSet1.getErrors());
    }

    @Test
    public void storeGCTest() {
        //given
        DataParser dataParser = new GCParser();
        String gcLogLine = "2017-11-03T10:41:03.724+0000: 6.549: [GC (Allocation Failure) [PSYoungGen: 655360K->58520K(764416K)] 655360K->58592K(2512384K), 0.0612895 secs] [Times: user=0.08 sys=0.01, real=0.06 secs] ";

        //when
        DataSet dataSet1 = dataStorage.get(1);
        dataParser.parseLine(dataSet1, gcLogLine);
        DataSet dataSet2 = dataStorage.get(2);

        //then
        verify(influxDAOMock).storeGc(batchPoints, dbName, 1, dataSet1.getGc());
    }

    @Test
    public void storeTopTest() {
        //given
        DataParser dataParser = new TopParser();
        String gcLogLine = "top - 00:00:01 up 219 days, 17:42,  0 users,  load average: 0.00, 0.01, 0.05";

        //when
        DataSet dataSet1 = dataStorage.get(1);
        dataParser.parseLine(dataSet1, gcLogLine);
        DataSet dataSet2 = dataStorage.get(2);

        //then
        verify(influxDAOMock).storeTop(batchPoints, dbName, 1, dataSet1.getTopData());
    }

}
