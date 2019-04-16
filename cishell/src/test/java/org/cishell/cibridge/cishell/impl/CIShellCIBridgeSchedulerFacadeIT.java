package org.cishell.cibridge.cishell.impl;

import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeAlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.cishell.cibridge.core.model.AlgorithmState.*;
import static org.junit.Assert.*;

public class CIShellCIBridgeSchedulerFacadeIT extends CIShellCIBridgeBaseIT {

    private CIShellCIBridgeSchedulerFacade schedulerFacade = getCIShellCIBridge().cishellScheduler;
    private CIShellCIBridgeAlgorithmFacade algorithmFacade = getCIShellCIBridge().cishellAlgorithm;

    @Before
    public void setSchedulerRunning() {
        getCIShellCIBridge().getSchedulerService().setRunning(true);
    }

    @Test
    public void isSchedulerEmpty() {
        assertTrue(schedulerFacade.isSchedulerEmpty());
        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertFalse(schedulerFacade.isSchedulerEmpty());
    }

    @Test
    public void isSchedulerRunning() {
        getCIShellCIBridge().getSchedulerService().setRunning(false);
        assertFalse(schedulerFacade.isSchedulerRunning());
        getCIShellCIBridge().getSchedulerService().setRunning(true);
        assertTrue(schedulerFacade.isSchedulerRunning());
    }

    @Test
    public void getSchedulerQueueWaiting() throws InterruptedException {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();

        assertSame(2, schedulerFacade.getSchedulerQueueWaiting());
    }

    @Test
    public void setAlgorithmCancelled() throws InterruptedException {
        /* cancel running algorithm */
        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertSame(RUNNING, runningAI.getState());

        schedulerFacade.setAlgorithmCancelled(runningAI.getId(), true);
        Thread.sleep(1000);
        assertTrue(waitTillSatisfied(runningAI, ai -> ai.getState() == CANCELLED));
        assertSame(CANCELLED, runningAI.getState());

        /* cancel paused algorithm */
        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertSame(PAUSED, pausedAI.getState());

        schedulerFacade.setAlgorithmCancelled(pausedAI.getId(), true);
        Thread.sleep(1000);
        assertTrue(waitTillSatisfied(runningAI, ai -> ai.getState() == CANCELLED));
        assertSame(CANCELLED, pausedAI.getState());

    }

    @Test
    public void setAlgorithmPaused() throws InterruptedException {

        /* pause and unpause IDLE algorithm */
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertFalse(schedulerFacade.setAlgorithmPaused(idleAI.getId(), true));
        assertSame(IDLE, idleAI.getState());
        assertFalse(schedulerFacade.setAlgorithmPaused(idleAI.getId(), false));
        assertSame(IDLE, idleAI.getState());

        /* pause and unpause RUNNING algorithm */
        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(schedulerFacade.setAlgorithmPaused(runningAI.getId(), false));
        assertSame(RUNNING, runningAI.getState());
        assertTrue(schedulerFacade.setAlgorithmPaused(runningAI.getId(), true));
        assertSame(PAUSED, runningAI.getState());

        /* pause and unpause PAUSED algorithm */
        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(schedulerFacade.setAlgorithmPaused(pausedAI.getId(), true));
        assertSame(PAUSED, pausedAI.getState());
        assertTrue(schedulerFacade.setAlgorithmPaused(pausedAI.getId(), false));
        assertSame(RUNNING, pausedAI.getState());

        /* pause and unpause FINISHED algorithm */
        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(schedulerFacade.setAlgorithmPaused(finishedAI.getId(), true));
        assertSame(FINISHED, finishedAI.getState());
        assertFalse(schedulerFacade.setAlgorithmPaused(finishedAI.getId(), false));
        assertSame(FINISHED, finishedAI.getState());

        /* pause and unpause SCHEDULED algorithm */
        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertFalse(schedulerFacade.setAlgorithmPaused(scheduledAI.getId(), true));
        assertSame(SCHEDULED, scheduledAI.getState());
        assertFalse(schedulerFacade.setAlgorithmPaused(scheduledAI.getId(), false));
        assertSame(SCHEDULED, scheduledAI.getState());

        /* pause and unpause CANCELLED algorithm */
        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(schedulerFacade.setAlgorithmPaused(cancelledAI.getId(), true));
        assertSame(CANCELLED, cancelledAI.getState());
        assertFalse(schedulerFacade.setAlgorithmPaused(cancelledAI.getId(), false));
        assertSame(CANCELLED, cancelledAI.getState());

        /* pause and unpause ERRORED algorithm */
        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(schedulerFacade.setAlgorithmPaused(erroredAI.getId(), true));
        assertSame(ERRORED, erroredAI.getState());
        assertFalse(schedulerFacade.setAlgorithmPaused(erroredAI.getId(), false));
        assertSame(ERRORED, erroredAI.getState());

    }

