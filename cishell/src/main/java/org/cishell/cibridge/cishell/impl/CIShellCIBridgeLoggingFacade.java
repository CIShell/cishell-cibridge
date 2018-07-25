package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.core.model.Log;
import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogLevel;

import java.util.List;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.osgi.framework.BundleContext;

public class CIShellCIBridgeLoggingFacade implements CIBridge.LoggingFacade {
	private final BundleContext context;
	private CIShellCIBridge cibridge;

	public CIShellCIBridgeLoggingFacade(BundleContext context) {
		this.context = context;
	}
	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}

	@Override
	public LogQueryResults getLogs(LogFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Log logAdded(List<LogLevel> logLevels) {
		// TODO Auto-generated method stub
		return null;
	}

}
