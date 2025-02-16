package com.wappcode.java.graphql.models;

import java.util.List;

public class GQLConnection<T> {

    Long totalCount;
    GQLPageInfo pageInfo;
    List<GQLEdge<T>> edges;

    public GQLConnection() {
    }

    public GQLConnection(Long totalCount, GQLPageInfo pageInfo, List<GQLEdge<T>> edges) {
        this.totalCount = totalCount;
        this.pageInfo = pageInfo;
        this.edges = edges;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public GQLPageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(GQLPageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<GQLEdge<T>> getEdges() {
        return edges;
    }

    public void setEdges(List<GQLEdge<T>> edges) {
        this.edges = edges;
    }

}
