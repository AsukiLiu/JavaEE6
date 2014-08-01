package org.asuki.common.guava;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import org.asuki.common.guava.forwarding.MovableList;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

public class ForwardingCollectionTest {

    @Test(dataProvider = "data")
    public void testMovableList(Function<MovableList<String>, Boolean> move,
            String expectString) {

        List<String> array = newArrayList("1", "2", "3", "4", "5");
        MovableList<String> list = new MovableList<String>(array);

        move.apply(list);

        assertThat(Joiner.on(',').join(list), is(expectString));
    }

    @DataProvider(name = "data")
    private Object[][] data() {
        final int targetIndex = 2;

        // @formatter:off
        return new Object[][] { 
            { 
                new Function<MovableList<String>, Boolean>() {
                    public Boolean apply(MovableList<String> input) {
                        return input.moveUp(targetIndex);
                    }
                }, "1,3,2,4,5" 
            }, 
            { 
                new Function<MovableList<String>, Boolean>() {
                    public Boolean apply(MovableList<String> input) {
                        return input.moveDown(targetIndex);
                    }
                }, "1,2,4,3,5" 
            }, 
            { 
                new Function<MovableList<String>, Boolean>() {
                    public Boolean apply(MovableList<String> input) {
                        return input.moveTop(targetIndex);
                    }
                }, "3,1,2,4,5" 
            }, 
            { 
                new Function<MovableList<String>, Boolean>() {
                    public Boolean apply(MovableList<String> input) {
                        return input.moveBottom(targetIndex);
                    }
                }, "1,2,4,5,3" 
            }, 
        };
        // @formatter:on
    }

}
