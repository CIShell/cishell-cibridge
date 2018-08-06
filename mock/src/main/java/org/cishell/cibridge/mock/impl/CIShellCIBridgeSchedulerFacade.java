package org.cishell.cibridge.mock.impl;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.mock.data.Scheduler;

import java.time.ZonedDateTime;

public class CIShellCIBridgeSchedulerFacade implements CIBridge.SchedulerFacade {

	private Scheduler scheduler = new Scheduler();

	@Override
	public Boolean isSchedulerEmpty() {
		return scheduler.getQueueWaiting() == 0;
	}

	@Override
	public Boolean isSchedulerRunning() {
		return scheduler.getRunning();
	}

	@Override
	public Integer getSchedulerQueueWaiting() {
		return scheduler.getQueueWaiting();
	}

	@Override
	public Boolean runAlgorithmNow(String algorithmInstanceId) {
		return scheduler.runAlgorithmNow(algorithmInstanceId);
	}

	@Override
	public Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
		return scheduler.scheduleAlgorithm(algorithmInstanceId, date);
	}

	@Override
	public Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
		return scheduler.rescheduleAlgorithm(algorithmInstanceId, date);
	}

	@Override
	public Boolean unscheduleAlgorithm(String algorithmInstanceId) {
		return scheduler.unscheduleAlgorithm(algorithmInstanceId);
	}

	@Override
	public Integer clearScheduler() {
		return scheduler.clearScheduler();
	}

	@Override
	public Boolean setSchedulerRunning(Boolean running) {
		return scheduler.setRunning(running);
	}


	@Override
	public Boolean schedulerCleared() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean schedulerRunningChanged() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean isCancelled) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean isPaused) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean removeAlgorithm(String algorithmInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}

}
