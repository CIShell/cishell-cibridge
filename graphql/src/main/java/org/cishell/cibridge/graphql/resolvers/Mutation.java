package org.cishell.cibridge.graphql.resolvers;


import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

public class Mutation implements GraphQLMutationResolver{
	private CIBridge cibridge;
	
	public Mutation(CIBridge cibridge){
		this.cibridge = cibridge;
		System.out.println("Mutation initialized");
	}
	
	public void setCIBridge(CIBridge cibridge) {
		this.cibridge = cibridge;
	}
	public CIBridge getCIBridge() {
		return this.cibridge;
	}

	AlgorithmInstance createAlgorithm(String algorithmDefinitionId, List<String> dataIds, List<PropertyInput> parameters) {
		return cibridge.algorithm.createAlgorithm(algorithmDefinitionId, dataIds, parameters);
	}

	Data uploadData(String file,DataProperties properties){
		return cibridge.data.uploadData(file, properties);
	}
	Boolean removeData(String dataId) {
		return cibridge.data.removeData(dataId);
	}
	Boolean updateData(String dataId,DataProperties properties){
		return cibridge.data.updateData(dataId, properties);
	}
	Boolean setNotificationResponse(String notificationId,NotificationResponse response)
	{
		return  Boolean.TRUE;
	}
	Boolean closeNotification(String notificationId)
	{
		return Boolean.TRUE;
	}
	Boolean setAlgorithmCancelled(String algorithmInstanceId,Boolean isCancelled)
	{
		return Boolean.TRUE;
	}
	Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean isPaused)
	{
		return Boolean.TRUE;
	}
	Boolean removeAlgorithm(String algorithmInstanceId)
	{
		return Boolean.TRUE;
	}
	Boolean runAlgorithmNow(String algorithmInstanceId)
	{
		return cibridge.scheduler.runAlgorithmNow(algorithmInstanceId);
	}
	Boolean scheduleAlgorithm(String algorithmInstanceId,ZonedDateTime date){
		return cibridge.scheduler.scheduleAlgorithm(algorithmInstanceId, date);
	}
	Boolean rescheduleAlgorithm(String algorithmInstanceId,ZonedDateTime date){
		return cibridge.scheduler.rescheduleAlgorithm(algorithmInstanceId, date);
	}
	Boolean unscheduleAlgorithm(String algorithmInstanceId){
		return cibridge.scheduler.unscheduleAlgorithm(algorithmInstanceId);
	}
	int clearScheduler(){
		return cibridge.scheduler.clearScheduler();
	}
	Boolean setSchedulerRunning(Boolean running){
		return cibridge.scheduler.setSchedulerRunning(running);
	}
}