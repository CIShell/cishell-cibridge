package org.cishell.cibridge.cishell.resolvers;

import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.osgi.framework.BundleContext;

public class CIShellCIBridgeLoggingFacade implements CIBridge.LoggingFacade {
	
	private final BundleContext context;
	
	CIShellCIBridgeLoggingFacade(BundleContext context){
		this.context=context;
	}

	@Override
	public LogQueryResults getLogs(LogFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
