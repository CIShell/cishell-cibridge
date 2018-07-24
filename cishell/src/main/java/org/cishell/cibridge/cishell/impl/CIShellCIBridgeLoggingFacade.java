package org.cishell.cibridge.cishell.resolvers;

import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.osgi.framework.BundleContext;
//added
import org.osgi.service.log.LogService;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceListener;


public class CIShellCIBridgeLoggingFacade implements CIBridge.LoggingFacade {

	private final BundleContext context;

	CIShellCIBridgeLoggingFacade(BundleContext context){
		this.context=context;
	}

	@Override
	public LogQueryResults getLogs(LogFilter filter) {
		// TODO Auto-generated method stub
		LogService logService = this.getLogService();
		ServiceListener logServiceListener = new LogServiceListener();
		context.addServiceListener( logServiceListener, filter);
		return null;
	}


	private LogService getLogService() {
		ServiceReference ref = context.getServiceReference(LogService.class.getName());
		if (ref == null) {
			throw new RuntimeException("The required OSGi LogService is not installed.");
		} else {
			return (LogService) context.getService(ref);
		}
	}
}
