package org.cishell.cibridge.mock.data;

import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmState;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class Scheduler {
    private boolean running = false;
    private Map<String, ZonedDateTime> schedule = new HashMap<>();
    private Map<String, AlgorithmInstance> algorithms = new HashMap<>();

    public Scheduler() {
        algorithms.put("9667904207", new AlgorithmInstance("9667904207", null, null, null, AlgorithmState.IDLE, null, 0, null));
        algorithms.put("3706776691", new AlgorithmInstance("3706776691", null, null, null, AlgorithmState.IDLE, null, 0, null));
        algorithms.put("2037406284", new AlgorithmInstance("2037406284", null, null, null, AlgorithmState.IDLE, null, 0, null));
        algorithms.put("7442421741", new AlgorithmInstance("7442421741", null, null, null, AlgorithmState.IDLE, null, 0, null));
        algorithms.put("9134426416", new AlgorithmInstance("9134426416", null, null, null, AlgorithmState.IDLE, null, 0, null));

    }

    public Boolean setRunning(boolean running) {
        if (running) {
            System.out.println("scheduler resumed");
        } else {
            System.out.println("scheduler paused");
        }
        this.running = running;
        return this.running;
    }

    public Boolean getRunning() {
        return this.running;
    }

    public int getQueueWaiting() {
        return schedule.size();
    }

    public Boolean runAlgorithmNow(String algorithmInstanceId) {
        if (!algorithms.containsKey(algorithmInstanceId)) {
            System.out.println("algorithm doesn't exist");
            return false;
        }

        ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Indianapolis"));
        schedule.put(algorithmInstanceId, now);
        System.out.println("algorithm with id " + algorithmInstanceId + " scheduled at " + now);
        return true;
    }

    public Boolean scheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        if (!algorithms.containsKey(algorithmInstanceId)) {
            System.out.println("algorithm doesn't exist");
            return false;
        }

        schedule.put(algorithmInstanceId, date);
        System.out.println("algorithm with id " + algorithmInstanceId + " scheduled at " + date);
        return true;
    }

    public Boolean rescheduleAlgorithm(String algorithmInstanceId, ZonedDateTime date) {
        if (!algorithms.containsKey(algorithmInstanceId)) {
            System.out.println("algorithm doesn't exist");
            return false;
        }

        schedule.put(algorithmInstanceId, date);
        System.out.println("algorithm with id " + algorithmInstanceId + " rescheduled at " + date);
        return true;
    }

    public Boolean unscheduleAlgorithm(String algorithmInstanceId) {
        if (!algorithms.containsKey(algorithmInstanceId)) {
            System.out.println("algorithm doesnt exist");
            return false;
        }

        schedule.remove(algorithmInstanceId);
        System.out.println("algorithm with id " + algorithmInstanceId + " was removed for the scheduler");
        return true;
    }

    public Integer clearScheduler() {
        schedule.clear();
        System.out.println("the scheduler was cleared successfully");
        return schedule.size();
    }

    public Boolean setSchedulerRunning(Boolean running) {
        return running;
    }

}
