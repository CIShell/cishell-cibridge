package org.cishell.cibridge.core.model;

import org.cishell.cibridge.core.model.interfaces.QueryResults;

import java.util.List;

public class AlgorithmInstanceQueryResults implements QueryResults<AlgorithmInstance> {
    public final List<AlgorithmInstance> results;
    public final PageInfo pageInfo;

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

    @Override
    public QueryResults<AlgorithmInstance> getQueryResults(List<AlgorithmInstance> objList, PageInfo pageInfo) {
        return null;
    }

}
