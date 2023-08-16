package com.wappcode.java.graphql.models;

import java.util.List;

public class ConnectionInput {
    PaginationInput pagination;
    List<FilterGroupInput> filters;
    List<SortGroupInput> sorts;
    List<JoinInput> joins;

    public ConnectionInput() {
    }

    public ConnectionInput(PaginationInput pagination) {
        this.pagination = pagination;
    }

    public ConnectionInput(PaginationInput pagination, List<FilterGroupInput> filters, List<SortGroupInput> sorts,
            List<JoinInput> joins) {
        this.pagination = pagination;
        this.filters = filters;
        this.sorts = sorts;
        this.joins = joins;
    }

    public PaginationInput getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInput pagination) {
        this.pagination = pagination;
    }

    public List<FilterGroupInput> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterGroupInput> filters) {
        this.filters = filters;
    }

    public List<SortGroupInput> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortGroupInput> sorts) {
        this.sorts = sorts;
    }

    public List<JoinInput> getJoins() {
        return joins;
    }

    public void setJoins(List<JoinInput> joins) {
        this.joins = joins;
    }

}
