package com.wappcode.java.graphql.library;

import java.util.List;
import java.util.Map;

import com.wappcode.java.graphql.models.FilterConditionInput;
import com.wappcode.java.graphql.models.FilterGroupInput;
import com.wappcode.java.graphql.models.FilterLogic;
import com.wappcode.java.graphql.models.FilterOperator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class QueryFilter<T> {
    private CriteriaBuilder cb;
    private Root<T> root;
    private Map<String, Join<Object, Object>> joins;

    public QueryFilter(CriteriaBuilder cb, Root<T> root, Map<String, Join<Object, Object>> joins) {
        this.cb = cb;
        this.root = root;
        this.joins = joins;
    }

    public Predicate createPredicate(List<FilterGroupInput> filters) {
        if (filters == null) {
            return null;
        }
        Predicate mainPredicate = null;
        for (FilterGroupInput filterGroup : filters) {
            FilterLogic groupLogic = filterGroup.getGroupLogic();
            Predicate groupPredicate = createGroupPredicate(filterGroup);
            if (mainPredicate == null) {
                mainPredicate = groupPredicate;
            } else if (groupLogic == FilterLogic.OR) {
                mainPredicate = cb.or(mainPredicate, groupPredicate);
            } else {
                mainPredicate = cb.and(mainPredicate, groupPredicate);
            }
        }
        return mainPredicate;
    }

    private Predicate createGroupPredicate(FilterGroupInput filterGroup) {

        FilterLogic conditionsLogic = filterGroup.getConditionsLogic();
        Predicate groupPredicate;
        // List<Predicate> predicates = Collections.emptyList();
        List<Predicate> predicates = filterGroup.getConditions().stream()
                .map(condition -> createConditionPredicate(condition)).toList();
        if (conditionsLogic == FilterLogic.OR) {
            groupPredicate = cb.or(predicates.toArray(new Predicate[0]));
        } else {
            groupPredicate = cb.and(predicates.toArray(new Predicate[0]));
        }
        return groupPredicate;
    }

    private Predicate createConditionPredicate(FilterConditionInput condition) {
        FilterOperator operator = condition.getFilterOperator();
        From<?, ?> from = getFrom(condition.getOnJoinedProperty());
        String property = condition.getProperty();
        String value = condition.getValue().getSingle();
        List<String> values = condition.getValue().getMany();
        if (operator == FilterOperator.LIKE) {
            return cb.like(from.get(property), value);
        }
        if (operator == FilterOperator.NOT_LIKE) {
            return cb.notLike(from.get(property), value);
        }
        if (operator == FilterOperator.EQUAL) {
            return cb.equal(from.get(property), value);
        }
        if (operator == FilterOperator.NOT_EQUAL) {
            return cb.notEqual(from.get(property), value);
        }
        if (operator == FilterOperator.IN) {
            return from.get(property).in(values);
        }
        if (operator == FilterOperator.NOT_IN) {
            return cb.not(from.get(property).in(values));
        }
        if (operator == FilterOperator.BETWEEN) {
            return cb.between(from.get(property), values.get(0), values.get(1));
        }
        if (operator == FilterOperator.GREATER_THAN) {
            return cb.greaterThan(from.get(property), value);
        }
        if (operator == FilterOperator.GREATER_EQUAL_THAN) {
            return cb.greaterThanOrEqualTo(from.get(property), value);
        }
        if (operator == FilterOperator.LESS_THAN) {
            return cb.lessThan(from.get(property), value);
        }
        if (operator == FilterOperator.LESS_EQUAL_THAN) {
            return cb.lessThanOrEqualTo(from.get(property), value);
        }
        if (operator == FilterOperator.NOT_LIKE) {
            return cb.notLike(from.get(property), value);
        }
        if (operator == FilterOperator.IS_NULL) {
            return cb.isNull(from.get(property));
        }
        if (operator == FilterOperator.IS_NOT_NULL) {
            return cb.isNotNull(from.get(property));
        }

        return null;
    }

    private From<?, ?> getFrom(String joinedProperty) {

        if (joinedProperty == null) {
            return root;
        } else {
            return joins.get(joinedProperty);
        }
    }
}
