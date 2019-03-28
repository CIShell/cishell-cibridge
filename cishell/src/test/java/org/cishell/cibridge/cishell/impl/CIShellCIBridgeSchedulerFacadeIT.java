package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.cishell.cibridge.core.model.AlgorithmState.*;
import static org.junit.Assert.*;

public class CIShellCIBridgeSchedulerFacadeIT extends IntegrationTestCase {

    private CIShellCIBridgeSchedulerFacade cishellCIBridgeSchedulerFacade = getCIShellCIBridge().cishellScheduler;
    private CIShellCIBridgeAlgorithmFacade cishellCIBridgeAlgorithmFacade = getCIShellCIBridge().cishellAlgorithm;

    private static AlgorithmInstance finishedAlgorithmInstance = null;
    private static AlgorithmInstance cancelledAlgorithmInstance = null;

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
        /* cancel running algorithm */
        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertSame(RUNNING, runningAI.getState());
        cishellCIBridgeSchedulerFacade.setAlgorithmCancelled(runningAI.getId(), true);
        assertSame(CANCELLED, runningAI.getState());

        /* cancel paused algorithm */
        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertSame(PAUSED, pausedAI.getState());
        cishellCIBridgeSchedulerFacade.setAlgorithmCancelled(pausedAI.getId(), true);
        assertSame(CANCELLED, pausedAI.getState());

