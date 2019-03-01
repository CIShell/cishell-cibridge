package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.core.model.PageInfo;
import org.cishell.cibridge.core.model.QueryResults;

import java.util.List;

//TODO rename this class to something more useful
public class QueryResultsImpl<T> implements QueryResults<T> {
    private List<T> results;
    private PageInfo pageInfo;

    public QueryResultsImpl(List<T> results, PageInfo pageInfo) {
        this.results = results;
        this.pageInfo = pageInfo;
    }

    @Override
    public List<T> getResults() {
        return results;
    }

    @Override
    public PageInfo getPageInfo() {
        return pageInfo;
    }
}
