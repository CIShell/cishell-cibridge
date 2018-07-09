package org.cishell.cibridge.model;
import org.cishell.cibridge.model.*;
import java.io.File;

public interface CIBridgeInstance{
    AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter);
    AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter);
    String validateData(String ID, String dataIds[]);
    AlgorithmInstance findConverters(String dataId, String outFormat);
    AlgorithmInstance findConvertersByFormat(String inFormat,String outFormat);
    DataQueryResults getData(DataFilter filter);
    File downloadData(String dataId);
    NotificationQueryResults getNotifications(NotificationFilter filter);
    Boolean isClosed(String NotificationId);
    Boolean isSchedulerEmpty();
    Boolean isSchedulerRunning() ;
    int getSchedulerQueueWaiting();
    LogQueryResults getLogs(LogFilter filter);
}
