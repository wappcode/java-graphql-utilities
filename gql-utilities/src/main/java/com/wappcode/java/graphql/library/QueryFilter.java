package com.wappcode.java.graphql.library;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wappcode.java.graphql.models.FilterCompoundConditionsInput;
import com.wappcode.java.graphql.models.FilterConditionInput;
import com.wappcode.java.graphql.models.FilterGroupInput;
import com.wappcode.java.graphql.models.FilterLogic;
import com.wappcode.java.graphql.models.FilterOperator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;

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
        List<FilterConditionInput> conditionInputs = filterGroup.getConditions() == null ? new ArrayList<>()
                : filterGroup.getConditions();
        List<FilterCompoundConditionsInput> compoundConditionsInputs = filterGroup.getCompoundConditions() == null
                ? new ArrayList<>()
                : filterGroup.getCompoundConditions();
        List<Predicate> conditionsPredicates = conditionInputs.stream()
                .map(condition -> createConditionPredicate(condition)).toList();
        List<Predicate> compounPredicates = compoundConditionsInputs.stream()
                .map(condition -> createCompoudConditionsPredicate(condition)).toList();

        List<Predicate> predicates = Stream.concat(conditionsPredicates.stream(), compounPredicates.stream())
                .collect(Collectors.toList());
        if (conditionsLogic == FilterLogic.OR) {
            groupPredicate = cb.or(predicates.toArray(new Predicate[0]));
        } else {
            groupPredicate = cb.and(predicates.toArray(new Predicate[0]));
        }
        return groupPredicate;
    }

    private Predicate createCompoudConditionsPredicate(FilterCompoundConditionsInput filterCompoundCondition) {
        FilterLogic conditionsLogic = filterCompoundCondition.getConditionsLogic();
        Predicate groupPredicate;
        List<FilterConditionInput> conditionInputs = filterCompoundCondition.getConditions() == null ? new ArrayList<>()
                : filterCompoundCondition.getConditions();
        List<FilterCompoundConditionsInput> compoundConditionsInputs = filterCompoundCondition
                .getCompoundConditions() == null
                        ? new ArrayList<>()
                        : filterCompoundCondition.getCompoundConditions();
        List<Predicate> conditionsPredicates = conditionInputs.stream()
                .map(condition -> createConditionPredicate(condition)).toList();
        List<Predicate> compounPredicates = compoundConditionsInputs.stream()
                .map(condition -> createCompoudConditionsPredicate(condition)).toList();

        List<Predicate> predicates = Stream.concat(conditionsPredicates.stream(), compounPredicates.stream())
                .collect(Collectors.toList());
        if (conditionsLogic == FilterLogic.OR) {
            groupPredicate = cb.or(predicates.toArray(new Predicate[0]));
        } else {
            groupPredicate = cb.and(predicates.toArray(new Predicate[0]));
        }
        return groupPredicate;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Predicate createConditionPredicate(FilterConditionInput condition) {
        FilterOperator operator = condition.getFilterOperator();
        From<?, ?> from = getFrom(condition.getOnJoinedProperty());
        String property = condition.getProperty();
        String rawValue = condition.getValue().getSingle();

        Attribute<?, ?> attribute = root.getModel().getAttribute(property);
        Class<?> attributeType = attribute.getJavaType();
        if (rawValue == null) {
            rawValue = "";
        }
        List<String> rawvalues = condition.getValue().getMany();
        if (rawvalues == null) {
            rawvalues = List.of("");
        }
        var value = standardizeFilterValue(rawValue, attributeType);

        List<?> values = rawvalues.stream()
                .map(v -> (standardizeFilterValue(v, attributeType)))
                .toList();
        if (operator == FilterOperator.LIKE) {
            return cb.like(from.get(property), (String) value);
        }
        if (operator == FilterOperator.NOT_LIKE) {
            return cb.notLike(from.get(property), (String) value);
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
            return cb.between(from.get(property), (Comparable) values.get(0),
                    (Comparable) values.get(1));
        }
        if (operator == FilterOperator.GREATER_THAN) {
            return cb.greaterThan(from.get(property), (Comparable) value);
        }
        if (operator == FilterOperator.GREATER_EQUAL_THAN) {
            return cb.greaterThanOrEqualTo(from.get(property), (Comparable) value);
        }
        if (operator == FilterOperator.LESS_THAN) {
            return cb.lessThan(from.get(property), (Comparable) value);
        }
        if (operator == FilterOperator.LESS_EQUAL_THAN) {
            return cb.lessThanOrEqualTo(from.get(property), (Comparable) value);
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

    @SuppressWarnings({ "unchecked", "hiding" })
    private <T> T standardizeFilterValue(String value, Class<T> valueClass) {
        if (valueClass == Boolean.class) {
            return value.equals("1") ? (T) Boolean.TRUE : (T) Boolean.FALSE;
        }
        if (valueClass == Integer.class) {
            return (T) Integer.valueOf(value);
        }
        if (valueClass == Long.class) {
            return (T) Long.valueOf(value);
        }
        if (valueClass == Double.class) {
            return (T) Double.valueOf(value);
        }
        if (valueClass == Float.class) {
            return (T) Float.valueOf(value);
        }
        if (valueClass == Short.class) {
            return (T) Short.valueOf(value);
        }
        if (valueClass == Byte.class) {
            return (T) Byte.valueOf(value);
        }
        if (valueClass == Date.class) {
            ZonedDateTime valueDateTime = ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            Date date = Date.from(valueDateTime.toInstant());
            return (T) date;

        }
        return (T) value;
    }

}
