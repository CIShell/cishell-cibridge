package org.cishell.cibridge.core.model;

import java.util.List;

public class DataQueryResults implements QueryResults<Data> {
    private final List<Data> results;
    private final PageInfo pageInfo;
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_LIMIT = 500;

    public DataQueryResults(List<Data> results, PageInfo pageInfo) {
        this.results = results;
        this.pageInfo = pageInfo;
    }

    public List<Data> getResults() {
        return results;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    @Override
    public QueryResults<Data> getQueryResults(List<Data> objList, PageInfo pageInfo) {
        return this;
    }

}
