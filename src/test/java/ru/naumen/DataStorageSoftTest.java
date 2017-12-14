package ru.naumen;

import org.influxdb.dto.BatchPoints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.naumen.perfhouse.influx.DataStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.parser.data.Data;
import ru.naumen.perfhouse.parser.data.SdngData;
import ru.naumen.perfhouse.parser.data_savers.DataSaver;
import ru.naumen.perfhouse.parser.data_parsers.*;
import ru.naumen.perfhouse.parser.factories.SdngFactory;
import static org.mockito.Mockito.*;

public class DataStorageSoftTest {
    private InfluxDAO influxDAOMock;
    private DataSaver dataSaver;
    private BatchPoints batchPoints;
    private DataStorage dataStorage;
    private final static String dbName= "logTest";

    @Before
    public void initializationBeforeEachTest()
    {
        influxDAOMock = mock(InfluxDAO.class);
        dataSaver = mock(DataSaver.class);
        batchPoints = BatchPoints.database(dbName).build();
        when(influxDAOMock.startBatchPoints(dbName)).thenReturn(batchPoints);
        SdngFactory sdngFactoryMock = mock(SdngFactory.class);
        when(sdngFactoryMock.getDataSet()).thenAnswer(ans -> new SdngData());
        dataStorage = new DataStorage(influxDAOMock, sdngFactoryMock, dataSaver);
        dataStorage.init(dbName, false);
    }

    @Test
    public void getWithZeroTest() {
        //given

        //when
        Data dataSet = dataStorage.get(0);

        //then
        Assert.assertNotNull(dataSet);
    }

    @Test
    public void getWithNonZeroTest() {
        //given

        //when
        Data dataSet = dataStorage.get(1);

        //then
        Assert.assertNotNull(dataSet);
    }

    @Test
    public void simpleGetZeroTest() {
        //given

        //when
        Data dataSet = dataStorage.get(0);
        Data dataSetCopy = dataStorage.get(0);

        //then
        Assert.assertEquals(dataSet, dataSetCopy);
    }

    @Test
    public void simpleGetNonZeroTest() {
        //given

        //when
        Data dataSet = dataStorage.get(1);
        Data dataSetCopy = dataStorage.get(1);

        //then
        Assert.assertEquals(dataSet, dataSetCopy);
    }

    @Test
    public void getWithDifferentKeysTest() {
        //given

        //when
        Data dataSetWithKeyOne = dataStorage.get(1);
        Data dataSetWithKeyTwo = dataStorage.get(2);

        //then
        Assert.assertNotEquals(dataSetWithKeyOne, dataSetWithKeyTwo);
    }

    @Test
    public void getWithEditTest() {
        //given
        DataParser dataParser = new SdngParser(new ErrorParser(), new ActionDoneParser());
        String errorLogLine = "10126 [localhost-startStop-1 - -] (07 сен 2017 04:58:16,761) WARN  " +
                "server.SpringPropertyPlaceholderConfigurer - Could not load properties from URL " +
                "[file:////home/administrator/.naumen/sd/conf/dbaccess.properties";

        //when
        Data dataSet = dataStorage.get(0);
        dataParser.parseLine(dataSet, errorLogLine);
        Data dataSetCopy = dataStorage.get(0);

        //then
        Assert.assertEquals(dataSet, dataSet);
    }

    @Test
    public void storeSDNGTest() {
        //given
        DataParser dataParser = new SdngParser(new ErrorParser(), new ActionDoneParser());
        String errorLogLine = "10126 [localhost-startStop-1 - -] (07 сен 2017 04:58:16,761) WARN  " +
                "server.SpringPropertyPlaceholderConfigurer - Could not load properties from URL " +
                "[file:////home/administrator/.naumen/sd/conf/dbaccess.properties";
        String actionLogLine = "Done(10): AddObjectAction";

        //when
        Data dataSet = dataStorage.get(1);
        dataParser.parseLine(dataSet, errorLogLine);
        dataParser.parseLine(dataSet, actionLogLine);
        Data dataSetWithKeyTwo = dataStorage.get(2);

        //then
        verify(dataSaver).store(dataSet, influxDAOMock, batchPoints, dbName, 1, false);
    }

}
