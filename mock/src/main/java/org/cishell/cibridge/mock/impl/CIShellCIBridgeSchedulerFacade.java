package org.cishell.cibridge.mock.impl;

import org.cishell.cibridge.core.CIBridge;

import java.time.ZonedDateTime;

public class CIShellCIBridgeSchedulerFacade implements CIBridge.SchedulerFacade {

	@Override
	public Boolean isSchedulerEmpty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isSchedulerRunning() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getSchedulerQueueWaiting() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean runAlgorithmNow(String algorithmInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean unscheduleAlgorithm(String algorithmInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer clearScheduler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setSchedulerRunning(Boolean running) {
		// TODO Auto-generated method stub
		return null;
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

}
