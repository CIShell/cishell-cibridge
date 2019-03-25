package org.cishell.cibridge.cishell;

import org.cishell.cibridge.cishell.impl.CIShellCIBridgeAlgorithmFacadeIT;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeDataFacadeIT;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeSchedulerFacadeIT;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CIShellContainerLauncher.class,
        //todo some tests in logging facade are still failing
//        CIShellCIBridgeLoggingFacadeIT.class,
        CIShellCIBridgeDataFacadeIT.class,
        CIShellCIBridgeAlgorithmFacadeIT.class,
        CIShellCIBridgeSchedulerFacadeIT.class
})

public class IntegrationTestSuite {
}