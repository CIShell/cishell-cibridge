package org.cishell.cibridge.core.model;

import java.util.List;

public interface QueryResults<T> {

    public List<T> getResults();

    public PageInfo getPageInfo();

    public QueryResults<T> getQueryResults(List<T> objList, PageInfo pageInfo);

}
