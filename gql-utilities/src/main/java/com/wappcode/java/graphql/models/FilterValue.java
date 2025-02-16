package com.wappcode.java.graphql.models;

import java.util.List;

public class FilterValue {
    String single;
    List<String> many;

    public FilterValue(String single) {
        this.single = single;

    }

    public FilterValue(List<String> many) {
        this.many = many;
    }

    public FilterValue() {
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public List<String> getMany() {
        return many;
    }

    public void setMany(List<String> many) {
        this.many = many;
    }

}
