/*
 * MIT License
 *
 * Copyright (c) 2021 Udo Borkowski, (ub@abego.org)
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
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.abego.commons.lang.IterableUtil.isEmpty;
import static org.abego.commons.seq.MappedSeq.newMappedSeq;
import static org.abego.commons.seq.SeqForArray.newSeqForArray;
import static org.abego.commons.seq.SeqForIterable.newSeqForIterable;
import static org.abego.commons.seq.SeqForList.newSeqForList;
import static org.abego.commons.seq.SeqNonEmptyUtil.newSeqNonEmpty;
import static org.abego.commons.util.ListUtil.sortedList;

@SuppressWarnings("WeakerAccess")
public final class SeqUtil {
    SeqUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Return an (/the) empty Seq, i.e. a Seq with no items.
     */
    public static <T> Seq<T> emptySeq() {
        return SeqHelper.emptySeq();
    }

    /**
     * Return true if both {@link Seq}s (<code>seq</code> and <code>other</code>)
     * are equal, false otherwise.
     *
     * <p>The implementation does not use {@link Seq#equals(Object)} but
     * handles the {@link Seq}s as
     * {@link Iterable}s. Therefore, this method is typically used in the
     * {@link Seq#equals(Object)} code of concrete Seq implementations, like here:
     * <pre>
     * public boolean equals(Object o) {
     *     if (!(o instanceof Seq)) return false;
     *
     *     return SeqUtil.seqsAreEqual(this, (Seq&lt;?&gt;) o);
     * }
     * </pre>
     * Typically, a corresponding {@link Seq#hashCode()} implementation then uses
     * {@link org.abego.commons.lang.IterableUtil#hashCodeForIterable(Iterable)}
     * like here:
     *
     * <pre>
     * public int hashCode() {
     *     return hashCodeForIterable(this);
     * }
     * </pre>
     */
    public static boolean seqsAreEqual(Seq<?> seq, Seq<?> other) {
        return SeqHelper.seqsAreEqual(seq, other);
    }

    /**
     * Return a {@link Seq} with the given <code>items</code>.
     *
     * <p> Instead of multiple individual items you may also pass an array of type
     * <code>T[]</code> with <code>items</code>.
     */
    @SafeVarargs
    public static <T> Seq<T> newSeq(T... items) {
        return items.length == 0 ? SeqHelper.emptySeq() : newSeqForArray(items);
    }

    /**
     * Return a {@link Seq} with the given <code>items</code> mapped according
     * to the <code>mapper</code>.
     *
     * <p> Instead of multiple individual items you may also pass an
     * <code>int[]</code> with <code>items</code>.
     */
    public static <T> Seq<T> newSeq(IntFunction<? extends T> mapper, int... items) {
        List<T> result = new ArrayList<>();
        for (int i : items) {
            result.add(mapper.apply(i));
        }
        return newSeq(result);
    }

    /**
     * Return a {@link Seq} with the items of the given <code>list</code>.
     *
     * <p>The list must not change after the Seq is created.</p>
     */
    public static <T> Seq<T> newSeq(List<T> list) {
        return list.isEmpty() ? SeqHelper.emptySeq() : newSeqForList(list);
    }

    /**
     * Return a {@link Seq} for the given <code>iterable</code>.
     *
     * <p>The Iterable must not change after the Seq is created.</p>
     */
    public static <T, S extends T> Seq<T> newSeq(Iterable<S> iterable) {
        if (iterable instanceof List) return newSeq((List<T>) iterable);
        if (iterable instanceof Stream) //noinspection unchecked
            return newSeq((Stream<T>) iterable);

        return isEmpty(iterable) ? SeqHelper.emptySeq() : newSeqForIterable(iterable);
    }

    /**
     * Return a {@link Seq} with the items of the given <code>enumeration</code>.
     */
    public static <T> Seq<T> newSeq(Enumeration<T> enumeration) {
        return newSeq(Collections.list(enumeration));
    }

    /**
     * Return a {@link Seq} for the given <code>iterable</code>.
     *
     * <p>The Iterable must not change after the Seq is created.</p>
     * <p>In contrast to {@link #newSeq(Iterable)} this method will not create
     * a new {@link Seq} when the iterable is a Seq.</p>
     */
    public static <T> Seq<T> toSeq(Iterable<T> iterable) {
        return iterable instanceof Seq ? (Seq<T>) iterable : newSeq(iterable);
    }

    @SuppressWarnings("unchecked")
    public static <T> Seq<T> newSeq(Stream<T> stream) {
        return SeqUtil.newSeq((T @NonNull []) stream.toArray());
    }

    /**
     * Return a {@link Seq} with the given <code>item</code> or an empty Seq
     * if the item is {@code null}.
     */
    public static <T> Seq<T> newSeqOfNullable(@Nullable T item) {
        return item == null ? SeqUtil.emptySeq() : newSeq(item);
    }

    /**
     * Return a new {@link Seq} consisting of the items of <code>seq</code> that match
     * the <code>predicate</code>.
     */
    public static <T> Seq<T> filter(Seq<T> seq, Predicate<T> condition) {
        return newSeq(seq.stream().filter(condition).collect(Collectors.toList()));
    }

    /**
     * Return a new {@link Seq} consisting of the results of applying the given
     * <code>mapper</code> function to the elements of the <code>seq</code>.
     */
    public static <T, R> Seq<R> map(Seq<T> seq, Function<? super T, ? extends R> mapper) {
        return newMappedSeq(seq, mapper);
    }

    /**
     * Return a new {@link Seq} consisting of the elements of <code>iterable</code>
     * sorted by the given <code>sortKey</code>.
     */
    public static <T, S extends Comparable<? super S>> Seq<T> sortedBy(
            Iterable<T> iterable, Function<? super T, ? extends S> sortKey) {

        return newSeq(ListUtil.toSortedList(iterable, Comparator.comparing(sortKey)));
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
        return newSeq(list);
    }

    /**
     * Return a new {@link Seq} consisting of the elements of <code>iterable</code>,
     * sorted in ascending order, according to the order defined by
     * {@link ObjectUtil#compareAsTexts(Object, Object)}.
     */
    public static <T> Seq<T> sortedByText(Iterable<T> iterable) {
        List<T> list = ListUtil.toList(iterable);
        list.sort(ObjectUtil::compareAsTexts);
        return newSeq(list);
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
        return newSeq(list);
    }

    /**
     * Return the items of the <code>iterable</code> in groups of equal items, with the equality defined by the
     * <code>comparator</code>.
     */
    public static <T> Seq<SeqNonEmpty<T>> groupedBy(Iterable<T> iterable, Comparator<T> comparator) {

        List<SeqNonEmpty<T>> result = new ArrayList<>();

        @Nullable
        List<T> currentGroup = null;

        for (T item : sortedList(iterable, comparator)) {
            if (currentGroup == null) {
                currentGroup = ListUtil.list(item);

            } else if (comparator.compare(currentGroup.get(0), item) == 0) {
                currentGroup.add(item);

            } else {
                result.add(newSeqNonEmpty(currentGroup));

                currentGroup = ListUtil.list(item);
            }
        }

        if (currentGroup != null) {
            result.add(newSeqNonEmpty(currentGroup));
        }

        return SeqUtil.newSeq(result);
    }

    /**
     * Returns a sequence of objects of type <code>R</code>, with each object
     * representing the result of merging groups of items of the
     * <code>iterable</code> according to the <code>mergeFunction</code> and
     * the groups defined by the <code>comparator</code>.
     *
     * <p>In detail:
     * <ul>
     *     <li>Group the items of the <code>iterable</code> by the
     *      <code>comparator</code>, i.e. items that are "equal" according to
     *      the comparator are in the same group.</li>
     *      <li>Merge the items of each group to a single object of type
     *      <code>R</code> using the <code>mergeFunction</code>.</li>
     *      <li>Return the sequence of all merged objects, in the order defined
     *      by the comparator</li>
     * </ul>
     */
    public static <T, R> Seq<R> groupedAndMerged(
            Iterable<T> iterable,
            Comparator<T> comparator,
            Function<SeqNonEmpty<T>, R> mergeFunction) {
        return groupedBy(iterable, comparator).map(mergeFunction);
    }

    public static <T> Seq<T> reverse(Seq<T> seq) {
        List<T> result = new ArrayList<>();
        for (int i = seq.size() - 1; i >= 0; i--) {
            result.add(seq.item(i));
        }
        return newSeq(result);
    }

}
