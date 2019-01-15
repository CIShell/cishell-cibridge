package org.cishell.cibridge.core.model;

import java.util.List;

public interface QueryResults<T> {

    public static int DEFAULT_OFFSET = 0;
    public static int DEFAULT_LIMIT = 500;

    public List<T> getResults();

    public PageInfo getPageInfo();
}