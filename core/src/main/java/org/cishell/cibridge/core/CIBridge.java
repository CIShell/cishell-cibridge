package org.cishell.cibridge.core;

import org.cishell.cibridge.core.model.*;
import org.reactivestreams.Publisher;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class CIBridge {
    public final AlgorithmFacade algorithm;
    public final DataFacade data;
    public final NotificationFacade notification;
    public final SchedulerFacade scheduler;
    public final LoggingFacade logging;

    public CIBridge(CIBridge.AlgorithmFacade algorithm, CIBridge.DataFacade data,
                    CIBridge.NotificationFacade notification, CIBridge.SchedulerFacade scheduler,
                    CIBridge.LoggingFacade logging) {
        this.algorithm = algorithm;
        this.data = data;
        this.notification = notification;
        this.scheduler = scheduler;
        this.logging = logging;
    }

    public interface AlgorithmFacade {
        // query
        AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter);

        AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter);

        // mutation
        AlgorithmInstance createAlgorithm(String algorithmDefinitionId, List<String> dataIds,
                                          List<PropertyInput> parameters);

        // subscription
        Publisher<AlgorithmDefinition> algorithmDefinitionAdded();

        Publisher<AlgorithmDefinition> algorithmDefinitionRemoved();

        Publisher<AlgorithmInstance> algorithmInstanceUpdated(AlgorithmFilter filter);

    }

    public interface DataFacade {
        // query
        String validateData(String algorithmDefinitionId, List<String> dataIds);

        List<AlgorithmDefinition> findConverters(String dataId, String outFormat);

        List<AlgorithmDefinition> findConvertersByFormat(String inFormat, String outFormat);

        DataQueryResults getData(DataFilter filter);

        String downloadData(String dataId);

        // mutation
        Data uploadData(String file, DataProperties properties);

        Boolean removeData(String dataId);

        Boolean updateData(String dataId, DataProperties properties);

        // subscription
        Publisher<Data> dataAdded();

        Publisher<Data> dataRemoved();

        Publisher<Data> dataUpdated();
    }

    public interface NotificationFacade {
        // query
        NotificationQueryResults getNotifications(NotificationFilter filter);

        Boolean isClosed(String NotificationId);

        // mutation
        Boolean setNotificationResponse(String notificationId, NotificationResponse response);

        Boolean closeNotification(String notificationId);

        // subscription
        Publisher<Notification> notificationAdded();

        Publisher<Notification> notificationUpdated();
    }

    public interface SchedulerFacade {
        // query
        Boolean isSchedulerEmpty();

        Boolean isSchedulerRunning();

        Integer getSchedulerQueueWaiting();

        // mutation
        Boolean runAlgorithmNow(String algorithmInstanceId);

        Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date);

        Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date);

        Boolean unscheduleAlgorithm(String algorithmInstanceId);

        Integer clearScheduler();

        Boolean setSchedulerRunning(Boolean running);

        Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean isCancelled);

        Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean isPaused);

        Boolean removeAlgorithm(String algorithmInstanceId);

        // subscription
        Publisher<Boolean> schedulerCleared();

        Publisher<Boolean> schedulerRunningChanged();
    }

    public interface LoggingFacade {
        // query
        LogQueryResults getLogs(LogFilter filter);

        // subscription
        Publisher<Log> logAdded(List<LogLevel> logLevels);
    }
}