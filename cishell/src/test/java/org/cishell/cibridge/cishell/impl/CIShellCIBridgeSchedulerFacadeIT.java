package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeAlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.cishell.cibridge.core.model.AlgorithmState.*;
import static org.junit.Assert.*;

public class CIShellCIBridgeSchedulerFacadeIT extends IntegrationTestCase {

    private CIShellCIBridgeSchedulerFacade cishellCIBridgeSchedulerFacade = getCIShellCIBridge().cishellScheduler;
    private CIShellCIBridgeAlgorithmFacade cishellCIBridgeAlgorithmFacade = getCIShellCIBridge().cishellAlgorithm;

    @Before
    public void setup() {
        getCIShellCIBridge().getSchedulerService().setRunning(true);
    }

    @Test
    public void isSchedulerEmpty() {
        assertTrue(cishellCIBridgeSchedulerFacade.isSchedulerEmpty());
        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
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
    public void setAlgorithmCancelled() throws InterruptedException {
        /* cancel running algorithm */
        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertSame(RUNNING, runningAI.getState());
        cishellCIBridgeSchedulerFacade.setAlgorithmCancelled(runningAI.getId(), true);
        Thread.sleep(1000);
        assertTrue(waitTillSatisfied(runningAI, ai -> ai.getState() == CANCELLED));
        assertSame(CANCELLED, runningAI.getState());

        /* cancel paused algorithm */
        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertSame(PAUSED, pausedAI.getState());
        cishellCIBridgeSchedulerFacade.setAlgorithmCancelled(pausedAI.getId(), true);
        Thread.sleep(1000);
        assertTrue(waitTillSatisfied(runningAI, ai -> ai.getState() == CANCELLED));
        assertSame(CANCELLED, pausedAI.getState());

        /* cancel algorithm waiting for user input */
        //todo cancel algorithm waiting for user input

    }

    @Test
    public void setAlgorithmPaused() throws InterruptedException {

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

        /* pause and unpause PAUSED algorithm */
        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(pausedAI.getId(), true));
        assertSame(PAUSED, pausedAI.getState());
        assertTrue(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(pausedAI.getId(), false));
        assertSame(RUNNING, pausedAI.getState());

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
        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(cancelledAI.getId(), true));
        assertSame(CANCELLED, cancelledAI.getState());
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(cancelledAI.getId(), false));
        assertSame(CANCELLED, cancelledAI.getState());

        /* pause and unpause ERRORED algorithm */
        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(erroredAI.getId(), true));
        assertSame(ERRORED, erroredAI.getState());
        assertFalse(cishellCIBridgeSchedulerFacade.setAlgorithmPaused(erroredAI.getId(), false));
        assertSame(ERRORED, erroredAI.getState());

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
    public void runAlgorithmNow() throws InterruptedException {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusSeconds(5);
        assertTrue(cishellCIBridgeSchedulerFacade.runAlgorithmNow(idleAI.getId()));
        assertTrue(idleAI.getScheduledRunTime().compareTo(start) >= 0 &&
                idleAI.getScheduledRunTime().compareTo(end) < 0);
        assertTrue(waitTillSatisfied(idleAI, ai -> ai.getState() == RUNNING));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.runAlgorithmNow(runningAI.getId()));

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.runAlgorithmNow(pausedAI.getId()));

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.runAlgorithmNow(finishedAI.getId()));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        start = ZonedDateTime.now();
        end = start.plusSeconds(5);
        assertTrue(cishellCIBridgeSchedulerFacade.runAlgorithmNow(scheduledAI.getId()));
        assertTrue(scheduledAI.getScheduledRunTime().compareTo(start) >= 0 &&
                scheduledAI.getScheduledRunTime().compareTo(end) < 0);
        assertTrue(waitTillSatisfied(scheduledAI, ai -> ai.getState() == RUNNING));

        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.runAlgorithmNow(cancelledAI.getId()));

        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.runAlgorithmNow(erroredAI.getId()));
    }

    @Test
    public void scheduleAlgorithm() throws InterruptedException {
        ZonedDateTime tomorrow = ZonedDateTime.now().plusDays(1);

        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertTrue(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(idleAI.getId(), tomorrow));
        assertSame(0, tomorrow.compareTo(idleAI.getScheduledRunTime()));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(runningAI.getId(), tomorrow));

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(pausedAI.getId(), tomorrow));

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(finishedAI.getId(), tomorrow));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertTrue(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(scheduledAI.getId(), tomorrow));
        assertSame(0, tomorrow.compareTo(scheduledAI.getScheduledRunTime()));

        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(cancelledAI.getId(), tomorrow));

        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.scheduleAlgorithm(erroredAI.getId(), tomorrow));
    }

    @Test
    public void rescheduleAlgorithm() throws InterruptedException {
        ZonedDateTime tomorrow = ZonedDateTime.now().plusDays(1);

        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(idleAI.getId(), tomorrow));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(runningAI.getId(), tomorrow));

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(pausedAI.getId(), tomorrow));

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(finishedAI.getId(), tomorrow));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertTrue(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(scheduledAI.getId(), tomorrow));

        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(cancelledAI.getId(), tomorrow));

        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.rescheduleAlgorithm(erroredAI.getId(), tomorrow));
    }

    @Test
    public void unscheduleAlgorithm() throws InterruptedException {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(idleAI.getId()));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(runningAI.getId()));

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(pausedAI.getId()));

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(finishedAI.getId()));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertTrue(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(scheduledAI.getId()));

        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(cancelledAI.getId()));

        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(cishellCIBridgeSchedulerFacade.unscheduleAlgorithm(erroredAI.getId()));
    }

    @Test
    public void clearSchedule() throws InterruptedException {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();

        assertEquals(0, cishellCIBridgeSchedulerFacade.clearScheduler().intValue());
        assertFalse(cishellCIBridgeAlgorithmFacade.getAlgorithmInstanceMap().containsKey(idleAI.getId()));
        assertFalse(cishellCIBridgeAlgorithmFacade.getAlgorithmInstanceMap().containsKey(scheduledAI.getId()));
        assertFalse(cishellCIBridgeAlgorithmFacade.getAlgorithmInstanceMap().containsKey(finishedAI.getId()));
        assertFalse(cishellCIBridgeAlgorithmFacade.getAlgorithmInstanceMap().containsKey(cancelledAI.getId()));
        assertFalse(cishellCIBridgeAlgorithmFacade.getAlgorithmInstanceMap().containsKey(erroredAI.getId()));
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
        for (CIShellCIBridgeAlgorithmInstance algorithmInstance : cishellCIBridgeAlgorithmFacade.getAlgorithmInstanceMap().values()) {
            if (algorithmInstance.getState() == RUNNING || algorithmInstance.getState() == PAUSED) {
                //cancel running and paused algorithm instances after use to save CPU resources
                cishellCIBridgeSchedulerFacade.setAlgorithmCancelled(algorithmInstance.getId(), true);
            }
        }

        cishellCIBridgeSchedulerFacade.clearScheduler();
        assertTrue(cishellCIBridgeSchedulerFacade.isSchedulerEmpty());
    }

    private AlgorithmInstance getIdleAlgorithmInstance() {
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) cishellCIBridgeAlgorithmFacade.createAlgorithm(pid, null, null);
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

    private AlgorithmInstance getPausedAlgorithmInstance() throws InterruptedException {
        AlgorithmInstance algorithmInstance = getRunningAlgorithmInstance();
        cishellCIBridgeSchedulerFacade.setAlgorithmPaused(algorithmInstance.getId(), true);
        //let algorithm pause in cishell
        Thread.sleep(2 * TIME_QUANTUM);
        //check the progress is halted
        int progressBefore = algorithmInstance.getProgress();
        Thread.sleep(10 * TIME_QUANTUM);
        assertSame(progressBefore, algorithmInstance.getProgress());
        //check proper state
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == PAUSED));
        assertSame(PAUSED, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getScheduledAlgorithmInstance() {
        AlgorithmInstance algorithmInstance = getIdleAlgorithmInstance();
        cishellCIBridgeSchedulerFacade.scheduleAlgorithm(algorithmInstance.getId(), ZonedDateTime.now().plusDays(1));
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == SCHEDULED));
        assertSame(SCHEDULED, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getFinishedAlgorithmInstance() {
        //use another algorithm which quickly finishes
        String pid = "org.cishell.tests.algorithm.QuickAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) cishellCIBridgeAlgorithmFacade.createAlgorithm(pid, null, null);
        cishellCIBridgeSchedulerFacade.runAlgorithmNow(algorithmInstance.getId());
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == FINISHED));
        assertSame(FINISHED, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getCancelledAlgorithmInstance() {
        AlgorithmInstance algorithmInstance = getRunningAlgorithmInstance();
        cishellCIBridgeSchedulerFacade.setAlgorithmCancelled(algorithmInstance.getId(), true);
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == CANCELLED));
        assertSame(CANCELLED, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getErroredAlgorithmInstance() {
        //use another algorithm which throws error
        String pid = "org.cishell.tests.algorithm.ErringAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) cishellCIBridgeAlgorithmFacade.createAlgorithm(pid, null, null);
        cishellCIBridgeSchedulerFacade.runAlgorithmNow(algorithmInstance.getId());
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == ERRORED));
        return algorithmInstance;
    }

    private AlgorithmInstance getWaitingAlgorithmInstance() {
        //use another algorithm which waits for user input
        String pid = "org.cishell.tests.algorithm.UserInputAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) cishellCIBridgeAlgorithmFacade.createAlgorithm(pid, null, null);
        cishellCIBridgeSchedulerFacade.runAlgorithmNow(algorithmInstance.getId());
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == WAITING));
        return algorithmInstance;
    }
}
