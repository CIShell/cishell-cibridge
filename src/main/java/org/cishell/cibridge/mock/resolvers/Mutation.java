package org.cishell.cibridge.mock.resolvers;

import java.io.File;
import java.util.Date;
import java.util.List;
import org.cishell.cibridge.model.*;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;

public class Mutation implements GraphQLQueryResolver{

	public Mutation(){
		
	}

	AlgorithmInstance createAlgorithm(String algorithmDefinitionId, List<String> dataIds, List<PropertyInput> parameters) {
		return null;
	}

	Data uploadData(File file,DataProperties properties){
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