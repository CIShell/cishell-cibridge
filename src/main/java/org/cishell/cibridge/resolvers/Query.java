package org.cishell.cibridge.resolvers;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.cishell.cibridge.model.AlgorithmDefinition;
import org.cishell.cibridge.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.model.AlgorithmFilter;
import org.cishell.cibridge.model.AlgorithmInstance;
import org.cishell.cibridge.model.AlgorithmInstanceQueryResults;
import org.cishell.cibridge.model.CIBridgeInstance;
import org.cishell.cibridge.model.DataFilter;
import org.cishell.cibridge.model.DataQueryResults;
import org.cishell.cibridge.model.LogFilter;
import org.cishell.cibridge.model.LogQueryResults;
import org.cishell.cibridge.model.NotificationFilter;
import org.cishell.cibridge.model.NotificationQueryResults;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

public class Query implements GraphQLQueryResolver, CIBridgeInstance {
	private final AlgorithmDefination_OSGI_mock algorithmDefinationOSGIMock;

	// constructor
	public Query(AlgorithmDefination_OSGI_mock algorithmDefinationOSGIMock) {
		//System.out.println("**************************** reached here as well ****************************");
		this.algorithmDefinationOSGIMock = algorithmDefinationOSGIMock;
	}

	// resolver functions
	public List<AlgorithmDefinition> algorithmresults() {
		//System.out.println("**************************** reached here as well as well ****************************");
		return algorithmDefinationOSGIMock.getAlgorithmresults();
	}

	public AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter) {
		return algorithmDefinationOSGIMock.getAlgorithmDefinitionQueryResult(filter);
	}

	public AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter) {
		return algorithmDefinationOSGIMock.getAlgorithmInstanceQueryResults();
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