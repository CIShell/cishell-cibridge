package org.cishell.cibridge.cishell;

import org.cishell.cibridge.cishell.impl.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CIShellContainerLauncher.class,
        CIShellCIBridgeLoggingFacadeIT.class,
        CIShellCIBridgeDataFacadeIT.class,
        CIShellCIBridgeAlgorithmFacadeIT.class,
        CIShellCIBridgeSchedulerFacadeIT.class,
        CIShellCIBridgeDataConversionIT.class,
        CIBridgeGUIBuilderServiceIT.class,
        CIShellCIBridgeNotificationFacadeIT.class
})

public class IntegrationTestSuite {
}