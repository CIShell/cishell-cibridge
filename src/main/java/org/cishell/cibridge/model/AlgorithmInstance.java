package org.cishell.cibridge.model;
import java.time.LocalTime;

public class AlgorithmInstance {
    int ID;
    Data inData;
    Property parameters;
    AlgorithmDefinition algorithmDefinition;
    AlgorithmState state;
    LocalTime scheduledRunTime;
    int progress;
    Data outData;
}
