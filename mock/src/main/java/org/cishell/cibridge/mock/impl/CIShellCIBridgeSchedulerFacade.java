package org.cishell.cibridge.mock.impl;

import java.time.ZonedDateTime;

import org.cishell.cibridge.core.CIBridge;
import org.osgi.framework.BundleContext;

public class CIShellCIBridgeSchedulerFacade implements CIBridge.SchedulerFacade {
	
	private final BundleContext context;
	
	public CIShellCIBridgeSchedulerFacade(BundleContext context){
		this.context=context;
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