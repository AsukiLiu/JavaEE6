package org.asuki.common.validation;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ValidationTest {

    @Test(dataProvider = "data")
    public void testValidate(String target, boolean isValid, String errorMessages) {
        ValidationResult result = ValidationUtil.validate(target);

        assertThat(result.isValid(), is(isValid));
        assertThat(result.toString(), is(errorMessages));
    }

    @DataProvider
    public Object[][] data() {
        return new Object[][] {
                { "A and B", false, "[ERROR:Starts with A, ERROR:Ends with B]" },
                { "Right", true, "[]" } };
    }

}
