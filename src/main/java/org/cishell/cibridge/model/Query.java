package org.cishell.cibridge.model;

import java.io.File;
import java.util.List;

import org.cishell.cibridge.resolvers.AlgorithmDefination_OSGI_mock;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

public class Query implements CIBridgeInstance,GraphQLRootResolver {
	private final AlgorithmDefination_OSGI_mock algorithmDefinationOSGIMock;
	
	//constructor 
	public Query(AlgorithmDefination_OSGI_mock algorithmDefinationOSGIMock) {
		this.algorithmDefinationOSGIMock=algorithmDefinationOSGIMock;
	}
	
	//resolver functions
//	public AlgorithmDefinitionQueryResults getAlgorithmDefinitionsNoFilter() {
//		// TODO Auto-generated method stub
//		return algorithmDefinationOSGIMock.getAlgorithmDefinationQuerResult();
//	}
	
	public AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter) {
		return algorithmDefinationOSGIMock.getAlgorithmDefinationQuerResult(filter);
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

//	public File downloadData(String dataId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
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

	public List<AlgorithmDefinition> getAlgorithmresults() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}