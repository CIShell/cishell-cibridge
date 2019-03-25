package org.cishell.cibridge.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import java.util.List;

public class Query implements GraphQLQueryResolver {
    private CIBridge cibridge;

    public Query(CIBridge cibridge) {
        this.cibridge = cibridge;
        System.out.println("Query initialized");
    }

    public void setCIBridge(CIBridge cibridge) {
        this.cibridge = cibridge;
    }

    public CIBridge getCIBridge() {
        return this.cibridge;
    }

    // resolver functions
    public AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter) {
        return cibridge.algorithm.getAlgorithmDefinitions(filter);
    }

    public AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter) {
        return cibridge.algorithm.getAlgorithmInstances(filter);
    }

    public String validateData(String algorithmDefinitionId, List<String> dataIds) {
        return cibridge.data.validateData(algorithmDefinitionId, dataIds);
    }

    public List<AlgorithmDefinition> findConverters(String dataId, String outFormat) {
        return cibridge.data.findConverters(dataId, outFormat);
    }

    public List<AlgorithmDefinition> findConvertersByFormat(String inFormat, String outFormat) {
        return cibridge.data.findConvertersByFormat(inFormat, outFormat);
    }

    public DataQueryResults getData(DataFilter filter) {
        return cibridge.data.getData(filter);
    }

    public String downloadData(String dataId) {
        return cibridge.data.downloadData(dataId);
    }

    public NotificationQueryResults getNotifications(NotificationFilter filter) {
        System.out.println("Get Notifications Query");
        System.out.println(filter);
        return cibridge.notification.getNotifications(filter);
    }

    public Boolean isClosed(String notificationId) {
        System.out.println("Closed Notification Query");
        return cibridge.notification.isClosed(notificationId);
    }

    public Boolean isSchedulerEmpty() {
        return cibridge.scheduler.isSchedulerEmpty();
    }

    public Boolean isSchedulerRunning() {
        return cibridge.scheduler.isSchedulerRunning();
    }

    public int getSchedulerQueueWaiting() {
        return cibridge.scheduler.getSchedulerQueueWaiting();
    }

    public LogQueryResults getLogs(LogFilter filter) {
        return cibridge.logging.getLogs(filter);
    }

}