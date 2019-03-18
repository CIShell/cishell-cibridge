package org.cishell.cibridge.core.model;

import java.util.List;

public class NotificationFilter {
    private List<String> ids;
    private Boolean isClosed;
    private int limit;
    private int offset;

    public NotificationFilter(){
    }

    public NotificationFilter(List<String> ids, Boolean isClosed, int limit, int offset) {
        this.ids = ids;
        this.isClosed = isClosed;
        this.limit = limit;
        this.offset = offset;
    }

    public List<String> getID() {
        return ids;
    }

    public void setID(List<String> ids) {
        this.ids = ids;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
