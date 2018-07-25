package org.cishell.cibridge.cishell;

import org.cishell.cibridge.cishell.impl.CIShellCIBridgeDataFacade;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeLoggingFacade;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeNotificationFacade;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeAlgorithmFacade;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeSchedulerFacade;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.CIBridge.AlgorithmFacade;
import org.cishell.cibridge.core.CIBridge.DataFacade;
import org.cishell.cibridge.core.CIBridge.LoggingFacade;
import org.cishell.cibridge.core.CIBridge.NotificationFacade;
import org.cishell.cibridge.core.CIBridge.SchedulerFacade;
import org.osgi.framework.BundleContext;

public class CIShellCIBridge extends CIBridge {
	private BundleContext context;
	
	public final CIShellCIBridgeAlgorithmFacade cishellAlgorithm;
	public final CIShellCIBridgeDataFacade cishellData;
	public final CIShellCIBridgeNotificationFacade cishellNotification;
	public final CIShellCIBridgeSchedulerFacade cishellScheduler;
	public final CIShellCIBridgeLoggingFacade cishellLogging;

	public CIShellCIBridge(BundleContext context) {
		super(new CIShellCIBridgeAlgorithmFacade(context), new CIShellCIBridgeDataFacade(context),
				new CIShellCIBridgeNotificationFacade(context), new CIShellCIBridgeSchedulerFacade(context),
				new CIShellCIBridgeLoggingFacade(context));
		this.context = context;
		
		this.cishellAlgorithm = (CIShellCIBridgeAlgorithmFacade) this.algorithm;
		this.cishellData = (CIShellCIBridgeDataFacade) this.data;
		this.cishellNotification = (CIShellCIBridgeNotificationFacade) this.notification;
		this.cishellScheduler = (CIShellCIBridgeSchedulerFacade) this.scheduler;
		this.cishellLogging = (CIShellCIBridgeLoggingFacade) this.logging;
		
		cishellAlgorithm.setCIBridge(this);
		cishellData.setCIBridge(this);
		cishellNotification.setCIBridge(this);
		cishellScheduler.setCIBridge(this);
		cishellLogging.setCIBridge(this);
		
	}
}
