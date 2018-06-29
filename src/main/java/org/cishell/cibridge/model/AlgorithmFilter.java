package org.cishell.cibridge.model;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmFilter {
    List<Integer> algortihmDefinitionIds = new ArrayList<Integer>();
    List<Integer> algortihmInstanceIds = new ArrayList<Integer>();
    AlgorithmState states;
    List<Integer> inputDataIds = new ArrayList<Integer>();
    String inputFormats;
    String outputFormats;
    PropertyInput properties;
    int limit;
    int offset;
}
