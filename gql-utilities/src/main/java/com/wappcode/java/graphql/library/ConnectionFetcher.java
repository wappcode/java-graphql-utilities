package com.wappcode.java.graphql.library;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.wappcode.java.graphql.models.ConnectionInput;
import com.wappcode.java.graphql.models.FilterGroupInput;
import com.wappcode.java.graphql.models.GQLConnection;
import com.wappcode.java.graphql.models.GQLEdge;
import com.wappcode.java.graphql.models.GQLPageInfo;
import com.wappcode.java.graphql.models.PaginationInput;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ConnectionFetcher<T> {

    private EntityManager em;
    private Class<T> className;
    private ConnectionInput connectionInput;
    private List<FilterGroupInput> appFilterInput;

    public ConnectionFetcher(EntityManager em, Class<T> className, ConnectionInput connectionInput) {
        this.em = em;
        this.className = className;
        this.connectionInput = connectionInput;
    }

    public ConnectionFetcher(EntityManager em, Class<T> className, ConnectionInput connectionInput,
            List<FilterGroupInput> appFilterInput) {
        this.em = em;
        this.className = className;
        this.connectionInput = connectionInput;
        this.appFilterInput = appFilterInput;
    }

    private ConnectionInput getNotEmptyInput() {
        if (connectionInput == null) {
            return new ConnectionInput();
        } else {
            return connectionInput;
        }
    }

    public GQLConnection<T> getResult() {
        var paginationInput = getNotEmptyInput().getPagination();
        Long totalCount = calculateTotalCount();
        List<GQLEdge<T>> edges = calculateEdges(paginationInput);
        GQLPageInfo pageInfo = calculatePageInfo(paginationInput, totalCount, edges);
        GQLConnection<T> connection = new GQLConnection<T>(totalCount, pageInfo, edges);
        return connection;
    }

    private List<GQLEdge<T>> calculateEdges(PaginationInput input) {
        if (input == null) {
            return new ArrayList<>();
        }
        var joinsInputs = getNotEmptyInput().getJoins();
        var sortsInputs = getNotEmptyInput().getSorts();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(className);
        Root<T> root = cq.from(className);
        QueryJoins<T> qJoins = new QueryJoins<>(root);
        qJoins.addJoins(joinsInputs);
        var joins = qJoins.getJoins();

        QueryFilter<T> qFilter = new QueryFilter<>(cb, root, joins);
        var filtersPredicate = createPredicate(qFilter, cb);
        QuerySort<T> qSort = new QuerySort<>(cb, root, joins);
        qSort.addSorts(sortsInputs);
        var sorts = qSort.getSorts();

        if (filtersPredicate != null) {
            cq.where(filtersPredicate);
        }
        if (sorts != null) {
            cq.orderBy(sorts);
        }
        Integer first = input.getFirst();
        Integer last = input.getLast();
        List<T> items;
        Integer cursor;
        TypedQuery<T> tq = em.createQuery(cq);
        if (first != null) {
            cursor = calculateCursorNumericValue(input.getAfter());
            tq.setMaxResults(first);
            tq.setFirstResult(cursor);
            items = tq.getResultList();
        } else if (last != null) {
            cursor = calculateCursorNumericValue(input.getBefore());
            tq.setMaxResults(last);
            tq.setFirstResult(cursor);
            items = tq.getResultList();
        } else {
            items = new ArrayList<>();
            cursor = 0;
        }
        var edges = createEdges(items, first, last, cursor);

        return edges;
    }

    private List<GQLEdge<T>> createEdges(List<T> items, Integer first, Integer last, Integer cursor) {
        List<GQLEdge<T>> result = new ArrayList<>();
        int currentIndex = 0;
        for (T item : items) {
            if (first == null && last == null) {
                continue;
            }
            Integer nodeCursorValue = first != null ? cursor + (currentIndex) : cursor - (currentIndex);
            String nodeCursor = calculateCursor(nodeCursorValue);
            GQLEdge<T> edge = new GQLEdge<T>(nodeCursor, item);
            result.add(currentIndex, edge);
            currentIndex++;
        }
        return result;
    }

    private Long calculateTotalCount() {
        var joinsInputs = getNotEmptyInput().getJoins();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(className);
        QueryJoins<T> qJoins = new QueryJoins<>(root);
        qJoins.addJoins(joinsInputs);
        var joins = qJoins.getJoins();

        QueryFilter<T> qFilter = new QueryFilter<>(cb, root, joins);
        Predicate filtersPredicate = createPredicate(qFilter, cb);

        cq.select(cb.countDistinct(root.get("id")));
        if (filtersPredicate != null) {
            cq.where(filtersPredicate);
        }
        TypedQuery<Long> tq = em.createQuery(cq);
        tq.setMaxResults(1);
        return tq.getSingleResult();

    }

    private GQLPageInfo calculatePageInfo(PaginationInput input, Long totalCount, List<GQLEdge<T>> edges) {
        if (edges.size() == 0) {
            return this.createDefaultPageInfo();
        }
        Boolean hasNextPage = this.calculateHasNextPage(input, totalCount);
        Boolean hasPreviousPage = calculateHasPreviousPage(input, totalCount);
        GQLEdge<?> firstNode = edges.get(0);
        GQLEdge<?> lastNode = edges.get(edges.size() - 1);
        GQLPageInfo pageInfo = new GQLPageInfo(hasNextPage, hasPreviousPage, firstNode.getCursor(),
                lastNode.getCursor());
        return pageInfo;
    }

    private GQLPageInfo createDefaultPageInfo() {
        String cursor = this.calculateCursor(0);
        GQLPageInfo pageInfo = new GQLPageInfo(false, false, cursor, cursor);
        return pageInfo;
    }

    private String calculateCursor(Integer value) {
        String cursorValue = String.valueOf(value);
        String cursor = Base64.getEncoder().encodeToString(cursorValue.getBytes());
        return cursor;
    }

    private Boolean calculateHasNextPage(PaginationInput input, Long totalCount) {

        Integer first = input.getFirst();
        Integer last = input.getLast();
        if (first != null) {
            Integer cursorNumber = calculateCursorNumericValue(input.getAfter());
            Integer lastItemPage = cursorNumber + first;
            return totalCount > lastItemPage;

        } else if (last != null) {
            Integer cursorNumber = calculateCursorNumericValue(input.getBefore());
            return cursorNumber > last;
        } else {
            return false;
        }
    }

    private Boolean calculateHasPreviousPage(PaginationInput input, Long totalCount) {

        Integer first = input.getFirst();
        Integer last = input.getLast();
        if (first != null) {
            Integer cursorNumber = calculateCursorNumericValue(input.getAfter());
            return cursorNumber > first;

        } else if (last != null) {
            Integer cursorNumber = calculateCursorNumericValue(input.getBefore());
            return cursorNumber < totalCount - last;
        } else {
            return false;
        }
    }

    private Integer calculateCursorNumericValue(String cursor) {
        if (cursor == null) {
            return 0;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(cursor);
        String decodedString = new String(decodedBytes);
        Integer value = Integer.parseInt(decodedString);
        return value;
    }

    private Predicate createPredicate(QueryFilter<T> qFilter, CriteriaBuilder cb) {
        var requestFilterInput = getNotEmptyInput().getFilters();
        Predicate filtersPredicate = null;
        var requestPredicate = qFilter.createPredicate(requestFilterInput);
        var appPredicate = qFilter.createPredicate(appFilterInput);
        if (requestPredicate != null && appPredicate != null) {
            filtersPredicate = cb.and(requestPredicate, appPredicate);
        } else if (requestPredicate != null) {
            filtersPredicate = requestPredicate;
        } else if (appPredicate != null) {
            filtersPredicate = appPredicate;
        }
        return filtersPredicate;
    }

}
