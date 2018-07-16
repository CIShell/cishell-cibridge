package org.cishell.cibridge.cishell.resolvers;

import java.util.List;

import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmInstanceQueryResults;
import org.cishell.cibridge.core.model.DataFilter;
import org.cishell.cibridge.core.model.DataQueryResults;
import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.cishell.cibridge.core.model.NotificationFilter;
import org.cishell.cibridge.core.model.NotificationQueryResults;

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

	interface AlgorithmFacade {
		List<AlgorithmDefinition> algorithmresults();
		AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter);
		AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter);
	}

	interface DataFacade {
		String validateData(String algorithmDefinitionId, List<String> dataIds);
		List<AlgorithmInstance> findConverters(String dataId, String outFormat);
		List<AlgorithmInstance> findConvertersByFormat(String inFormat, String outFormat);
		DataQueryResults getData(DataFilter filter);
		String downloadData(String dataId);

	}

	interface NotificationFacade {
		NotificationQueryResults getNotifications(NotificationFilter filter);
		Boolean isClosed(String NotificationId);
	}

	interface SchedulerFacade {
		Boolean isSchedulerEmpty();
		Boolean isSchedulerRunning();
		Integer getSchedulerQueueWaiting();
	}

	interface LoggingFacade {
		LogQueryResults getLogs(LogFilter filter);
	}
}