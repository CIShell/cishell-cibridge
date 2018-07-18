package org.cishell.cibridge.cishell.impl;

import org.osgi.framework.BundleContext;
import org.cishell.cibridge.core.CIBridge;

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

}
