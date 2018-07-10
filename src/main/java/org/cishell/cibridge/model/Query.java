package org.cishell.cibridge.model;
import java.io.File;

public class Query implements CIBridgeInstance{
    String validateData(String ID, String dataIds[]){
        return new String("Hello");
    }
    AlgorithmInstance findConverters(String dataId, String outFormat)
    {
        System.out.println("Find Converters");
        return null;
    }
    AlgorithmInstance findConvertersByFormat(String inFormat,String outFormat)
    {
        System.out.println("Find Converters By Format");
        return  null;
    }
    DataQueryResults getData(DataFilter filter)
    {
        System.out.println("get Data");
        return null;
    }
    File downloadData(String dataId)
    {
        System.out.println(dataId + "download Data");
        return null;
    }
    NotificationQueryResults getNotifications(NotificationFilter filter)
    {
        System.out.println("getNotifications");
        return null;
    }
    Boolean isClosed(String NotificationId)
    {
        System.out.println("isClosed");
        return Boolean.TRUE;
    }
    Boolean isSchedulerEmpty()
    {
        System.out.println("isSchedulerEmpty");
        return Boolean.TRUE;
    }
    Boolean isSchedulerRunning()
    {
        System.out.println("isSchedulerRunning");
        return Boolean.TRUE;
    }
    int getSchedulerQueueWaiting()
    {
        return 20;
    }
    LogQueryResults getLogs(LogFilter filter)
    {
        System.out.println("getLogs");
        return null;
    }
}