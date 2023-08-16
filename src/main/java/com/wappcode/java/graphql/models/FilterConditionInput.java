package com.wappcode.java.graphql.models;

public class FilterConditionInput {
    FilterOperator filterOperator;
    String property;
    FilterValue value;
    String onJoinedProperty;

    public FilterConditionInput(FilterOperator filterOperator, String property, FilterValue value,
            String onJoinedProperty) {
        this.filterOperator = filterOperator;
        this.property = property;
        this.value = value;
        this.onJoinedProperty = onJoinedProperty;
    }

    public FilterConditionInput() {
    }

    public FilterOperator getFilterOperator() {
        return filterOperator;
    }

    public void setFilterOperator(FilterOperator filterOperator) {
        this.filterOperator = filterOperator;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public FilterValue getValue() {
        return value;
    }

    public void setValue(FilterValue value) {
        this.value = value;
    }

    public String getOnJoinedProperty() {
        return onJoinedProperty;
    }

    public void setOnJoinedProperty(String onJoinedProperty) {
        this.onJoinedProperty = onJoinedProperty;
    }

}
