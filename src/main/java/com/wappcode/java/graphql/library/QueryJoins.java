package com.wappcode.java.graphql.library;

import java.util.HashMap;
import java.util.List;
import com.wappcode.java.graphql.models.JoinInput;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.Map;

public class QueryJoins<T> {

    private Root<T> root;
    Map<String, Join<Object, Object>> joins;

    public QueryJoins(Root<T> root) {
        this.root = root;
        this.joins = new HashMap<String, Join<Object, Object>>();
    }

    public Map<String, Join<Object, Object>> getJoins() {
        return joins;
    }

    public static String createKey(JoinInput joinInput) {
        String property = joinInput.getProperty();
        String alias = joinInput.getJoinedProperty();
        return alias == null ? property : alias;

    }

    public void addJoins(List<JoinInput> joinsInputs) {
        if (joinsInputs == null) {
            return;
        }
        for (JoinInput joinInput : joinsInputs) {
            String key = createKey(joinInput);
            if (joins.containsKey(key)) {
                continue;
            }
            Join<Object, Object> join = createJoin(joinInput);
            joins.put(key, join);
        }
    }

    private Join<Object, Object> createJoin(JoinInput joinInput) {
        String property = joinInput.getProperty();
        String joinedProperty = joinInput.getJoinedProperty();
        String key = createKey(joinInput);

        JoinType joinType = createJoinType(joinInput);
        Join<Object, Object> join;
        if (joinedProperty == null) {
            join = root.join(property, joinType);
        } else {
            Join<Object, Object> joined = joins.get(key);
            join = joined.join(property, joinType);
        }
        return join;

    }

    private JoinType createJoinType(JoinInput joinInput) {
        JoinType joinType;
        switch (joinInput.getJoinType()) {
            case INNER:
                joinType = JoinType.INNER;
                break;
            case LEFT:
                joinType = JoinType.LEFT;
                break;
            default:
                joinType = JoinType.INNER;
                break;
        }
        return joinType;
    }
}
