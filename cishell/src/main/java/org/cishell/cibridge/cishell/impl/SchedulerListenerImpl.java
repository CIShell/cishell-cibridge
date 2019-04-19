package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.app.service.scheduler.SchedulerListener;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeData;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;

import java.time.ZoneId;
import java.util.Calendar;

import static org.cishell.cibridge.core.model.AlgorithmState.*;

public class SchedulerListenerImpl implements SchedulerListener {

    private CIShellCIBridge cibridge;

    public void setCIBridge(CIShellCIBridge cibridge) {
        Preconditions.checkNotNull(cibridge, "cibridge cannot be null");
        this.cibridge = cibridge;
        this.cibridge.getSchedulerService().addSchedulerListener(this);
    }

    @Override
    public void algorithmScheduled(Algorithm algorithm, Calendar calendar) {
        AlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithm);
        algorithmInstance.setState(SCHEDULED);
        algorithmInstance.setScheduledRunTime(calendar.toInstant().atZone(ZoneId.systemDefault()));
        cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(getAlgorithmInstance(algorithm));
    }

    @Override
    public void algorithmRescheduled(Algorithm algorithm, Calendar calendar) {
        algorithmScheduled(algorithm, calendar);
        cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(getAlgorithmInstance(algorithm));
    }

    @Override
    public void algorithmUnscheduled(Algorithm algorithm) {
        getAlgorithmInstance(algorithm).setState(IDLE);
        cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(getAlgorithmInstance(algorithm));
    }

    @Override
    public void algorithmStarted(Algorithm algorithm) {
        getAlgorithmInstance(algorithm).setState(RUNNING);
        cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(getAlgorithmInstance(algorithm));
    }

    @Override
    public void algorithmFinished(Algorithm algorithm, Data[] data) {
        AlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithm);
        if (algorithmInstance.getState() == ERRORED) {
            return;
        }
        if (algorithm instanceof ProgressTrackable) {
            if (((ProgressTrackable) algorithm).getProgressMonitor().isCanceled()) {
                getAlgorithmInstance(algorithm).setState(CANCELLED);
                return;
            }
        }
        getAlgorithmInstance(algorithm).setState(FINISHED);
        cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(getAlgorithmInstance(algorithm));

        if (data != null) {
            for (Data datum : data) {
                //todo set some label here. Its not being set by cishell
                cibridge.cishellData.getDataManagerListener().dataAdded(datum, null);
                CIShellCIBridgeData cishellCIBridgeDatum = cibridge.cishellData.getCIShellDataCIBridgeDataMap().get(datum);
                algorithmInstance.getOutData().add(cishellCIBridgeDatum);

            }
        }
    }

    @Override
    public void algorithmError(Algorithm algorithm, Throwable throwable) {
        getAlgorithmInstance(algorithm).setState(ERRORED);
        cibridge.cishellAlgorithm.getAlgorithmInstanceUpdatedObservableEmitter().onNext(getAlgorithmInstance(algorithm));
        throwable.printStackTrace();
    }

    @Override
    public void schedulerRunStateChanged(boolean b) {
        cibridge.cishellScheduler.getSchedulerRunningChangedObservableEmitter().onNext(b);
    }

    @Override
    public void schedulerCleared() {
        cibridge.cishellScheduler.getSchedulerClearedObservableEmitter().onNext(true);
    }

    private AlgorithmInstance getAlgorithmInstance(Algorithm algorithm) {
        return cibridge.cishellAlgorithm.getCIShellAlgorithmCIBridgeAlgorithmMap().get(algorithm);
    }

}
