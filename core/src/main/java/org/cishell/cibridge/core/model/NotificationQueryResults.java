package org.cishell.cibridge.core.model;

import org.cishell.cibridge.core.model.interfaces.QueryResults;

import java.util.List;

public class NotificationQueryResults implements QueryResults<Notification> {
    private final List<Notification> results;
    private final PageInfo pageInfo;

    public NotificationQueryResults(List<Notification> results, PageInfo pageInfo) {
        this.results = results;
        this.pageInfo = pageInfo;
    }

    public List<Notification> getResults() {
        return results;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    @Override
    public QueryResults<Notification> getQueryResults(List<Notification> objList, PageInfo pageInfo) {
        return new NotificationQueryResults(objList, pageInfo);
    }

}
