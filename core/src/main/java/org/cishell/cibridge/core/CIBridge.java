package org.cishell.cibridge.core;

import java.time.ZonedDateTime;
import java.util.List;

import org.cishell.cibridge.core.CIBridge.AlgorithmFacade;
import org.cishell.cibridge.core.CIBridge.DataFacade;
import org.cishell.cibridge.core.CIBridge.LoggingFacade;
import org.cishell.cibridge.core.CIBridge.NotificationFacade;
import org.cishell.cibridge.core.CIBridge.SchedulerFacade;
import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmInstanceQueryResults;
import org.cishell.cibridge.core.model.Data;
import org.cishell.cibridge.core.model.DataFilter;
import org.cishell.cibridge.core.model.DataProperties;
import org.cishell.cibridge.core.model.DataQueryResults;
import org.cishell.cibridge.core.model.Log;
import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogLevel;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.cishell.cibridge.core.model.Notification;
import org.cishell.cibridge.core.model.NotificationFilter;
import org.cishell.cibridge.core.model.NotificationQueryResults;
import org.cishell.cibridge.core.model.NotificationResponse;
import org.cishell.cibridge.core.model.PropertyInput;

public abstract class CIBridge {
	public final AlgorithmFacade algorithm;
	public final DataFacade data;
	public final NotificationFacade notification;
	public final SchedulerFacade scheduler;
	public final LoggingFacade logging;
	
	public CIBridge(CIBridge.AlgorithmFacade algorithm, CIBridge.DataFacade data,CIBridge.NotificationFacade notification, CIBridge.SchedulerFacade scheduler, CIBridge.LoggingFacade logging) {
		this.algorithm = algorithm;
		this.data = data;
		this.notification=notification;
		this.scheduler=scheduler;
		this.logging=logging;
	}

	public interface AlgorithmFacade {
		//query
		List<AlgorithmDefinition> algorithmresults();
		AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter);
		AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter);
		//mutation
		AlgorithmInstance createAlgorithm(String algorithmDefinitionId, List<String> dataIds, List<PropertyInput> parameters);
		//subscription
		AlgorithmDefinition algorithmDefinitionAdded();
		AlgorithmDefinition algorithmDefinitionRemoved();
		AlgorithmInstance algorithmInstanceUpdated(AlgorithmFilter filter);
		
	}

	public interface DataFacade {
		//query
		String validateData(String algorithmDefinitionId, List<String> dataIds);
		List<AlgorithmInstance> findConverters(String dataId, String outFormat);
		List<AlgorithmInstance> findConvertersByFormat(String inFormat, String outFormat);
		DataQueryResults getData(DataFilter filter);
		String downloadData(String dataId);
		//mutation
		Data uploadData(String file, DataProperties properties);
		Boolean removeData(String dataId);
		Boolean updateData(String dataId, DataProperties properties);
		//subscription
		Data dataAdded();
		Data dataRemoved();
		Data dataUpdated();
	}

	public interface NotificationFacade {
		//query
		NotificationQueryResults getNotifications(NotificationFilter filter);
		Boolean isClosed(String NotificationId);
		//mutation
		Boolean setNotificationResponse(String notificationId, NotificationResponse response);
		Boolean closeNotification(String notificationId);
		Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean isCancelled);
		Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean isPaused);
		Boolean removeAlgorithm(String algorithmInstanceId);
		//subscription
		Notification notificationAdded();
		Notification notificationUpdated();
	}

	public interface SchedulerFacade {
		//query
		Boolean isSchedulerEmpty();
		Boolean isSchedulerRunning();
		Integer getSchedulerQueueWaiting();
		//mutation
		Boolean runAlgorithmNow(String algorithmInstanceId);
		Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date);
		Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date);
		Boolean unscheduleAlgorithm(String algorithmInstanceId);
		Integer clearScheduler();
		Boolean setSchedulerRunning(Boolean running);
		//subscription
		Boolean schedulerCleared();
		Boolean schedulerRunningChanged();
	}

	public interface LoggingFacade {
		//query
		LogQueryResults getLogs(LogFilter filter);
		//subscription
		Log logAdded(List<LogLevel> logLevels);
	}
}