    @Test
    public void removeAlgorithm() {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertTrue(schedulerFacade.removeAlgorithm(idleAI.getId()));
        assertFalse(algorithmFacade.getAlgorithmInstanceMap().containsKey(idleAI.getId()));
        assertFalse(algorithmFacade.getCIShellAlgorithmCIBridgeAlgorithmMap().containsKey(((CIShellCIBridgeAlgorithmInstance) idleAI).getAlgorithm()));
    }


    @Test
    public void runAlgorithmNow() throws InterruptedException {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusSeconds(5);
        assertTrue(schedulerFacade.runAlgorithmNow(idleAI.getId()));
        assertTrue(idleAI.getScheduledRunTime().compareTo(start) >= 0 &&
                idleAI.getScheduledRunTime().compareTo(end) < 0);
        assertTrue(waitTillSatisfied(idleAI, ai -> ai.getState() == RUNNING));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(schedulerFacade.runAlgorithmNow(runningAI.getId()));

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(schedulerFacade.runAlgorithmNow(pausedAI.getId()));

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(schedulerFacade.runAlgorithmNow(finishedAI.getId()));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        start = ZonedDateTime.now();
        end = start.plusSeconds(5);
        assertTrue(schedulerFacade.runAlgorithmNow(scheduledAI.getId()));
        assertTrue(scheduledAI.getScheduledRunTime().compareTo(start) >= 0 &&
                scheduledAI.getScheduledRunTime().compareTo(end) < 0);
        assertTrue(waitTillSatisfied(scheduledAI, ai -> ai.getState() == RUNNING));

        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(schedulerFacade.runAlgorithmNow(cancelledAI.getId()));

        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(schedulerFacade.runAlgorithmNow(erroredAI.getId()));
    }

    @Test
    public void scheduleAlgorithm() throws InterruptedException {
        ZonedDateTime tomorrow = ZonedDateTime.now().plusDays(1);

        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertTrue(schedulerFacade.scheduleAlgorithm(idleAI.getId(), tomorrow));
        assertSame(0, tomorrow.compareTo(idleAI.getScheduledRunTime()));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(schedulerFacade.scheduleAlgorithm(runningAI.getId(), tomorrow));

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(schedulerFacade.scheduleAlgorithm(pausedAI.getId(), tomorrow));

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(schedulerFacade.scheduleAlgorithm(finishedAI.getId(), tomorrow));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertTrue(schedulerFacade.scheduleAlgorithm(scheduledAI.getId(), tomorrow));
        assertSame(0, tomorrow.compareTo(scheduledAI.getScheduledRunTime()));

        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(schedulerFacade.scheduleAlgorithm(cancelledAI.getId(), tomorrow));

        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(schedulerFacade.scheduleAlgorithm(erroredAI.getId(), tomorrow));
    }

