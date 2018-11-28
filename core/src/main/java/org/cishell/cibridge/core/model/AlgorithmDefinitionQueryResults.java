package org.cishell.cibridge.core.model;

import java.util.List;

public class AlgorithmDefinitionQueryResults implements QueryResults<AlgorithmDefinition> {
    private final List<AlgorithmDefinition> results;
    private final PageInfo pageInfo;

    public AlgorithmDefinitionQueryResults(List<AlgorithmDefinition> results, PageInfo pageInfo) {
        this.results = results;
        this.pageInfo = pageInfo;
    }


    public List<AlgorithmDefinition> getResults() {
        return results;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    @Override
    public QueryResults<AlgorithmDefinition> getQueryResults(List<AlgorithmDefinition> objList, PageInfo pageInfo) {
        return new AlgorithmDefinitionQueryResults(objList, pageInfo);
    }

}
