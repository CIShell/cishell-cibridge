package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.core.model.PageInfo;
import org.cishell.cibridge.core.model.QueryResults;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

//todo rename the class and place it in appropriate package
public class PaginationUtil {
    public static <T, R> QueryResults<T> getPaginatedResults(Iterator<Map.Entry<String, R>> iterator, List<Predicate<T>> criteria, int offset, int limit) {

        if (offset < 1) {
            offset = QueryResults.DEFAULT_OFFSET;
        }

        if (limit < 1) {
            limit = QueryResults.DEFAULT_LIMIT;
        }

        boolean hasNextPage = false;
        boolean hasPreviousPage = false;

        //filter based on criteria
        List<T> resultList = new LinkedList<>();

        while (iterator.hasNext()) {
            //todo put some kind of check here
            T item = (T) iterator.next().getValue();
            boolean satisfied = true;

            for (Predicate<T> criterion : criteria) {
                if (!criterion.test(item)) {
                    satisfied = false;
                    break;
                }
            }

            if (satisfied) {
                if (offset == 0) {
                    if (limit > 0) {
                        resultList.add(item);
                        limit--;
                    } else {
                        hasNextPage = true;
                        break;
                    }

                } else {
                    hasPreviousPage = true;
                    offset--;
                }
            }
        }

        return new QueryResultsImpl<>(resultList, new PageInfo(hasNextPage, hasPreviousPage));
    }

}