    @Test
    public void rescheduleAlgorithm() throws InterruptedException {
        ZonedDateTime tomorrow = ZonedDateTime.now().plusDays(1);

        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertFalse(schedulerFacade.rescheduleAlgorithm(idleAI.getId(), tomorrow));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(schedulerFacade.rescheduleAlgorithm(runningAI.getId(), tomorrow));

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(schedulerFacade.rescheduleAlgorithm(pausedAI.getId(), tomorrow));

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(schedulerFacade.rescheduleAlgorithm(finishedAI.getId(), tomorrow));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertTrue(schedulerFacade.rescheduleAlgorithm(scheduledAI.getId(), tomorrow));

        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(schedulerFacade.rescheduleAlgorithm(cancelledAI.getId(), tomorrow));

        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(schedulerFacade.rescheduleAlgorithm(erroredAI.getId(), tomorrow));
    }

    @Test
    public void unscheduleAlgorithm() throws InterruptedException {
        AlgorithmInstance idleAI = getIdleAlgorithmInstance();
        assertFalse(schedulerFacade.unscheduleAlgorithm(idleAI.getId()));

        AlgorithmInstance runningAI = getRunningAlgorithmInstance();
        assertFalse(schedulerFacade.unscheduleAlgorithm(runningAI.getId()));

        AlgorithmInstance pausedAI = getPausedAlgorithmInstance();
        assertFalse(schedulerFacade.unscheduleAlgorithm(pausedAI.getId()));

        AlgorithmInstance finishedAI = getFinishedAlgorithmInstance();
        assertFalse(schedulerFacade.unscheduleAlgorithm(finishedAI.getId()));

        AlgorithmInstance scheduledAI = getScheduledAlgorithmInstance();
        assertTrue(schedulerFacade.unscheduleAlgorithm(scheduledAI.getId()));

        AlgorithmInstance cancelledAI = getCancelledAlgorithmInstance();
        assertFalse(schedulerFacade.unscheduleAlgorithm(cancelledAI.getId()));

        AlgorithmInstance erroredAI = getErroredAlgorithmInstance();
        assertFalse(schedulerFacade.unscheduleAlgorithm(erroredAI.getId()));
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

        assertEquals(0, schedulerFacade.clearScheduler().intValue());
        assertFalse(algorithmFacade.getAlgorithmInstanceMap().containsKey(idleAI.getId()));
        assertFalse(algorithmFacade.getAlgorithmInstanceMap().containsKey(scheduledAI.getId()));
        assertFalse(algorithmFacade.getAlgorithmInstanceMap().containsKey(finishedAI.getId()));
        assertFalse(algorithmFacade.getAlgorithmInstanceMap().containsKey(cancelledAI.getId()));
        assertFalse(algorithmFacade.getAlgorithmInstanceMap().containsKey(erroredAI.getId()));
    }

    @Test
    public void runAndPauseSchedulerService() {
        assertFalse(schedulerFacade.setSchedulerRunning(false));
        assertTrue(schedulerFacade.setSchedulerRunning(true));
        assertFalse(schedulerFacade.setSchedulerRunning(false));
        assertTrue(schedulerFacade.setSchedulerRunning(true));
    }

    @Test
    public void validateSchedulerClearedSubscription() {
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        schedulerFacade.schedulerCleared().subscribe(testSubscriber);

        schedulerFacade.clearScheduler();

        testSubscriber.awaitCount(1);
        assertTrue(testSubscriber.values().get(0));
    }

    @Test
    public void validateSchedulerStateChangedSubscription() {
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        schedulerFacade.schedulerRunningChanged().subscribe(testSubscriber);

        schedulerFacade.setSchedulerRunning(false);
        testSubscriber.awaitCount(1);
        assertFalse(testSubscriber.values().get(0));

        schedulerFacade.setSchedulerRunning(true);
        testSubscriber.awaitCount(2);
        assertTrue(testSubscriber.values().get(1));
    }

