package org.cishell.cibridge.cishell;

import org.cishell.cibridge.cishell.impl.CIShellCIBridgeAlgorithmFacadeIT;
import org.cishell.cibridge.cishell.impl.CIShellCIBridgeDataFacadeIT;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CIShellContainerLauncher.class,
        CIShellCIBridgeDataFacadeIT.class,
        CIShellCIBridgeAlgorithmFacadeIT.class,
})

public class IntegrationTestSuite {
}