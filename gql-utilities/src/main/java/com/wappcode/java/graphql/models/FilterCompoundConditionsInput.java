package com.wappcode.java.graphql.models;

import java.util.List;

import com.wappcode.java.graphql.models.FilterLogic;

public class FilterCompoundConditionsInput {
    FilterLogic conditionsLogic;
    List<FilterConditionInput> conditions;
    List<FilterCompoundConditionsInput> compoundConditions;

    public FilterCompoundConditionsInput(List<FilterConditionInput> conditions,
            List<FilterCompoundConditionsInput> compoundConditions,
            FilterLogic conditionsLogic) {
        this.conditionsLogic = conditionsLogic;
        this.conditions = conditions;
        this.compoundConditions = compoundConditions;
    }

    public FilterCompoundConditionsInput(List<FilterConditionInput> conditions, FilterLogic conditionsLogic) {
        this.conditionsLogic = conditionsLogic;
        this.conditions = conditions;
    }

    public FilterCompoundConditionsInput(List<FilterConditionInput> conditions) {
        this.conditions = conditions;
        this.conditionsLogic = FilterLogic.AND;
    }

    public FilterCompoundConditionsInput() {
        this.conditionsLogic = FilterLogic.AND;
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

    public List<FilterCompoundConditionsInput> getCompoundConditions() {
        return compoundConditions;
    }

    public void setCompoundConditions(List<FilterCompoundConditionsInput> compoundConditions) {
        this.compoundConditions = compoundConditions;
    }

}
