/*
 * MIT License
 *
 * Copyright (c) 2020 Udo Borkowski, (ub@abego.org)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.abego.commons.util;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.abego.commons.lang.StringUtil.stringOrNull;


@SuppressWarnings("WeakerAccess")
public final class ListUtil {

    ListUtil() {
        throw new MustNotInstantiateException();
    }

    // --- Factories ---

    @SafeVarargs
    public static <C> List<C> list(C... array) {
        return toList(array);
    }

    @SafeVarargs
    public static <C> List<C> toList(C... array) {
        ArrayList<C> result = new ArrayList<>();
        Collections.addAll(result, array);
        return result;
    }

    public static <C> List<C> toList(Iterable<C> iterable) {
        ArrayList<C> result = new ArrayList<>();
        for (C i : iterable) {
            result.add(i);
        }
        return result;
    }

    public static <T extends Comparable<? super T>> List<T> toSortedList(Iterable<T> iterable) {
        List<T> result = toList(iterable);
        Collections.sort(result);
        return result;
    }

    public static <T extends Comparable<? super T>> List<T> sortedList(Iterable<T> iterable) {
        return toSortedList(iterable);
    }

    public static <T> List<T> toSortedList(Iterable<T> iterable, Comparator<? super T> comparator) {
        List<T> result = toList(iterable);
        result.sort(comparator);
        return result;
    }

    public static <T> List<T> sortedList(Iterable<T> iterable, Comparator<? super T> comparator) {
        return toSortedList(iterable, comparator);
    }

    public static <T, C extends Comparable<? super C>> List<T> toSortedList(Iterable<T> iterable, Function<? super T, C> sortKey) {
        return toSortedList(iterable, Comparator.comparing(sortKey));
    }

    public static <T, C extends Comparable<? super C>> List<T> sortedList(Iterable<T> iterable, Function<? super T, C> sortKey) {
        return toSortedList(iterable, sortKey);
    }

    public static <T, M> List<M> mappedList(Iterable<T> iterable, Function<T, M> mapper) {
        List<M> result = new ArrayList<>();
        for (T i : iterable) {
            result.add(mapper.apply(i));
        }
        return result;
    }

    public static <T, M> List<M> toListWithMapping(Iterable<T> iterable, Function<T, M> mapper) {
        return mappedList(iterable, mapper);
    }

    public static <T, M> List<M> map(Iterable<T> iterable, Function<T, M> mapper) {
        return toListWithMapping(iterable, mapper);
    }

    // --- Queries ---

    @Nullable
    public static String nthItemAsStringOrNull(List<?> list, int i) {
        @Nullable Object item = i >= 0 && i < list.size() ? list.get(i) : null;
        return stringOrNull(item);
    }
}
