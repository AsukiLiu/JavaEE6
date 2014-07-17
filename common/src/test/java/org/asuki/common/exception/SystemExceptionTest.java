package org.asuki.common.exception;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.ResourceBundle.getBundle;
import static org.testng.Assert.fail;
import static org.asuki.common.exception.CustomCode.VALUE_TOO_SHORT;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.util.ResourceBundle;

import org.asuki.common.javase.NioUtil;
import org.testng.annotations.Test;

public class SystemExceptionTest {

    private static final int MIN_LENGTH = 8;

    @Test
    public void shouldOccurSystemException() {
        try {
            validate("password", "1234");
        } catch (SystemException e) {
            assertThat((CustomCode) e.getErrorCode(), is(VALUE_TOO_SHORT));
            assertThat(e.getProperties().toString(),
                    is("{field=password, min-length=8, value=1234}"));
            return;
        }

        fail("No exception occured");
    }

    @Test
    public void shouldWrapOtherException() {
        try {
            saveFile("existed.txt");
        } catch (SystemException e) {
            assertThat(e.getProperties().toString(),
                    is("{filePath=existed.txt}"));
            return;
        }

        fail("No exception occured");
    }

    @Test
    public void shouldGetErrorMessage() {
        assertThat(getErrorMessage(VALUE_TOO_SHORT),
                is("The value is too short"));
    }

    private static void validate(String field, String value) {
        if (isNullOrEmpty(value) || value.length() < MIN_LENGTH) {
            // @formatter:off
            throw new SystemException(VALUE_TOO_SHORT)
                    .set("field", field)
                    .set("value", value)
                    .set("min-length", MIN_LENGTH);
            // @formatter:on
        }
    }

    private static void saveFile(String filePath) {
        try {
            NioUtil.saveFile(filePath, "test");
        } catch (IOException e) {
            throw SystemException.wrap(e).set("filePath", filePath);
        }
    }

    private static String getErrorMessage(ErrorCode errorCode) {
        if (errorCode == null) {
            return "";
        }

        String key = errorCode.getClass().getSimpleName() + "_" + errorCode;
        ResourceBundle bundle = getBundle("exceptions");
        return bundle.getString(key);
    }
}
