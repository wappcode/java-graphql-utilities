package com.wappcode.java.graphql.library;

import java.util.List;
import java.util.Map;
import com.wappcode.java.graphql.models.SortGroupInput;
import com.wappcode.java.graphql.models.SortType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class QuerySort<T> {

    private List<Order> sorts;
    private CriteriaBuilder cb;
    private Root<T> root;
    private Map<String, Join<Object, Object>> joins;

    public QuerySort(CriteriaBuilder cb, Root<T> root, Map<String, Join<Object, Object>> joins) {
        this.cb = cb;
        this.root = root;
        this.joins = joins;
    }

    public List<Order> getSorts() {
        return sorts;
    }

    public void addSorts(List<SortGroupInput> sorts) {
        if (sorts == null) {
            return;
        }
        List<Order> orders = sorts.stream().map(sort -> createSort(sort)).toList();
        this.sorts = orders;

    }

    private Order createSort(SortGroupInput sorting) {
        String property = sorting.getProperty();
        String joinedProperty = sorting.getOnJoinedProperty();
        Order order;
        From<?, ?> from = getFrom(joinedProperty);
        if (sorting.getDirection().equals(SortType.DESC)) {
            order = cb.desc(from.get(property));
        } else {
            order = cb.asc(from.get(property));
        }
        return order;
    }

    private From<?, ?> getFrom(String joinedProperty) {

        if (joinedProperty == null) {
            return root;
        } else {
            return joins.get(joinedProperty);
        }
    }
}
