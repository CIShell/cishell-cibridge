package org.cishell.cibridge.graphql.resolvers;


import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import java.time.ZonedDateTime;
import java.util.List;

public class Mutation implements GraphQLMutationResolver {
    private CIBridge cibridge;

    public Mutation(CIBridge cibridge) {
        this.cibridge = cibridge;
        System.out.println("Mutation initialized");
    }

    public void setCIBridge(CIBridge cibridge) {
        this.cibridge = cibridge;
    }

    public CIBridge getCIBridge() {
        return this.cibridge;
    }

    public AlgorithmInstance createAlgorithm(String algorithmDefinitionId, List<String> dataIds, List<PropertyInput> parameters) {
        return cibridge.algorithm.createAlgorithm(algorithmDefinitionId, dataIds, parameters);
    }

    public Data uploadData(String file, DataProperties properties) {
        return cibridge.data.uploadData(file, properties);
    }

    public Boolean removeData(String dataId) {
        return cibridge.data.removeData(dataId);
    }

    public Boolean updateData(String dataId, DataProperties properties) {
        return cibridge.data.updateData(dataId, properties);
    }

    public Boolean setNotificationResponse(String notificationId, NotificationResponse response) {
        return Boolean.TRUE;
    }

    public Boolean closeNotification(String notificationId) {
        return Boolean.TRUE;
    }

    public Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean isCancelled) {
        return cibridge.scheduler.setAlgorithmCancelled(algorithmInstanceId, isCancelled);
    }

    public Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean isPaused) {
        return cibridge.scheduler.setAlgorithmPaused(algorithmInstanceId, isPaused);
    }

    public Boolean removeAlgorithm(String algorithmInstanceId) {
        return cibridge.scheduler.removeAlgorithm(algorithmInstanceId);
    }

    public Boolean runAlgorithmNow(String algorithmInstanceId) {
        return cibridge.scheduler.runAlgorithmNow(algorithmInstanceId);
    }

    public Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        return cibridge.scheduler.scheduleAlgorithm(algorithmInstanceId, date);
    }

    public Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        return cibridge.scheduler.rescheduleAlgorithm(algorithmInstanceId, date);
    }

    public Boolean unscheduleAlgorithm(String algorithmInstanceId) {
        return cibridge.scheduler.unscheduleAlgorithm(algorithmInstanceId);
    }

    public int clearScheduler() {
        return cibridge.scheduler.clearScheduler();
    }

    public Boolean setSchedulerRunning(Boolean running) {
        return cibridge.scheduler.setSchedulerRunning(running);
    }
}