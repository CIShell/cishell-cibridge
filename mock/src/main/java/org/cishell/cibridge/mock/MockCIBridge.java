package org.cishell.cibridge.mock;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.mock.impl.CIShellCIBridgeLoggingFacade;
import org.cishell.cibridge.mock.impl.CIShellCIBridgeNotificationFacade;
import org.cishell.cibridge.mock.impl.CIShellCIBridgeSchedulerFacade;
import org.cishell.cibridge.mock.impl.MockCIBridgeAlgorithmFacade;
import org.cishell.cibridge.mock.impl.MockCIBridgeDataFacade;
import org.osgi.framework.BundleContext;

public class MockCIBridge extends CIBridge {
	private BundleContext context;

	public MockCIBridge(BundleContext context) {
		super(new MockCIBridgeAlgorithmFacade(context), new MockCIBridgeDataFacade(context), new CIShellCIBridgeNotificationFacade(context), 
				new CIShellCIBridgeSchedulerFacade(context), new CIShellCIBridgeLoggingFacade(context));
		this.context = context;
	}
}
