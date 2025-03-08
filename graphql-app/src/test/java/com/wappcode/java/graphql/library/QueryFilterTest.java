package com.wappcode.java.graphql.library;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wappcode.java.graphql.GraphqlApplication;
import com.wappcode.java.graphql.entities.User;
import com.wappcode.java.graphql.models.FilterCompoundConditionsInput;
import com.wappcode.java.graphql.models.FilterConditionInput;
import com.wappcode.java.graphql.models.FilterGroupInput;
import com.wappcode.java.graphql.models.FilterLogic;
import com.wappcode.java.graphql.models.FilterOperator;
import com.wappcode.java.graphql.models.FilterValue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@SpringBootTest(classes = GraphqlApplication.class)
public class QueryFilterTest {

    @Autowired
    private EntityManager em;

    @Test
    public void testQueryFilterEqualBoolean() {

        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        var queryFilter = new QueryFilter<>(cb, root, null);

        var filterGroup = new FilterGroupInput();
        var conditions = new FilterConditionInput();
        var filterValue = new FilterValue();
        filterValue.setSingle("0");
        conditions.setFilterOperator(FilterOperator.EQUAL);
        conditions.setProperty("active");
        conditions.setValue(filterValue);
        filterGroup.setConditions(List.of(conditions));
        var filtersPredicate = queryFilter.createPredicate(List.of(filterGroup));
        cq.where(filtersPredicate);
        TypedQuery<User> tq = em.createQuery(cq);
        List<User> items = tq.getResultList();
        assertTrue(items.size() == 1);

    }

    @Test
    public void testQueryFilterLike() {

        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        var queryFilter = new QueryFilter<>(cb, root, null);

        var filterGroup = new FilterGroupInput();
        var conditions = new FilterConditionInput();
        var filterValue = new FilterValue();
        filterValue.setSingle("%illiam%");
        conditions.setFilterOperator(FilterOperator.LIKE);
        conditions.setProperty("name");
        conditions.setValue(filterValue);
        filterGroup.setConditions(List.of(conditions));
        var filtersPredicate = queryFilter.createPredicate(List.of(filterGroup));
        cq.where(filtersPredicate);
        TypedQuery<User> tq = em.createQuery(cq);
        List<User> items = tq.getResultList();
        assertTrue(items.size() == 2);
        assertTrue(items.get(0).getName().contains("illiam"));

    }

    @Test
    public void testQueryFilterNotLike() {

        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        var queryFilter = new QueryFilter<>(cb, root, null);

        var filterGroup = new FilterGroupInput();
        var conditions = new FilterConditionInput();
        var filterValue = new FilterValue();
        filterValue.setSingle("%illiam%");
        conditions.setFilterOperator(FilterOperator.NOT_LIKE);
        conditions.setProperty("name");
        conditions.setValue(filterValue);
        filterGroup.setConditions(List.of(conditions));
        var filtersPredicate = queryFilter.createPredicate(List.of(filterGroup));
        cq.where(filtersPredicate);
        TypedQuery<User> tq = em.createQuery(cq);
        List<User> items = tq.getResultList();
        assertTrue(items.size() == 2);
        assertTrue(!items.get(0).getName().contains("illiam"));

    }

    @Test
    public void testQueryFilterIn() {

        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        var queryFilter = new QueryFilter<>(cb, root, null);

        var filterGroup = new FilterGroupInput();
        var conditions = new FilterConditionInput();
        var filterValue = new FilterValue();
        filterValue.setMany(List.of("1"));
        conditions.setFilterOperator(FilterOperator.IN);
        conditions.setProperty("active");
        conditions.setValue(filterValue);
        filterGroup.setConditions(List.of(conditions));
        var filtersPredicate = queryFilter.createPredicate(List.of(filterGroup));
        cq.where(filtersPredicate);
        TypedQuery<User> tq = em.createQuery(cq);
        List<User> items = tq.getResultList();
        assertTrue(items.size() == 3);

    }

    @Test
    public void testQueryFilterCompoud() {

        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        var queryFilter = new QueryFilter<>(cb, root, null);

        var filterGroup = new FilterGroupInput();
        var filterCompound = new FilterCompoundConditionsInput();

        var conditionIn = new FilterConditionInput();
        var filterValue = new FilterValue();
        filterValue.setMany(List.of("1"));
        conditionIn.setFilterOperator(FilterOperator.IN);
        conditionIn.setProperty("active");
        var conditionLike = new FilterConditionInput();
        conditionLike.setProperty("name");
        conditionLike.setFilterOperator(FilterOperator.LIKE);
        conditionLike.setValue(new FilterValue("%William%"));
        conditionIn.setValue(filterValue);
        filterCompound.setConditionsLogic(FilterLogic.OR);
        filterCompound.setConditions(List.of(conditionIn, conditionLike));
        filterGroup.setCompoundConditions(List.of(filterCompound));
        var filtersPredicate = queryFilter.createPredicate(List.of(filterGroup));
        cq.where(filtersPredicate);
        TypedQuery<User> tq = em.createQuery(cq);
        List<User> items = tq.getResultList();

        assertTrue(items.size() == 4);

    }
    // TODO: Add test to compare dates and numbers with between and greater than,
    // less than, etc.

}
