package org.asuki.common.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public final class CollectionUtil {

    public static <T> List<T> removeDuplication(List<T> list) {
        return new ArrayList<T>(new HashSet<T>(list));
    }

    public static <T> List<T> removeDuplicationInOrder(List<T> list) {
        return new ArrayList<T>(new LinkedHashSet<T>(list));
    }

    public static <A, B extends Collection<A> & Comparable<B>> boolean compareOrContain(
            B b1, B b2, A a) {

        return (b1.compareTo(b2) == 0) || b1.contains(a) || b2.contains(a);
    }
}
