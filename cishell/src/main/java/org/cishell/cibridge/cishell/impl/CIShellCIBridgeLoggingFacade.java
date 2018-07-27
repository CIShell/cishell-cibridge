package org.cishell.cibridge.cishell.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.Log;
import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogLevel;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.cishell.cibridge.core.model.PageInfo;
import org.osgi.framework.ServiceEvent;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

public class CIShellCIBridgeLoggingFacade implements CIBridge.LoggingFacade {
	private CIShellCIBridge cibridge;

	/*LogReaderService logReaderService = (LogReaderService) getLogService();
	LogToFile fileLogger;
	List<LogReaderService> logReaders = new ArrayList<LogReaderService>();

	private ServiceListener serviceListener = new ServiceListener() {
		@Override
		public void serviceChanged(ServiceEvent event) {
			if (logReaderService != null) {
				if (event.getType() == ServiceEvent.REGISTERED) {
					this.logReaders.add(logReaderService);
					logReaderService.addLogListener(this.fileLogger);
				} else if (event.getType() == ServiceEvent.UNREGISTERING) {
					logReaderService.removeLogListener(this.fileLogger);
					this.logReaders.remove(logReaderService);
				}
			}
		}
	};
*/
	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}

	@Override
	public LogQueryResults getLogs(LogFilter filter) {
		
		
		
		// TODO Auto-generated method stub
	/*	LogService logService = this.cibridge.getLogService();
		List<LoqQueryResults> results = new ArrayList<>();
		HashSet<LogLevel> hset = new HashSet<LogLevel>();
		for(LogLevel temp:filter.loglevel)
		{
			hset.add(temp);
		}
		for(Log log: getLog())
		{
			if(hset.contains(log.getLevel()) && log.getTime() < filter.logsBefore)
				results.append(log);
		}
	*/	return null;
	}

	@Override
	public Log logAdded(List<LogLevel> logLevels) {
		// TODO Auto-generated method stub
		return null;
	}

//	private Boolean match(LogFilter filter, LogEntry log)
//	{
//		if log.getLevel()
//	}

//	private LogService getLogService() {
//		ServiceReference ref = context.getServiceReference(LogService.class.getName());
//		if (ref == null) {
//			throw new RuntimeException("The required OSGi LogService is not installed.");
//		} else {
//			return (LogService) context.getService(ref);
//		}
//	}
}