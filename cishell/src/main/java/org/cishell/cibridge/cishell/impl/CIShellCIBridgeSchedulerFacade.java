package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.osgi.framework.ServiceReference;
import org.reactivestreams.Publisher;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.cishell.cibridge.core.model.AlgorithmState.*;

public class CIShellCIBridgeSchedulerFacade implements CIBridge.SchedulerFacade {
    private CIShellCIBridge cibridge;
    private SchedulerListenerImpl schedulerListener = new SchedulerListenerImpl();
    private ConnectableObservable<Boolean> schedulerClearedObservable;
    private ObservableEmitter<Boolean> schedulerClearedObservableEmitter;

    private ConnectableObservable<Boolean> schedulerRunningChangedObservable;
    private ObservableEmitter<Boolean> schedulerRunningChangedObservableEmitter;

    public void setCIBridge(CIShellCIBridge cibridge) {
        Preconditions.checkNotNull(cibridge, "cibridge cannot be null");
        this.cibridge = cibridge;
        this.schedulerListener.setCIBridge(cibridge);

        io.reactivex.Observable<Boolean> schedulerclearedobservable = Observable.create(emitter -> {
            schedulerClearedObservableEmitter = emitter;

        });
        schedulerClearedObservable = schedulerclearedobservable.share().publish();
        schedulerClearedObservable.connect();

        io.reactivex.Observable<Boolean> schedulerrunningchangedobservable = Observable.create(emitter -> {
            schedulerRunningChangedObservableEmitter = emitter;

        });
        schedulerRunningChangedObservable = schedulerrunningchangedobservable.share().publish();
        schedulerRunningChangedObservable.connect();
    }

    /* Queries */
    @Override
    public Boolean isSchedulerEmpty() {
        return cibridge.getSchedulerService().isEmpty();
    }

    @Override
    public Boolean isSchedulerRunning() {
        return cibridge.getSchedulerService().isRunning();
    }

    @Override
    public Integer getSchedulerQueueWaiting() {
        int count = 0;
        for (AlgorithmInstance algorithmInstance : cibridge.cishellAlgorithm.getAlgorithmInstanceMap().values()) {
            if (algorithmInstance.getState() == IDLE ||
                    algorithmInstance.getState() == PAUSED ||
                    algorithmInstance.getState() == WAITING) {
                count++;
            }
        }
        return count;
    }

    /* Mutations */

    @Override
    public Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean cancelled) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);

        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            progressTrackableAlgorithm.getProgressMonitor().setCanceled(cancelled);
            //return true if the operation succeeded
            System.out.println(algorithmInstance.getState().equals(CANCELLED) == cancelled);
            return algorithmInstance.getState().equals(CANCELLED) == cancelled;
        }

        return false;
    }


    @Override
    public Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean paused) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);

        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            progressTrackableAlgorithm.getProgressMonitor().setPaused(paused);
            //return true if the operation succeeded
            return algorithmInstance.getState().equals(PAUSED) == paused;
        }

        return false;
    }

    @Override
    public Boolean removeAlgorithm(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        Algorithm algorithm = algorithmInstance.getAlgorithm();

        //unschedule the algorithm
        cibridge.getSchedulerService().unschedule(algorithm);

        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            progressTrackableAlgorithm.setProgressMonitor(new ProgressMonitorImpl(cibridge, algorithmInstance));
            //also mark the algorithm canceled
            progressTrackableAlgorithm.getProgressMonitor().setCanceled(true);
        }

        //remove the algorithm from cibridge
        cibridge.cishellAlgorithm.getCIShellAlgorithmCIBridgeAlgorithmMap().remove(algorithm);
        cibridge.cishellAlgorithm.getAlgorithmInstanceMap().remove(algorithmInstanceId);

        return true;
    }

    @Override
    public Boolean runAlgorithmNow(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        Algorithm algorithm = algorithmInstance.getAlgorithm();

        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            progressTrackableAlgorithm.setProgressMonitor(new ProgressMonitorImpl(cibridge, algorithmInstance));
        }

        cibridge.getSchedulerService().runNow(algorithm, getServiceReference(algorithmInstanceId));
        return true;
    }

    @Override
    public Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        Algorithm algorithm = algorithmInstance.getAlgorithm();

        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            progressTrackableAlgorithm.setProgressMonitor(new ProgressMonitorImpl(cibridge, algorithmInstance));
        }

        cibridge.getSchedulerService().schedule(algorithm, getServiceReference(algorithmInstanceId), GregorianCalendar.from(date));
        algorithmInstance.setScheduledRunTime(date);
        algorithmInstance.setState(SCHEDULED);
        return true;
    }

    @Override
    public Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        Algorithm algorithm = algorithmInstance.getAlgorithm();

        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            progressTrackableAlgorithm.setProgressMonitor(new ProgressMonitorImpl(cibridge, algorithmInstance));
        }

        boolean isAlreadyScheduled = cibridge.getSchedulerService().reschedule(algorithm, GregorianCalendar.from(date));
        if (!isAlreadyScheduled) {
            return false;
        }
        algorithmInstance.setScheduledRunTime(date);
        algorithmInstance.setState(SCHEDULED);
        return true;
    }

    @Override
    public Boolean unscheduleAlgorithm(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        Algorithm algorithm = algorithmInstance.getAlgorithm();

        boolean unscheduled = cibridge.getSchedulerService().unschedule(algorithm);
        if (!unscheduled) {
            return false;
        }

        algorithmInstance.setState(IDLE);
        return true;
    }

    @Override
    public Integer clearScheduler() {
        cibridge.getSchedulerService().clearSchedule();
        schedulerClearedObservableEmitter.onNext(false);
        return cibridge.getSchedulerService().getScheduledAlgorithms().length;
    }

    @Override
    public Boolean setSchedulerRunning(Boolean running) {
        cibridge.getSchedulerService().setRunning(running);
        schedulerRunningChangedObservableEmitter.onNext(running);
        return cibridge.getSchedulerService().isRunning();
    }

    @Override
    public Publisher<Boolean> schedulerCleared() {
        Flowable<Boolean> publisher;
        ConnectableObservable<Boolean> connectableObservable = schedulerClearedObservable;
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
        return publisher;
    }

    @Override
    public Publisher<Boolean> schedulerRunningChanged() {
        Flowable<Boolean> publisher;
        ConnectableObservable<Boolean> connectableObservable = schedulerRunningChangedObservable;
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
        return publisher;
    }

    private CIShellCIBridgeAlgorithmInstance getAlgorithmInstance(String algorithmInstanceId) {
        Preconditions.checkArgument(cibridge.cishellAlgorithm.getAlgorithmInstanceMap().containsKey(algorithmInstanceId), "No algorithm found with id '%s'", algorithmInstanceId);
        return cibridge.cishellAlgorithm.getAlgorithmInstanceMap().get(algorithmInstanceId);
    }

    private ServiceReference getServiceReference(String algorithmInstanceId) {
        return cibridge.getSchedulerService().getServiceReference(getAlgorithmInstance(algorithmInstanceId).getAlgorithm());
    }

    private boolean setProgressMonitorImpl(CIShellCIBridgeAlgorithmInstance algorithmInstance) {
        //if ProgressTrackable, supply cibridge implementation of progress monitor
        if (algorithmInstance.getAlgorithm() instanceof ProgressTrackable) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            if (!(progressTrackableAlgorithm.getProgressMonitor() instanceof ProgressMonitorImpl)) {
                progressTrackableAlgorithm.setProgressMonitor(new ProgressMonitorImpl(cibridge, algorithmInstance));
            }
            return true;
        } else {
            return false;
        }
    }
}
