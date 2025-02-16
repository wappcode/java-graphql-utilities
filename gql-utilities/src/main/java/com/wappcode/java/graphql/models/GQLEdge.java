package com.wappcode.java.graphql.models;

public class GQLEdge<T> {
    String cursor;
    T node;

    public GQLEdge() {

    }

    public GQLEdge(String cursor, T node) {
        this.cursor = cursor;
        this.node = node;

    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        this.node = node;
    }

}
