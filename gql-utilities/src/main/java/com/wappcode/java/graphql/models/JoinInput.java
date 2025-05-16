package com.wappcode.java.graphql.models;

public class JoinInput {
    String property;
    JoinType joinType;
    String joinedProperty;
    String alias;

    public JoinInput(String property, JoinType joinType, String alias, String joinedProperty) {
        this.property = property;
        this.joinType = joinType;
        this.joinedProperty = joinedProperty;
        this.alias = alias;
    }

    public JoinInput(String property, JoinType joinType) {
        this.property = property;
        this.joinType = joinType;
    }

    public JoinInput() {
        this.joinType = JoinType.INNER;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public String getJoinedProperty() {
        return joinedProperty;
    }

    public void setJoinedProperty(String joinedProperty) {
        this.joinedProperty = joinedProperty;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
