package org.cishell.cibridge.cishell.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.Log;
import org.cishell.cibridge.core.model.LogFilter;
import org.cishell.cibridge.core.model.LogLevel;
import org.cishell.cibridge.core.model.LogQueryResults;
import org.cishell.cibridge.core.model.PageInfo;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeAlgorithmFacade; 
// import java.util.Date;
// import java.util.DateFormat;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.Instant;

public class CIShellCIBridgeLoggingFacade implements CIBridge.LoggingFacade {
	private CIShellCIBridge cibridge;
	private LogReaderService logReaderService; 
	private LogListener fileLogger;
	private List<LogReaderService> logReaders;
	private ServiceListener serviceListener; 

	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
		this.logReaderService = getLogService();
		this.logReaders =  new ArrayList<LogReaderService>();
		this.serviceListener = new ServiceListener() {
		@Override
		public void serviceChanged(ServiceEvent event) {
			if (logReaderService != null) {
				if (event.getType() == ServiceEvent.REGISTERED) {
					logReaders.add(logReaderService);
					logReaderService.addLogListener(fileLogger);
				} else if (event.getType() == ServiceEvent.UNREGISTERING) {
					logReaderService.removeLogListener(fileLogger);
					logReaders.remove(logReaderService);
				}
			}
		}
	};
	}

	@Override
	public LogQueryResults getLogs(LogFilter filter) {
		try{
		
		LogReaderService reader = getLogService();
		List<Log> logs = new ArrayList<Log>();
		LogQueryResults logResults = null;
		HashSet<LogLevel> loglevelSet = new HashSet<LogLevel>();
		PageInfo pageInfo = new PageInfo(false,false); 
		Enumeration<LogEntry> latestLogs = reader.getLog();
		HashMap<Integer,LogLevel> logLevelMap = new HashMap<>();
		int limit = 0,offset = 0;

		logLevelMap.put(1,LogLevel.ERROR);
		logLevelMap.put(2,LogLevel.WARNING);
		logLevelMap.put(3,LogLevel.INFO);
		logLevelMap.put(4,LogLevel.DEBUG);

		if(filter!= null){
			
			for(LogLevel temp:filter.logLevel){
				loglevelSet.add(temp);	
			}
			while(latestLogs.hasMoreElements()){
			LogEntry log = (LogEntry)latestLogs.nextElement();
			Log tempLog = new Log();
			if(logLevelMap.get(log.getLevel())!= null){
				if(loglevelSet.contains(logLevelMap.get(log.getLevel()))){
					tempLog.setLogLevel(logLevelMap.get(log.getLevel()));	
					tempLog.setMessage(log.getMessage());
					logs.add(tempLog);	
					}
			}
			//TODO Timestamp
			// if(filter.logsBefore!=null){
					// ZonedDateTime dateTime = Instant.ofEpochMilli(log.getTime())
        	    	//.atZone(ZoneId.of("-5:00"));
					// tempLog.setTimestamp(dateTime);
					// if((dateTime.compareTo(filter.logsBefore))<=0)
					// 	logs.add(tempLog);	
					// }
						
			}
				limit = filter.getLimit();
				offset = filter.getOffset();
		}
		else{
			System.out.println("Filter is empty!");
			while(latestLogs.hasMoreElements()){
				LogEntry log = (LogEntry)latestLogs.nextElement();
				Log tempLog = new Log();
			
				switch(log.getLevel())
				{
					case 1:
						tempLog.setLogLevel(LogLevel.ERROR);
						break;						
					case 2:
						tempLog.setLogLevel(LogLevel.WARNING);
						break;
					case 3:
						tempLog.setLogLevel(LogLevel.INFO);
						break;
					case 4:
						tempLog.setLogLevel(LogLevel.DEBUG);
						break;
					default:
						break;
				}
				
				tempLog.setMessage(log.getMessage());
				//TODO Timestamp
				// ZonedDateTime dateTime = Instant.ofEpochMilli(log.getTime())
    			// 	.atZone(ZoneId.of("-5:00"));
				// tempLog.setTimestamp(dateTime);
				logs.add(tempLog);	
			}
		}
		logResults = new LogQueryResults(logs,pageInfo);
		CIShellCIBridgeAlgorithmFacade algorithmFacadeObj = new CIShellCIBridgeAlgorithmFacade();
		return (LogQueryResults) algorithmFacadeObj.getQueryResults(logResults,limit,offset);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	//TODO
	@Override
	public Log logAdded(List<LogLevel> logLevels) {
		// TODO Auto-generated method stub
		return null;
	}


	private LogReaderService getLogService() {
		ServiceReference ref = this.cibridge.getBundleContext().getServiceReference(LogReaderService.class.getName());
		if (ref == null) {
			throw new RuntimeException("The required OSGi LogService is not installed.");
		} else {
			return (LogReaderService)this.cibridge.getBundleContext().getService(ref);
		}
	}
}