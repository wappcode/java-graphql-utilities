package com.wappcode.java.graphql.models;

import java.util.List;

public class FilterGroupInput {
    FilterLogic groupLogic;
    FilterLogic conditionsLogic;
    List<FilterConditionInput> conditions;

    public FilterGroupInput(List<FilterConditionInput> conditions, FilterLogic groupLogic,
            FilterLogic conditionsLogic) {
        this.groupLogic = groupLogic;
        this.conditionsLogic = conditionsLogic;
        this.conditions = conditions;
    }

    public FilterGroupInput(List<FilterConditionInput> conditions, FilterLogic conditionsLogic) {
        this.conditionsLogic = conditionsLogic;
        this.conditions = conditions;
        this.groupLogic = FilterLogic.AND;
    }

    public FilterGroupInput(List<FilterConditionInput> conditions) {
        this.conditions = conditions;
        this.groupLogic = FilterLogic.AND;
        this.conditionsLogic = FilterLogic.AND;
    }

    public FilterGroupInput() {
        this.groupLogic = FilterLogic.AND;
        this.conditionsLogic = FilterLogic.AND;
    }

    public FilterLogic getGroupLogic() {
        return groupLogic;
    }

    public void setGroupLogic(FilterLogic groupLogic) {
        this.groupLogic = groupLogic;
    }

    public FilterLogic getConditionsLogic() {
        return conditionsLogic;
    }

    public void setConditionsLogic(FilterLogic conditionsLogic) {
        this.conditionsLogic = conditionsLogic;
    }

    public List<FilterConditionInput> getConditions() {
        return conditions;
    }

    public void setConditions(List<FilterConditionInput> conditions) {
        this.conditions = conditions;
    }

}
