package org.asuki.common.collection;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Function;

public class CollectionTest {

    private static final Function<List<Integer>, List<Integer>> REMOVE_DUPLICATION = new Function<List<Integer>, List<Integer>>() {
        public List<Integer> apply(List<Integer> input) {
            return CollectionUtil.removeDuplication(input);
        };
    };

    private static final Function<List<Integer>, List<Integer>> REMOVE_DUPLICATION_IN_ORDER = new Function<List<Integer>, List<Integer>>() {
        public List<Integer> apply(List<Integer> input) {
            return CollectionUtil.removeDuplicationInOrder(input);
        };
    };

    @Test(dataProvider = "data")
    public void shouldRemoveDuplication(
            Function<List<Integer>, List<Integer>> remove, String expect) {

        List<Integer> numbers = newArrayList(asList(2, 5, 6, 3, 2, 6));

        List<Integer> result = remove.apply(numbers);

        assertThat(result.toString(), is(expect));
    }

    @DataProvider
    public Object[][] data() {
        // @formatter:off
        return new Object[][] { 
                { REMOVE_DUPLICATION,          "[2, 3, 5, 6]" },
                { REMOVE_DUPLICATION_IN_ORDER, "[2, 5, 6, 3]" } };
        // @formatter:on
    }

}
