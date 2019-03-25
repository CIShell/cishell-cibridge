package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.cishell.cibridge.core.model.AlgorithmState.CANCELLED;
import static org.junit.Assert.*;

public class CIShellCIBridgeSchedulerFacadeIT extends IntegrationTestCase {

    CIShellCIBridgeSchedulerFacade cishellCIBridgeSchedulerFacade = getCIShellCIBridge().cishellScheduler;
    CIShellCIBridgeAlgorithmFacade cishellCIBridgeAlgorithmFacade = getCIShellCIBridge().cishellAlgorithm;

    @Before
    public void setup() {
        getCIShellCIBridge().getSchedulerService().setRunning(true);
    }

    @Test
    public void isSchedulerEmpty() {
        assertTrue(cishellCIBridgeSchedulerFacade.isSchedulerEmpty());
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";
        AlgorithmInstance algorithmInstance = cishellCIBridgeAlgorithmFacade.createAlgorithm(pid, null, null);
        cishellCIBridgeSchedulerFacade.scheduleAlgorithm(algorithmInstance.getId(), Instant.ofEpochMilli(Long.MAX_VALUE).atZone(ZoneOffset.UTC));
        assertFalse(cishellCIBridgeSchedulerFacade.isSchedulerEmpty());
    }

    @Test
    public void isSchedulerRunning() {
        getCIShellCIBridge().getSchedulerService().setRunning(false);
        assertFalse(cishellCIBridgeSchedulerFacade.isSchedulerRunning());
        getCIShellCIBridge().getSchedulerService().setRunning(true);
        assertTrue(cishellCIBridgeSchedulerFacade.isSchedulerRunning());
    }

    @Test
    public void getSchedulerQueueWaiting() {
        //todo how to simulate waiting for user input
    }

    @Test
    public void setAlgorithmCancelled() {
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";
        AlgorithmInstance algorithmInstance = cishellCIBridgeAlgorithmFacade.createAlgorithm(pid, null, null);
        //can only be cancelled after the algorithm has started based on the capability of progress monitor
        assertSame(algorithmInstance.getState(), CANCELLED);
    }

    @Test
    public void setAlgorithmPaused() {
        //can only be paused after the algorithm has started based on the capability of progress monitor
    }

    @Test
    public void runAlgorithmNow() {
        assertTrue(cishellCIBridgeSchedulerFacade.isSchedulerEmpty());
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";
        AlgorithmInstance algorithmInstance = cishellCIBridgeAlgorithmFacade.createAlgorithm(pid, null, null);
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusSeconds(5);
        cishellCIBridgeSchedulerFacade.runAlgorithmNow(algorithmInstance.getId());
        assertTrue(algorithmInstance.getScheduledRunTime().compareTo(start) >= 0 &&
                algorithmInstance.getScheduledRunTime().compareTo(end) < 0);
    }

    @Test
    public void reschedule() {
        //if not scheduled already, should fail
    }

    @Test
    public void unschedule() {
        //if already running, should fail
    }

    @Test
    public void clearSchedule() {
        //if already running, then should not be cleared
    }

    @After
    public void tearDown() {
        cishellCIBridgeSchedulerFacade.clearScheduler();
        assertTrue(cishellCIBridgeSchedulerFacade.isSchedulerEmpty());

        //todo remove algorithms from cibridge
//        for (Algorithm algorithm : cishellCIBridgeAlgorithmFacade.getCIShellAlgorithmCIBridgeAlgorithmMap().keySet()) {
//            System.out.println(algorithm);
//        }
//
//        for (AlgorithmInstance instance : cishellCIBridgeAlgorithmFacade.getAlgorithmInstanceMap().values()) {
//            System.out.println(instance);
//        }

    }
}
