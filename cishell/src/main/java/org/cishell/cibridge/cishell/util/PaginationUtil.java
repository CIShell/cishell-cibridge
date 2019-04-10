package org.cishell.cibridge.cishell.util;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.cishell.impl.QueryResultsImpl;
import org.cishell.cibridge.core.model.PageInfo;
import org.cishell.cibridge.core.model.QueryResults;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

//todo rename the class to Pagination
//the parameters in the static methods should go inside constructor of pagination object
public class PaginationUtil {
    public static <T> QueryResults<T> getPaginatedResults(List<T> items, List<Predicate<T>> criteria, int offset, int limit) {
        Preconditions.checkNotNull(items, "list of items to be paginated cannot be null");
        Preconditions.checkNotNull(criteria, "list of criteria for filtering cannot be null");
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
        for (T item : items) {
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
