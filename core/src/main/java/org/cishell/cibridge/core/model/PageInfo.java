package org.cishell.cibridge.core.model;

public class PageInfo {
    private final boolean hasNextPage;
    private final boolean hasPreviousPage;

    public PageInfo(boolean hasNextPage, boolean hasPreviousPage) {
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public boolean hasPreviousPage() {
        return hasPreviousPage;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "hasNextPage=" + hasNextPage +
                ", hasPreviousPage=" + hasPreviousPage +
                '}';
    }
}
