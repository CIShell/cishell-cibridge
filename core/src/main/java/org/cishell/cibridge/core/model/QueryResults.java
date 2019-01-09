package org.cishell.cibridge.core.model;

import java.util.List;

public interface QueryResults<T> {

    int DEFAULT_OFFSET = 0;
    int DEFAULT_LIMIT = 500;

    public List<T> getResults();

    public PageInfo getPageInfo();

    public QueryResults<T> getQueryResults(List<T> objList, PageInfo pageInfo);

}
