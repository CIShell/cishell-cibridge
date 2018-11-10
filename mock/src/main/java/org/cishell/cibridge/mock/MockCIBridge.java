package org.cishell.cibridge.mock;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.mock.impl.*;

public class MockCIBridge extends CIBridge {

    public MockCIBridge() {
        super(new MockCIBridgeAlgorithmFacade(), new MockCIBridgeDataFacade(), new CIShellCIBridgeNotificationFacade(),
                new CIShellCIBridgeSchedulerFacade(), new CIShellCIBridgeLoggingFacade());
    }
}
