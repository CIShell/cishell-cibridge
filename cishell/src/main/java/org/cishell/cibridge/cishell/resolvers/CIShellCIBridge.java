package org.cishell.cibridge.cishell.resolvers;

import org.osgi.framework.BundleContext;

public class CIShellCIBridge extends CIBridge {
	private BundleContext context;

	public CIShellCIBridge(BundleContext context) {
		super(new CIShellCIBridgeAlgorithmFacade(context), new CIShellCIBridgeDataFacade(context), new CIShellCIBridgeNotificationFacade(context), new CIShellCIBridgeSchedulerFacade(context), new CIShellCIBridgeLoggingFacade(context));
		this.context = context;
	}
}









