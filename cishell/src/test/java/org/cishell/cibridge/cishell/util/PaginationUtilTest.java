package org.cishell.cibridge.cishell.util;

import org.cishell.cibridge.core.model.QueryResults;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class PaginationUtilTest {

    @Test
    public void getPaginatedResultsWithEmptyFilter() {
        List<Integer> listOfIntegers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        QueryResults<Integer> queryResults;

        //limit:3 offset:0
        //first page
        queryResults = PaginationUtil.getPaginatedResults(listOfIntegers, new ArrayList<>(), 0, 3);
        assertTrue(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(3, queryResults.getResults().size());
        assertEquals(1, queryResults.getResults().get(0).intValue());
        assertEquals(2, queryResults.getResults().get(1).intValue());
        assertEquals(3, queryResults.getResults().get(2).intValue());

        //limit:3 offset:3
        //second page
        queryResults = PaginationUtil.getPaginatedResults(listOfIntegers, new ArrayList<>(), 3, 3);
        assertTrue(queryResults.getPageInfo().hasNextPage());
        assertTrue(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(3, queryResults.getResults().size());
        assertEquals(4, queryResults.getResults().get(0).intValue());
        assertEquals(5, queryResults.getResults().get(1).intValue());
        assertEquals(6, queryResults.getResults().get(2).intValue());

        //limit:3 offset:6
        //third and the last page
        queryResults = PaginationUtil.getPaginatedResults(listOfIntegers, new ArrayList<>(), 6, 3);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertTrue(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(2, queryResults.getResults().size());
        assertEquals(7, queryResults.getResults().get(0).intValue());
        assertEquals(8, queryResults.getResults().get(1).intValue());

        //limit:9 offset:0
        //should return everything
        queryResults = PaginationUtil.getPaginatedResults(listOfIntegers, new ArrayList<>(), 0, 9);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(8, queryResults.getResults().size());


        //limit:9 offset:9
        //should not return anything
        queryResults = PaginationUtil.getPaginatedResults(listOfIntegers, new ArrayList<>(), 9, 0);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertTrue(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void getPaginatedResultsWithEvenFilter() {
        List<Integer> listOfIntegers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        QueryResults<Integer> queryResults;

        List<Predicate<Integer>> criteria = new ArrayList<>();
        criteria.add(n -> n % 2 == 0);

        //should return all even numbers
        queryResults = PaginationUtil.getPaginatedResults(listOfIntegers, criteria, 0, 8);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(4, queryResults.getResults().size());
        assertEquals(2, queryResults.getResults().get(0).intValue());
        assertEquals(4, queryResults.getResults().get(1).intValue());
        assertEquals(6, queryResults.getResults().get(2).intValue());
        assertEquals(8, queryResults.getResults().get(3).intValue());
    }

}
