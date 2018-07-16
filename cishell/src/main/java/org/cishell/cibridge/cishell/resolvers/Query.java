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

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.cishell.cibridge.cishell.data.AlgorithmDefinationCIShell;

public class Query implements GraphQLQueryResolver, CIBridgeInstance {
	private final AlgorithmDefinationCIShell algorithmDefinationOSGICIShell;

	// constructor
	public Query(AlgorithmDefinationCIShell algorithmDefinationOSGICIShell) {
		this.algorithmDefinationOSGICIShell = algorithmDefinationOSGICIShell;
	}

	// resolver functions
	public List<AlgorithmDefinition> algorithmresults() {
		return algorithmDefinationOSGICIShell.getAlgorithmresults();
	}

	public AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter) {
		return algorithmDefinationOSGICIShell.getAlgorithmDefinitionQueryResult(filter);
	}

	public AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter) {
		return algorithmDefinationOSGICIShell.getAlgorithmInstanceQueryResults();
	}

	public String validateData(String algorithmDefinitionId, List<String> dataIds) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AlgorithmInstance> findConverters(String dataId, String outFormat) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AlgorithmInstance> findConvertersByFormat(String inFormat, String outFormat) {
		// TODO Auto-generated method stub
		return null;
	}

	public DataQueryResults getData(DataFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	// public File downloadData(String dataId) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	public String downloadData(String dataId) {
		// TODO Auto-generated method stub
		return null;
	}

	public NotificationQueryResults getNotifications(NotificationFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isClosed(String NotificationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isSchedulerEmpty() {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isSchedulerRunning() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSchedulerQueueWaiting() {
		// TODO Auto-generated method stub
		return 0;
	}

	public LogQueryResults getLogs(LogFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

}