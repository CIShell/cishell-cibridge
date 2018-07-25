package org.cishell.cibridge.cishell.impl;

import java.time.ZonedDateTime;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;

public class CIShellCIBridgeSchedulerFacade implements CIBridge.SchedulerFacade {
	private CIShellCIBridge cibridge;

	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}

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
