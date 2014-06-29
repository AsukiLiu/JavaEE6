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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddressServiceIT extends BaseServiceArquillian {

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
                .zipCode("123-1234")
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

        // @formatter:off
        return new Object[][] { 
                { null }, { "" }, { "  " }, 
                {"123-12345"}, {"123-123"}, {"12-1234"}, {"1234-1234"}
        };
        // @formatter:on
    }

}
