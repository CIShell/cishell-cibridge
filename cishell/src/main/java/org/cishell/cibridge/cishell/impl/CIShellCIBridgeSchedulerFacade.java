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
import java.util.Iterator;
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
            switch (algorithmInstance.getState()) {
                case IDLE:
                case PAUSED:
                case WAITING:
                    ++count;
            }
        }

        return count;
    }

    /* Mutations */

    @Override
    public Boolean setAlgorithmCancelled(String algorithmInstanceId, Boolean cancel) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);

        if (!(algorithmInstance.getState() == RUNNING ||
                algorithmInstance.getState() == PAUSED)) {
            return false;
        }

        if (setProgressMonitorImpl(algorithmInstance)) {
            if (cancel) {
                ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
                progressTrackableAlgorithm.getProgressMonitor().setCanceled(true);
                if (algorithmInstance.getState() == PAUSED) {
                    progressTrackableAlgorithm.getProgressMonitor().setPaused(false);
                }
                return true;
            }
        }

        return false;
    }


    @Override
    public Boolean setAlgorithmPaused(String algorithmInstanceId, Boolean pause) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);

        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            if (pause && algorithmInstance.getState() == RUNNING) {
                progressTrackableAlgorithm.getProgressMonitor().setPaused(true);
                return algorithmInstance.getState().equals(PAUSED);
            } else if (!pause && algorithmInstance.getState() == PAUSED) {
                progressTrackableAlgorithm.getProgressMonitor().setPaused(false);
                return algorithmInstance.getState().equals(RUNNING);
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public Boolean removeAlgorithm(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        Algorithm algorithm = algorithmInstance.getAlgorithm();

        //unschedule the algorithm
        cibridge.getSchedulerService().unschedule(algorithm);

        //set cancelled
        if (setProgressMonitorImpl(algorithmInstance)) {
            ProgressTrackable progressTrackableAlgorithm = (ProgressTrackable) algorithmInstance.getAlgorithm();
            progressTrackableAlgorithm.getProgressMonitor().setCanceled(true);
        }

        //remove the algorithm from cibridge cache
        cibridge.cishellAlgorithm.getCIShellAlgorithmCIBridgeAlgorithmMap().remove(algorithm);
        cibridge.cishellAlgorithm.getAlgorithmInstanceMap().remove(algorithmInstanceId);

        return true;
    }

    @Override
    public Boolean runAlgorithmNow(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        setProgressMonitorImpl(algorithmInstance);

        if (algorithmInstance.getState() == SCHEDULED) {
            unscheduleAlgorithm(algorithmInstanceId);
        } else if (algorithmInstance.getState() != IDLE) {
            return false;
        }

        cibridge.getSchedulerService().runNow(algorithmInstance.getAlgorithm(), getServiceReference(algorithmInstanceId));
        return true;
    }

    @Override
    public Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        setProgressMonitorImpl(algorithmInstance);

        if (algorithmInstance.getState() == IDLE) {
            cibridge.getSchedulerService().schedule(algorithmInstance.getAlgorithm(), getServiceReference(algorithmInstanceId), GregorianCalendar.from(date));
        } else if (algorithmInstance.getState() == SCHEDULED) {
            rescheduleAlgorithm(algorithmInstanceId, date);
        } else {
            return false;
        }

        return true;
    }

    @Override
    public Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        setProgressMonitorImpl(algorithmInstance);

        if (algorithmInstance.getState() != SCHEDULED) {
            return false;
        }

        cibridge.getSchedulerService().unschedule(algorithmInstance.getAlgorithm());
        cibridge.getSchedulerService().schedule(algorithmInstance.getAlgorithm(), getServiceReference(algorithmInstanceId), GregorianCalendar.from(date));

        return true;
    }

    @Override
    public Boolean unscheduleAlgorithm(String algorithmInstanceId) {
        CIShellCIBridgeAlgorithmInstance algorithmInstance = getAlgorithmInstance(algorithmInstanceId);
        Algorithm algorithm = algorithmInstance.getAlgorithm();

        if (algorithmInstance.getState() != SCHEDULED) {
            return false;
        }

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
        Iterator<String> iterator = cibridge.cishellAlgorithm.getAlgorithmInstanceMap().keySet().iterator();
        while (iterator.hasNext()) {
            String algorithmInstanceId = iterator.next();
            CIShellCIBridgeAlgorithmInstance algorithmInstance = cibridge.cishellAlgorithm.getAlgorithmInstanceMap().get(algorithmInstanceId);

            if (!(algorithmInstance.getState() == RUNNING || algorithmInstance.getState() == PAUSED || algorithmInstance.getState() == WAITING)) {
                cibridge.cishellAlgorithm.getCIShellAlgorithmCIBridgeAlgorithmMap().remove(algorithmInstance.getAlgorithm());
                iterator.remove();
            }
        }
        return cibridge.getSchedulerService().getScheduledAlgorithms().length;
    }

    @Override
    public Boolean setSchedulerRunning(Boolean running) {
        cibridge.getSchedulerService().setRunning(running);
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

    private void log(int logLevel, String message) {
        cibridge.getLogService().log(logLevel, message);
    }

    public ObservableEmitter<Boolean> getSchedulerClearedObservableEmitter() {
        return schedulerClearedObservableEmitter;
    }

    public ObservableEmitter<Boolean> getSchedulerRunningChangedObservableEmitter() {
        return schedulerRunningChangedObservableEmitter;
    }
}
