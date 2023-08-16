package com.wappcode.java.graphql.models;

public class GQLPageInfo {
    boolean hasNextPage;
    boolean hasPreviousPage;
    String startCursor;
    String endCursor;

    public GQLPageInfo() {
    }

    public GQLPageInfo(boolean hasNextPage, boolean hasPreviousPage, String startCursor, String endCursor) {
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
        this.startCursor = startCursor;
        this.endCursor = endCursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public String getStartCursor() {
        return startCursor;
    }

    public void setStartCursor(String startCursor) {
        this.startCursor = startCursor;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }

}
