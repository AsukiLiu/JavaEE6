package org.asuki.common.exception;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.ResourceBundle.getBundle;
import static org.testng.Assert.fail;
import static org.asuki.common.exception.CustomCode.VALUE_TOO_SHORT;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

import static com.googlecode.catchexception.CatchException.*;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.*;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.*;

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

    @Test
    public void shouldGetExceptionWhenAgeLessThan0() {
        Person person = new Person();

        catchException(person).setAge(-1);

        assertThat(caughtException(),
                instanceOf(IllegalArgumentException.class));
        assertThat(caughtException().getMessage(),
                containsString(Person.AGE_IS_INVALID));
    }

    // BDD風
    @Test
    public void shouldGetExceptionWhenAgeLessThan0ByBdd() {
        // given
        Person person = new Person();

        // when
        when(person).setAge(-1);

        // @formatter:off
        // then
        then(caughtException())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Person.AGE_IS_INVALID)
                .hasNoCause();
        // @formatter:on
    }

    // Hamcrest風
    @SuppressWarnings("unchecked")
    @Test
    public void shouldGetExceptionWhenAgeLessThan0ByHamcrest() {
        // given
        Person person = new Person();

        // when
        when(person).setAge(-1);

        // @formatter:off
        // then
        assertThat(caughtException(), allOf(
                instanceOf(IllegalArgumentException.class)
                ,hasMessage(Person.AGE_IS_INVALID)
                ,hasNoCause()));
        // @formatter:on
    }

    private static class Person {
        public static final String AGE_IS_INVALID = "age is invalid";

        public void setAge(int age) {
            if (age < 0) {
                throw new IllegalArgumentException(AGE_IS_INVALID);
            }
            // do something
        }
    }
}
