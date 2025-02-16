package com.wappcode.java.graphql.models;

public class PaginationInput {
    Integer first;
    String after;
    Integer last;
    String before;

    public PaginationInput() {
    }

    public PaginationInput(Integer first, String after, Integer last, String before) {
        this.first = first;
        this.after = after;
        this.last = last;
        this.before = before;
    }

    public PaginationInput(Integer first, String after) {
        this.first = first;
        this.after = after;
    }

    public PaginationInput(Integer first) {
        this.first = first;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public Integer getLast() {
        return last;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

}
