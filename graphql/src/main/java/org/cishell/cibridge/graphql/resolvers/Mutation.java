package org.cishell.cibridge.graphql.resolvers;


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
		return null;
	}

	Data uploadData(String file,DataProperties properties){
		return null;
	}
	Boolean removeData(String dataId) {
		return Boolean.TRUE;
	}
	Boolean updateData(String dataId,DataProperties properties){
		return Boolean.TRUE;
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
		return Boolean.TRUE;
	}
	Boolean scheduleAlgorithm(String algorithmInstanceId,Date date){
		return Boolean.TRUE;
	}

	Boolean rescheduleAlgorithm(String algorithmInstanceId,Date date){
		return Boolean.TRUE;
	}
	Boolean unscheduleAlgorithm(String algorithmInstanceId){
		return Boolean.TRUE;
	}
	int clearScheduler(){
		return 0;
	}
	Boolean setSchedulerRunning(Boolean running){
		return Boolean.TRUE;
	}
}