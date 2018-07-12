package org.cishell.cibridge.model;
import org.cishell.cibridge.model.*;
import java.io.File;
import java.util.List;

public interface CIBridgeInstance{
	// algorithm must return
//	AlgorithmDefinitionQueryResults getAlgorithmDefinitionsNoFilter();
	List<AlgorithmDefinition> algorithmresults();
    AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter);
    AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter);
    //data must return
    String validateData(String algorithmDefinitionId, List<String> dataIds);
    List<AlgorithmInstance> findConverters(String dataId, String outFormat);
    List<AlgorithmInstance> findConvertersByFormat(String inFormat,String outFormat);
    DataQueryResults getData(DataFilter filter);
//    File downloadData(String dataId);
    String downloadData(String dataId);
    //notification must return
    NotificationQueryResults getNotifications(NotificationFilter filter);
    //scheduler must return
    Boolean isClosed(String NotificationId);
    Boolean isSchedulerEmpty();
    Boolean isSchedulerRunning() ;
    int getSchedulerQueueWaiting();
    //Logging must return
    LogQueryResults getLogs(LogFilter filter);
}
