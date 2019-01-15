package org.cishell.cibridge.core.model;

import java.util.List;

public class AlgorithmInstanceQueryResults implements QueryResults<AlgorithmInstance> {
    private final List<AlgorithmInstance> results;
    private final PageInfo pageInfo;

    public AlgorithmInstanceQueryResults(List<AlgorithmInstance> results, PageInfo pageInfo) {
        this.results = results;
        this.pageInfo = pageInfo;
    }

    public List<AlgorithmInstance> getResults() {
        return results;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

}