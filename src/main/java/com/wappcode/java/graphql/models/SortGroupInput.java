package com.wappcode.java.graphql.models;

public class SortGroupInput {
    String property;
    SortDirection direction;
    String onJoinedProperty;

    public SortGroupInput(String property, SortDirection direction) {
        this.property = property;
        this.direction = direction;
    }

    public SortGroupInput(String property, SortDirection direction, String onJoinedProperty) {
        this.property = property;
        this.direction = direction;
        this.onJoinedProperty = onJoinedProperty;
    }

    public SortGroupInput() {
        this.direction = SortDirection.ASC;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }

    public String getOnJoinedProperty() {
        return onJoinedProperty;
    }

    public void setOnJoinedProperty(String onJoinedProperty) {
        this.onJoinedProperty = onJoinedProperty;
    }

}