    @Test
    public void validateAlgorithmInstanceUpdatedSubscription() {
        AlgorithmFilter algorithmFilter = new AlgorithmFilter();
        algorithmFilter.setAlgorithmInstanceIds(Arrays.asList("org.cishell.tests.algorithm.StandardAlgorithm"));
        TestSubscriber<AlgorithmInstance> testSubscriber = new TestSubscriber<>();
        algorithmFacade.algorithmInstanceUpdated(algorithmFilter).subscribe(testSubscriber);

        schedulerFacade.runAlgorithmNow(getRunningAlgorithmInstance().getId());
        testSubscriber.awaitCount(2);

        AlgorithmInstance actualAlgoInstance = testSubscriber.values().get(0);
        assertTrue(RUNNING == actualAlgoInstance.getState() || FINISHED == actualAlgoInstance.getState());

    }

    @After
    public void tearDown() {
        for (CIShellCIBridgeAlgorithmInstance algorithmInstance : algorithmFacade.getAlgorithmInstanceMap().values()) {
            if (algorithmInstance.getState() == RUNNING || algorithmInstance.getState() == PAUSED) {
                //cancel running and paused algorithm instances after use to save CPU resources
                schedulerFacade.setAlgorithmCancelled(algorithmInstance.getId(), true);
            }
        }

        schedulerFacade.clearScheduler();
        assertTrue(schedulerFacade.isSchedulerEmpty());
    }

    private AlgorithmInstance getIdleAlgorithmInstance() {
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) algorithmFacade.createAlgorithm(pid, null, null);
        assertSame(IDLE, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getRunningAlgorithmInstance() {
        AlgorithmInstance algorithmInstance = getIdleAlgorithmInstance();
        //set algorithm running
        schedulerFacade.runAlgorithmNow(algorithmInstance.getId());
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == RUNNING));
        return algorithmInstance;
    }

    private AlgorithmInstance getPausedAlgorithmInstance() throws InterruptedException {
        AlgorithmInstance algorithmInstance = getRunningAlgorithmInstance();
        schedulerFacade.setAlgorithmPaused(algorithmInstance.getId(), true);
        //let algorithm pause in cishell
        Thread.sleep(5 * TIME_QUANTUM);
        //check the progress is halted
        int progressBefore = algorithmInstance.getProgress();
        Thread.sleep(5 * TIME_QUANTUM);
        assertSame(progressBefore, algorithmInstance.getProgress());
        //check proper state
        assertSame(PAUSED, algorithmInstance.getState());
        return algorithmInstance;
    }

    private AlgorithmInstance getScheduledAlgorithmInstance() {
        AlgorithmInstance algorithmInstance = getIdleAlgorithmInstance();
        schedulerFacade.scheduleAlgorithm(algorithmInstance.getId(), ZonedDateTime.now().plusDays(1));
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == SCHEDULED));
        return algorithmInstance;
    }

    private AlgorithmInstance getFinishedAlgorithmInstance() {
        //use another algorithm which quickly finishes
        String pid = "org.cishell.tests.algorithm.QuickAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) algorithmFacade.createAlgorithm(pid, null, null);
        schedulerFacade.runAlgorithmNow(algorithmInstance.getId());
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == FINISHED));
        return algorithmInstance;
    }

    private AlgorithmInstance getCancelledAlgorithmInstance() {
        AlgorithmInstance algorithmInstance = getRunningAlgorithmInstance();
        schedulerFacade.setAlgorithmCancelled(algorithmInstance.getId(), true);
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == CANCELLED));
        return algorithmInstance;
    }

    private AlgorithmInstance getErroredAlgorithmInstance() {
        //use another algorithm which throws error
        String pid = "org.cishell.tests.algorithm.ErringAlgorithm";
        CIShellCIBridgeAlgorithmInstance algorithmInstance = (CIShellCIBridgeAlgorithmInstance) algorithmFacade.createAlgorithm(pid, null, null);
        schedulerFacade.runAlgorithmNow(algorithmInstance.getId());
        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == ERRORED));
        return algorithmInstance;
    }
}
