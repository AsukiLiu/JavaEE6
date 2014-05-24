package org.asuki.test.service;

import static org.asuki.model.entity.Address.builder;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.testng.Assert.fail;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import org.asuki.model.entity.Address;
import org.asuki.service.AddressService;
import org.asuki.test.BaseArquillian;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddressServiceIT extends BaseArquillian {

    static {
        webInfResources.put("META-INF/xml/entity.xml",
                "classes/META-INF/xml/entity.xml");
        webInfResources.put("META-INF/xml/jpql.xml",
                "classes/META-INF/xml/jpql.xml");
        webInfResources.put("META-INF/xml/sql.xml",
                "classes/META-INF/xml/sql.xml");
        webInfResources.put("META-INF/testPersistence.xml",
                "classes/META-INF/persistence.xml");
    }

    @EJB
    private AddressService service;

    @BeforeMethod
    public void beforeMethod() throws Exception {

    }

    @Test
    public void testCrud() {

        List<Address> addresses = service.findAll();
        assertThat(addresses.size(), is(0));

        // @formatter:off
        Address expected = builder()
                .city("city")
                .prefecture("prefecture")
                .zipCode("zipCode")
                .build();
        // @formatter:on

        expected = service.create(expected);

        addresses = service.findAll();
        assertThat(addresses.size(), is(1));

        Address actual = service.findById(expected.getId());
        assertThat(actual.toString(), is(expected.toString()));

        service.delete(expected.getId());

        addresses = service.findAll();
        assertThat(addresses.size(), is(0));

    }

    @Test(dataProvider = "data", expectedExceptions = EJBException.class, expectedExceptionsMessageRegExp = "(?s).*ConstraintViolationException.*")
    public void testValidate(String zipCode) {

        // @formatter:off
        Address expected = builder()
                .city("city")
                .prefecture("prefecture")
                .zipCode(zipCode)
                .build();
        // @formatter:on

        service.create(expected);

        fail("No exception happened!");
    }

    // TODO 重複実行
    @DataProvider(name = "data")
    public Object[][] dataProvider() {

        return new Object[][] { { null }, { "" }, { "  " } };
    }

}
