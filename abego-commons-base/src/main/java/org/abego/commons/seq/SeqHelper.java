/*
 * MIT License
 *
 * Copyright (c) 2022 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.seq;

import org.abego.commons.lang.ObjectUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.util.ListUtil;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.abego.commons.lang.IterableUtil.areEqual;
import static org.abego.commons.seq.MappedSeq.newMappedSeq;

final class SeqHelper {

    SeqHelper() {
        throw new MustNotInstantiateException();
    }

    static boolean seqsAreEqual(Seq<?> seq, Seq<?> other) {
        if (seq == other) return true;
        return areEqual(seq, other);
    }

    /**
     * Return a new {@link Seq} consisting of the items of <code>seq</code> that match
     * the <code>predicate</code>.
     */
    static <T> Seq<T> filter(Seq<T> seq, Predicate<T> condition) {
        return SeqFactories.newSeq(seq.stream().filter(condition).collect(Collectors.toList()));
    }

    /**
     * Return a new {@link Seq} consisting of the results of applying the given
     * <code>mapper</code> function to the elements of the <code>seq</code>.
     */
    static <T, R> Seq<R> map(Seq<T> seq, Function<? super T, ? extends R> mapper) {
        return newMappedSeq(seq, mapper);
    }

    /**
     * Return a new {@link Seq} consisting of the elements of <code>iterable</code>
     * sorted by the given <code>sortKey</code>.
     */
    static <T, S extends Comparable<? super S>> Seq<T> sortedBy(
            Iterable<T> iterable, Function<? super T, ? extends S> sortKey) {

        return SeqFactories.newSeq(ListUtil.toSortedList(iterable, Comparator.comparing(sortKey)));
    }

    /**
     * Return a new {@link Seq} consisting of the elements of <code>iterable</code>,
     * sorted in ascending order, according to the {@linkplain Comparable
     * natural ordering} of its elements.
     *
     * <p>All elements in the iterable must implement the {@link Comparable}
     * interface.  Furthermore, all elements in the iterable must be
     * <i>mutually comparable</i> (that is, {@code e1.compareTo(e2)}
     * must not throw a {@code ClassCastException} for any elements
     * {@code e1} and {@code e2} in the iterable).
     */
    public static <T> Seq<T> sorted(Iterable<T> iterable) {
        List<T> list = ListUtil.toList(iterable);
        list.sort(null);
        return SeqFactories.newSeq(list);
    }

    /**
     * Return a new {@link Seq} consisting of the elements of <code>iterable</code>,
     * sorted in ascending order, according to the given <code>comparator</code>.
     *
     * <p>All elements in the iterable must be <i>mutually comparable</i> using the
     * specified comparator (that is, {@code c.compare(e1, e2)} must not throw
     * a {@code ClassCastException} for any elements {@code e1} and {@code e2}
     * in the iterable).
     */
    public static <T> Seq<T> sorted(Iterable<T> iterable, Comparator<? super T> comparator) {
        List<T> list = ListUtil.toList(iterable);
        list.sort(comparator);
        return SeqFactories.newSeq(list);
    }

    /**
     * Return a new {@link Seq} consisting of the elements of <code>iterable</code>,
     * sorted in ascending order, according to the order defined by
     * {@link ObjectUtil#compareAsTexts(Object, Object)}.
     */
    public static <T> Seq<T> sortedByText(Iterable<T> iterable) {
        List<T> list = ListUtil.toList(iterable);
        list.sort(ObjectUtil::compareAsTexts);
        return SeqFactories.newSeq(list);
    }
}
