package org.asuki.test.bv;

import static com.google.common.collect.Iterables.toArray;
import static org.asuki.common.Constants.Systems.LINE_SEPARATOR;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.testng.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.asuki.bv.CustomValidator;
import org.asuki.exception.ServiceException;
import org.asuki.test.BaseArquillian;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.persistence20.PersistenceDescriptor;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Splitter;

public class ValidatorIT extends BaseArquillian {

    static {
        resources.put("ValidationMessages_en.properties",
                "ValidationMessages_en.properties");
        resources.put("ValidationMessages_ja.properties",
                "ValidationMessages_ja.properties");

        persistenceXml = Descriptors.create(PersistenceDescriptor.class)  
                .createPersistenceUnit()  
                    .name("testU")  
                    .jtaDataSource("java:jboss/datasources/ExampleDS")  
                .up();
    }

    @Inject
    private CustomValidator customValidator;

    @BeforeMethod
    public void beforeMethod() throws Exception {

    }

    @Test
    public void testValidator() {
        InputBean ib = new InputBean();
        ib.setData("aaabbbccc");

        // @formatter:off
        List<InputSubBean> beans = Arrays.asList(
                new InputSubBean(new BigDecimal("1.2")),
                new InputSubBean(new BigDecimal(12345)), 
                new InputSubBean(new BigDecimal(123456)));
        // @formatter:on
        ib.setBeans(beans);

        try {
            customValidator.validate(ib);
        } catch (ServiceException e) {

            Iterable<String> messages = Splitter.on(LINE_SEPARATOR)
                    .trimResults().split(e.getMessage());

            assertThat(toArray(messages, String.class).length, is(4));
            return;
        }

        fail("No exception happened!");
    }

}