        /* cancel algorithm waiting for user input */
        //todo cancel algorithm waiting for user input

    }

    @Test
    public void setAlgorithmPaused() {

        /* pause and unpause IDLE algorithm */
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(idleAI.getId(), true));
        assertSame(IDLE, idleAI.getState());
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(idleAI.getId(), false));
        assertSame(IDLE, idleAI.getState());

        /* pause and unpause RUNNING algorithm */
        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(runningAI.getId(), false));
        assertSame(RUNNING, runningAI.getState());
        assertTrue(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(runningAI.getId(), true));
        assertSame(PAUSED, runningAI.getState());
        cancel(runningAI);

        /* pause and unpause PAUSED algorithm */
        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(pausedAI.getId(), true));
        assertSame(PAUSED, pausedAI.getState());
        assertTrue(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(pausedAI.getId(), false));
        assertSame(RUNNING, pausedAI.getState());
        cancel(pausedAI);

        /* pause and unpause FINISHED algorithm */
        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(finishedAI.getId(), true));
        assertSame(FINISHED, finishedAI.getState());
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(finishedAI.getId(), false));
        assertSame(FINISHED, finishedAI.getState());

        /* pause and unpause SCHEDULED algorithm */
        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(scheduledAI.getId(), true));
        assertSame(SCHEDULED, scheduledAI.getState());
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(scheduledAI.getId(), false));
        assertSame(SCHEDULED, scheduledAI.getState());

        /* pause and unpause CANCELLED algorithm */
        AlgorithmInstance cancelledAI = getCancelledAlgorithm();
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(cancelledAI.getId(), true));
        assertSame(CANCELLED, cancelledAI.getState());
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(cancelledAI.getId(), false));
        assertSame(CANCELLED, cancelledAI.getState());

        //todo cant pause algorithm waiting for user input
        //todo cant unpause algorithm waiting for user input

    }

    @Test
    public void removeAlgorithm() {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertTrue(cishellCIBridgeSchedulerFacade.removeAlgorithm(idleAI.getId()));
        assertFalse(cishellCIBridgeAlgorithmFacade.getAlgorithmInstanceMap().containsKey(idleAI.getId()));
        assertFalse(cishellCIBridgeAlgorithmFacade.getCIShellAlgorithmCIBridgeAlgorithmMap().containsKey(((CIShellCIBridgeAlgorithmInstance) idleAI).getAlgorithm()));
    }


    @Test
    public void runAlgorithmNow() {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusSeconds(5);
        assertTrue(cishellCIBridgeSchedulerFacade.runAlgorithmNow(idleAI.getId()));
        assertTrue(idleAI.getScheduledRunTime().compareTo(start) >= 0 &&
                idleAI.getScheduledRunTime().compareTo(end) < 0);
        assertTrue(waitTillSatisfied(idleAI, ai -> ai.getState() == RUNNING));
        cancel(idleAI);

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.runAlgorithmNow(runningAI.getId()));
        cancel(runningAI);

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.runAlgorithmNow(pausedAI.getId()));
        cancel(pausedAI);

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.runAlgorithmNow(finishedAI.getId()));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        start = ZonedDateTime.now();
        end = start.plusSeconds(5);
        assertTrue(cishellCIBridgeSchedulerFacade.runAlgorithmNow(scheduledAI.getId()));
        assertTrue(scheduledAI.getScheduledRunTime().compareTo(start) >= 0 &&
                scheduledAI.getScheduledRunTime().compareTo(end) < 0);
        assertTrue(waitTillSatisfied(scheduledAI, ai -> ai.getState() == RUNNING));
        cancel(scheduledAI);

        AlgorithmInstance cancelledAI = getCancelledAlgorithm();
        assertFalse(cishellCIBridgeSchedulerFacade.runAlgorithmNow(cancelledAI.getId()));
    }

    @Test
    public void scheduleAlgorithm() {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertTrue(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(idleAI.getId(), ZonedDateTime.now().plusDays(1)));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(runningAI.getId(), ZonedDateTime.now().plusDays(1)));
        cancel(runningAI);

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(pausedAI.getId(), ZonedDateTime.now().plusDays(1)));
        cancel(pausedAI);

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(finishedAI.getId(), ZonedDateTime.now().plusDays(1)));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertTrue(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(scheduledAI.getId(), ZonedDateTime.now().plusDays(1)));

        AlgorithmInstance cancelledAI = getCancelledAlgorithm();
        assertFalse(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(cancelledAI.getId(), ZonedDateTime.now().plusDays(1)));
    }

    @Test
    public void rescheduleAlgorithm() {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(idleAI.getId(), ZonedDateTime.now().plusDays(1)));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(runningAI.getId(), ZonedDateTime.now().plusDays(1)));
        cancel(runningAI);

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(pausedAI.getId(), ZonedDateTime.now().plusDays(1)));
        cancel(pausedAI);

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(finishedAI.getId(), ZonedDateTime.now().plusDays(1)));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
//todo        assertTrue(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(scheduledAI.getId(), ZonedDateTime.now().plusDays(2)));

        AlgorithmInstance cancelledAI = getCancelledAlgorithm();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(cancelledAI.getId(), ZonedDateTime.now().plusDays(1)));
    }

    @Test
    public void unscheduleAlgorithm() {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(idleAI.getId()));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(runningAI.getId()));
        cancel(runningAI);

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(pausedAI.getId()));
        cancel(pausedAI);

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(finishedAI.getId()));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertTrue(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(scheduledAI.getId()));

        AlgorithmInstance cancelledAI = getCancelledAlgorithm();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(cancelledAI.getId()));
    }

    @Test
    public void clearSchedule() {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();

        AlgorithmInstance cancelledAI = getCancelledAlgorithm();

        assertEquals(0, (int) cishellCIBridgeSchedulerFacade.clearScheduler());

        cancel(runningAI);
        cancel(pausedAI);
    }

    @Test
    public void setSchedulerRunning() {
        assertFalse(cishellCIBridgeSchedulerFacade.setSchedulerRunning(false));
        assertTrue(cishellCIBridgeSchedulerFacade.setSchedulerRunning(true));
        assertFalse(cishellCIBridgeSchedulerFacade.setSchedulerRunning(false));
        assertTrue(cishellCIBridgeSchedulerFacade.setSchedulerRunning(true));
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

    private AlgorithmInstance getIdleAlgorithmInstance() {
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";
        AlgorithmInstance algorithmInstance = cishellCIBridgeAlgorithmFacade.createAlgorithm(pid, null, null);
        assertSame(IDLE, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getRunningAlgorithmInstance() {
        AlgorithmInstance algorithmInstance = getIdleAlgorithmInstance();

        //set algorithm running
        cishellCIBridgeSchedulerFacade.runAlgorithmNow(algorithmInstance.getId());
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == RUNNING));
        assertSame(RUNNING, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getPausedAlgorithmInstance() {
        AlgorithmInstance algorithmInstance = getRunningAlgorithmInstance();
        cishellCIBridgeSchedulerFacade.setAlgorithmPaused(algorithmInstance.getId(), true);
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == PAUSED));
        assertSame(PAUSED, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getFinishedAlgorithmInstance() {
        if(finishedAlgorithmInstance == null){
            System.out.println("came here once for finished");
            finishedAlgorithmInstance = getIdleAlgorithmInstance();
            cishellCIBridgeSchedulerFacade.runAlgorithmNow(finishedAlgorithmInstance.getId());
            assertTrue(waitTillSatisfied(finishedAlgorithmInstance, ai -> ai.getState() == FINISHED));
            assertSame(FINISHED, finishedAlgorithmInstance.getState());
        }

        return finishedAlgorithmInstance;
    }

    private AlgorithmInstance getScheduledAlgorithmInstance() {
        AlgorithmInstance algorithmInstance = getIdleAlgorithmInstance();
        cishellCIBridgeSchedulerFacade.scheduleAlgorithm(algorithmInstance.getId(), ZonedDateTime.now().plusDays(1));
        assertSame(SCHEDULED, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getCancelledAlgorithm() {

        if(cancelledAlgorithmInstance == null){
            System.out.println("came here once for cancelled");
            cancelledAlgorithmInstance = getRunningAlgorithmInstance();
            cishellCIBridgeSchedulerFacade.setAlgorithmCancelled(cancelledAlgorithmInstance.getId(), true);
            assertTrue(waitTillSatisfied(cancelledAlgorithmInstance, ai -> ai.getState() == CANCELLED));
            assertSame(CANCELLED, cancelledAlgorithmInstance.getState());
        }

        return cancelledAlgorithmInstance;
    }

    //cancel algorithm instances after use to save CPU resources
    private void cancel(AlgorithmInstance algorithmInstance) {
        cishellCIBridgeSchedulerFacade.setAlgorithmCancelled(algorithmInstance.getId(), true);
    }
}
