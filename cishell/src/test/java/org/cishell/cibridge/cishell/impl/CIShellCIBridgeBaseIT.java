package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CIShellCIBridgeBaseIT extends IntegrationTestCase {
    protected static final List<String> LIST_OF_INTEGRATION_TESTS_ALGORITHMS_PIDS = Arrays.asList(
            "org.cishell.tests.algorithm.StandardAlgorithm",
            "org.cishell.tests.algorithm.LossyConverterAlgorithm",
            "org.cishell.tests.algorithm.LosslessConverterAlgorithm",
            "org.cishell.tests.algorithm.ValidatorAlgorithm",
            "org.cishell.tests.algorithm.A2BConverterAlgorithm",
            "org.cishell.tests.algorithm.B2CConverterAlgorithm",
            "org.cishell.tests.algorithm.UserInputAlgorithm",
            "org.cishell.tests.algorithm.WaitForUserInputAlgorithm",
            "org.cishell.tests.algorithm.ErringAlgorithm",
            "org.cishell.tests.algorithm.QuickAlgorithm"
    );

    protected CIShellCIBridgeAlgorithmFacade algorithmFacade = getCIShellCIBridge().cishellAlgorithm;
    protected CIShellCIBridgeDataFacade dataFacade = getCIShellCIBridge().cishellData;
    protected CIShellCIBridgeNotificationFacade notificationFacade = getCIShellCIBridge().cishellNotification;
    protected CIBridgeGUIBuilderService guiBuilderService = (CIBridgeGUIBuilderService) getCIShellCIBridge().getGUIBuilderService();
    protected CIShellCIBridgeSchedulerFacade schedulerFacade = getCIShellCIBridge().cishellScheduler;
    protected CIShellCIBridgeLoggingFacade loggingFacade = getCIShellCIBridge().cishellLogging;

    @Before
    public void waitForAllAlgorithmDefinitionsToBeCached() {
        for (String algorithmPID : LIST_OF_INTEGRATION_TESTS_ALGORITHMS_PIDS) {
            boolean success = waitTillSatisfied(algorithmPID, pid -> getCIShellCIBridge().cishellAlgorithm.getAlgorithmDefinitionMap().containsKey(pid));
            assertTrue("Algorithm factory for pid: '" + algorithmPID + "' was not cached", success);
        }
    }
}
