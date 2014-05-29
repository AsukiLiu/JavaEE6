package org.asuki.common.guava;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.or;
import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.base.Strings.padEnd;
import static com.google.common.base.Strings.padStart;
import static com.google.common.base.Strings.repeat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.partition;
import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.union;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.testng.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.testng.annotations.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Defaults;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.Constraints;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.Flushables;
import com.google.common.io.Resources;
import com.google.common.net.InetAddresses;
import com.google.common.primitives.Ints;

public class GuavaTest {

    private Customer customer1 = new Customer(1, "Name1");
    private Customer customer2 = new Customer(2, "Name2");
    private Customer customer3 = new Customer(3, "Name3");
    private Customer customer4 = new Customer(null, "Unknown");

    /* Base */

    @Test
    public void testOptional() {

        Optional<Integer> possible = Optional.of(3);

        if (possible.isPresent()) {
            assertThat(possible.get(), is(3));
        }

        assertThat(possible.or(10), is(3));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testPreconditions() {

        checkNotNull(customer2.getId());
        checkState(!customer2.isSick());
        checkArgument(customer2.getAddress() != null,
                "Not found the address of customer[id:%s]", customer2.getId());

        fail("No exception happened!");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testObjects() {

        Object[] customers = new Object[] { customer1, customer2 };

        assertEquals(Arrays.hashCode(customers),
                Objects.hashCode(customer1, customer2));

        assertThat(Objects.equal("a", null), is(false));
        assertThat(Objects.equal(null, null), is(true));

        Integer a = Objects.firstNonNull(null, 3);
        Integer b = Objects.firstNonNull(9, 3);

        assertThat(Integer.valueOf(3), is(a));
        assertThat(Integer.valueOf(9), is(b));

        Objects.firstNonNull(null, null);

        fail("No exception happened!");
    }

    @Test
    public void testStrings() {

        assertNull(emptyToNull(""));
        assertEquals(nullToEmpty(null), "");
        assertTrue(isNullOrEmpty(""));

        assertEquals(repeat("*", 10), "**********");
        assertEquals(padStart("7", 2, '0'), "07");
        assertEquals(padEnd("7", 2, '0'), "70");

        String str1 = LOWER_CAMEL.to(LOWER_UNDERSCORE, "someString");
        assertEquals(str1, "some_string");

        String str2 = LOWER_UNDERSCORE.to(LOWER_CAMEL, "some_string");
        assertEquals(str2, "someString");
    }

    @Test
    public void testJoinerAndSplitter() {

        ImmutableSet<String> strings = ImmutableSet.of("A", "B", "C");
        String joined = Joiner.on(":").join(strings);
        assertThat("A:B:C", is(joined));

        final String string = ": A::: B : C :::";

        // String[] parts = string.split(":");
        Iterable<String> parts = Splitter.on(":").omitEmptyStrings()
                .trimResults().split(string);

        joined = Joiner.on(":").join(parts);
        assertThat("A:B:C", is(joined));
    }

    @Test
    public void testCharMatcher() {

        Charset utf8 = Charsets.UTF_8;
        assertTrue(utf8.canEncode());

        CharMatcher matcher = CharMatcher.anyOf("andy");
        assertTrue(matcher.matches('a'));

        ImmutableList<Character> someChars = ImmutableList.of('a', 'b', 'c',
                'd', 'e');
        Collection<Character> filter = Collections2.filter(someChars, matcher);

        ImmutableList<Character> result = ImmutableList.of('a', 'd');

        assertThat(toCharArray(filter), is(toCharArray(result)));
    }

    private static Character[] toCharArray(Collection<Character> filter) {
        return filter.toArray(new Character[0]);
    }

    @Test
    public void testPrimitives() {

        Integer defaultValue = Defaults.defaultValue(int.class);
        assertEquals(defaultValue.intValue(), 0);

        int[] array = new int[] { 1, 2, 3 };

        // Array⇒List
        List<Integer> list = Ints.asList(array);
        assertEquals(list.toString(), "[1, 2, 3]");

        assertEquals(Ints.max(array), 3);
        assertEquals(Ints.join(" : ", array), "1 : 2 : 3");
    }

    /* Collect */

    @Test
    public void testLists() {

        List<Integer> items = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        List<List<Integer>> pages = partition(items, 3);

        assertEquals(pages.toString(), "[[1, 2, 3], [4, 5, 6], [7]]");
    }

    @Test
    public void testSets() {

        ImmutableSet<Customer> customersA = ImmutableSet.of(customer1,
                customer2, customer3);
        ImmutableSet<Customer> customersB = ImmutableSet.of(customer3,
                customer4);

        assertEquals(union(customersA, customersB).size(), 4);
        assertEquals(intersection(customersA, customersB),
                ImmutableSet.of(customer3));

        List<String> list = newArrayList("aa", "bb", "cc", "cc");
        Set<String> set = newHashSet(list);
        assertEquals(set.toString(), "[aa, bb, cc]");

        Set<Integer> a = newHashSet(Arrays.asList(1, 2, 3));
        Set<Integer> b = newHashSet(Arrays.asList(3, 4, 5));

        assertEquals(intersection(a, b).toString(), "[3]");
        assertEquals(difference(a, b).toString(), "[1, 2]");
        assertEquals(union(a, b).toString(), "[1, 2, 3, 4, 5]");
    }

    @Test
    public void testMaps() {

        SortedMap<String, String> map = new TreeMap<>();
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", null);
        map.put("4", "four");

        SortedMap<String, String> filtered = Maps.filterValues(map,
                Predicates.notNull());

        assertThat(filtered.size(), is(3));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstraints() {

        HashSet<Customer> customers = newHashSet();
        customers.add(null);

        Set<Customer> noMoreNulls = Constraints.constrainedSet(customers,
                Constraints.notNull());
        noMoreNulls.add(null);

        fail("No exception happened!");
    }

    /* IO */

    @Test
    public void testFiles() {

        final String fileName = "test.txt";
        final File file = new File(fileName);
        final String content = "This is test!";

        try {
            Files.touch(file);

            Files.write(content, file, UTF_8);

            Files.toByteArray(file);
            Files.newInputStreamSupplier(file);

            assertEquals(Files.readFirstLine(file, UTF_8), content);
            assertEquals(Files.toString(file, UTF_8), content);

            java.nio.file.Files.deleteIfExists(Paths.get(fileName));

        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    @Test
    public void testIO() {

        InputStream inputStream = System.in;

        // JDK way
        try {
            inputStream.close();
        } catch (IOException e) {
            Throwables.propagate(e);
        }

        Closeables.closeQuietly(inputStream);

        PrintStream outputStream = System.out;
        Flushables.flushQuietly(outputStream);
    }

    @Test
    public void testResources() {

        final String location = "org/asuki/common/guava/GuavaTest.class";

        URL guavaWay = Resources.getResource(location);
        checkArgument(guavaWay != null, "Resource[%s] not found", location);

        URL jdkWay = getClass().getClassLoader().getResource(location);
        checkArgument(jdkWay != null, "Resource[%s] not found", location);

        assertEquals(guavaWay, jdkWay);
    }

    /* Net */

    @Test
    public void testAddresses() {

        try {
            assertEquals(InetAddresses.forString("0.0.0.0"),
                    InetAddress.getByName("0.0.0.0"));
        } catch (UnknownHostException e) {
            Throwables.propagate(e);
        }
    }

    /* Functional idioms */

    @Test
    public void testFunction() {

        Function<Customer, Boolean> isOddId = new Function<Customer, Boolean>() {

            public Boolean apply(Customer customer) {
                return customer.getId().intValue() % 2 != 0;
            }
        };

        assertTrue(isOddId.apply(customer1));
        assertFalse(isOddId.apply(customer2));
    }

    @Test
    public void testPredicate() {

        Predicate<Customer> isOddId = new Predicate<Customer>() {

            public boolean apply(Customer customer) {
                return customer.getId().intValue() % 2 != 0;
            }
        };

        assertTrue(isOddId.apply(customer1));
        assertFalse(isOddId.apply(customer2));
    }

    @Test
    public void testFunctions() {

        final String toString = "Customer{name=Name1, id=1}";

        assertEquals(customer1.toString(), toString);

        Function<Object, String> toStringFunction = Functions
                .toStringFunction();

        assertEquals(toStringFunction.apply(customer1), toString);
    }

    @Test
    public void testPredicates() {

        Predicate<Customer> isCustomer1 = equalTo(customer1);
        Predicate<Customer> isCustomer2 = equalTo(customer2);
        Predicate<Customer> isCustomer1OrCustomer2 = or(isCustomer1,
                isCustomer2);

        ImmutableSet<Customer> customers = ImmutableSet.of(customer1,
                customer2, customer3);

        Iterable<Customer> filtered = Iterables.filter(customers,
                isCustomer1OrCustomer2);

        assertEquals(ImmutableSet.copyOf(filtered).size(), 2);
    }

    @Test
    public void testSuppliers() {

        Function<Ingredients, Cake> bakeProcess = new Function<Ingredients, Cake>() {
            public Cake apply(Ingredients ingredients) {
                return new Cake(ingredients);
            }
        };

        IngredientsFactory ingredientsFactory = new IngredientsFactory();

        Supplier<Cake> cakeFactory = Suppliers.compose(bakeProcess,
                ingredientsFactory);
        cakeFactory.get();
        cakeFactory.get();
        cakeFactory.get();

        assertEquals(ingredientsFactory.getUsedNumber(), 3);

        Supplier<Ingredients> factory1 = new IngredientsFactory();
        Supplier<Ingredients> factory2 = new IngredientsFactory();

        ImmutableList<Supplier<Ingredients>> factories = ImmutableList.of(
                factory1, factory2);

        Function<Supplier<Ingredients>, Ingredients> supplierFunction = Suppliers
                .supplierFunction();

        Collection<Ingredients> ingredients = Collections2.transform(factories,
                supplierFunction);

        assertEquals(ingredients.size(), 2);
    }

}

class Ingredients {
}

class Cake {
    Cake(Ingredients ingredients) {
    }
}

class IngredientsFactory implements Supplier<Ingredients> {

    private int counter;

    public Ingredients get() {
        counter++;
        return new Ingredients();
    }

    int getUsedNumber() {
        return counter;
    }

}
