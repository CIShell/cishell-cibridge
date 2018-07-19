package org.cishell.cibridge.cishell;

import org.cishell.cibridge.cishell.impl.CIShellCIBridgeDataFacade;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeLoggingFacade;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeNotificationFacade;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeAlgorithmFacade;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeSchedulerFacade;
import org.cishell.cibridge.core.CIBridge;
import org.osgi.framework.BundleContext;

public class CIShellCIBridge extends CIBridge {
	private BundleContext context;

	public CIShellCIBridge(BundleContext context) {
		super(new CIShellCIBridgeAlgorithmFacade(context), new CIShellCIBridgeDataFacade(context),
				new CIShellCIBridgeNotificationFacade(context), new CIShellCIBridgeSchedulerFacade(context),
				new CIShellCIBridgeLoggingFacade(context));
		this.context = context;
	}
}
