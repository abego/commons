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

package org.abego.commons.seq;

import org.eclipse.jdt.annotation.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A sequence of items of type T.
 * <p>
 * A Seq is a way to abstract from implementation details of "classic"
 * data types representing sequences, such as {@link List}s or arrays.
 * <p>
 * In contrast to an {@link Iterable} or a {@link Stream} a Seq also provides
 * the number of items in the sequence ({@link #size()}) as well as indexed
 * access to the items ({@link #item(int)}). Also Seq introduces the concept of
 * a "single item" sequence.
 */
public interface Seq<T> extends Iterable<T> {

    String MORE_THAN_ONE_ELEMENT_MESSAGE = "More than one element"; //NON-NLS
    String NO_SUCH_ELEMENT_MESSAGE = "No such element"; //NON-NLS

    /**
     * Return the size of the sequence.
     */
    int size();

    /**
     * Return the <code>index</code>-ed item in the sequence.
     *
     * <p><code>index</code> is zero-based.</p>
     */
    T item(int index);

    /**
     * Return the index of the first occurrence of the specified item in this
     * Seq, or -1 if this Seq does not contain the item.
     * <p>
     * {@link Objects#equals(Object, Object)} is used to check for the occurrence.
     */
    default int indexOf(@Nullable T item) {
        int i = 0;
        for (T o : this) {
            if ((Objects.equals(item, o)))
                return i;
            i++;
        }
        return -1;
    }

    /**
     * Return {@code true} when the Seq contains the specified item,
     * {@code false} otherwise.
     * <p>
     * {@link Objects#equals(Object, Object)} is used to check for the occurrence.
     */
    default boolean contains(@Nullable T item) {
        return indexOf(item) >= 0;
    }

    /**
     * Return the items of the sequence as a {@link Stream}.
     */
    default Stream<T> stream() {
        Stream.Builder<T> builder = Stream.builder();
        for (T o : this) {
            builder.add(o);
        }
        return builder.build();
    }

    /**
     * Return <code>true</code> when the sequence is empty,
     * <code>false</code> otherwise.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Return <code>true</code> when the sequence has one or more items,
     * <code>false</code> otherwise.
     * <p>
     * It is equivalent to {@code !isEmpty()}.
     * </p>
     */
    default boolean hasItems() {
        return !isEmpty();
    }

    /**
     * Return <code>true</code> when the sequence has exactly one item,
     * <code>false</code> otherwise.
     */
    default boolean hasSingleItem() {
        return size() == 1;
    }

    /**
     * Return the first item of the sequence.
     *
     * <p>Throw an {@link NoSuchElementException} when the sequence is empty.</p>
     */
    default T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return item(0);
    }

    /**
     * Returns a new Seq consisting of the items of this sequence that match
     * the <code>predicate</code>.
     *
     * <p>See {@link SeqUtil#filter(Seq, Predicate)} for a default implementation.</p>
     *
     * <p>This method has no default implementation to avoid cyclic dependencies.</p>
     */
    Seq<T> filter(Predicate<T> condition);

    /**
     * Returns a new Seq consisting of the results of applying the given
     * <code>mapper</code> function to the elements of this Seq.
     *
     * <p>See {@link SeqUtil#map(Seq, Function)} for a default implementation.</p>
     *
     * <p>This method has no default implementation to avoid cyclic dependencies.</p>
     */
    <R> Seq<R> map(Function<? super T, ? extends R> mapper);

    /**
     * Returns a new Seq consisting of the elements of this Seq
     * sorted by the given <code>sortKey</code>.
     */
    <S extends Comparable<S>> Seq<T> sortedBy(Function<T, S> sortKey);

    /**
     * Return a new Seq consisting of the elements of this Seq
     * sorted in ascending order, according to the {@linkplain Comparable
     * natural ordering} of its elements.
     *
     * <p>All elements in the Seq must implement the {@link Comparable}
     * interface.  Furthermore, all elements in the Seq must be
     * <i>mutually comparable</i> (that is, {@code e1.compareTo(e2)}
     * must not throw a {@code ClassCastException} for any elements
     * {@code e1} and {@code e2} in the Seq).
     */
    Seq<T> sorted();

    /**
     * Return a new Seq consisting of the elements of this Seq
     * sorted in ascending order, according to the given <code>comparator</code>.
     *
     * <p>All elements in this list must be <i>mutually comparable</i> using the
     * specified comparator (that is, {@code c.compare(e1, e2)} must not throw
     * a {@code ClassCastException} for any elements {@code e1} and {@code e2}
     * in the list).
     */
    Seq<T> sorted(Comparator<? super T> comparator);

    /**
     * Return the one and only item of this sequence.
     * <p>
     * Throw a {@link NoSuchElementException} when the sequence is empty or
     * contains more than one item.
     */
    default T singleItem() {
        if (!hasSingleItem()) {
            throw new NoSuchElementException(size() > 1 ? MORE_THAN_ONE_ELEMENT_MESSAGE : NO_SUCH_ELEMENT_MESSAGE);
        }
        return first();
    }

    /**
     * Return the one and only item of this sequence, or <code>null</code> when
     * the sequence is empty.
     * <p>
     * Throw a {@link NoSuchElementException} when the sequence contains more
     * than one item.
     */
    @Nullable
    default T singleItemOrNull() {
        if (!hasSingleItem()) {
            if (isEmpty()) {
                return null;
            }
            throw new NoSuchElementException(MORE_THAN_ONE_ELEMENT_MESSAGE);
        }
        return first();
    }

    /**
     * Return any item of this sequence, or <code>null</code> when
     * the sequence is empty.
     */
    @Nullable
    default T anyItemOrNull() {
        return isEmpty() ? null : anyItem();
    }

    /**
     * Return any item of this sequence.
     * <p>
     * Throw a {@link NoSuchElementException} when the sequence is empty.
     * <p>
     * It is not required to always return the first item.
     */
    default T anyItem() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return first();
    }

    default String joined(CharSequence separator, Function<T, String> mapToString) {
        return String.join(separator, map(mapToString));
    }

    default String joined(CharSequence separator) {
        return joined(separator, Object::toString);
    }

    default String joined() {
        return joined("");
    }

    /**
     * Return <code>true</code> if the specified <code>object</code> is a
     * Seq and equal to this Seq, return <code>false</code> otherwise.
     *
     * <p>Two Seqs are equal if they contain the same elements in the same
     * order. Two elements <code>e1</code> and <code>e2</code> are the same
     * elements if <code>(e1==null ? e2==null : e1.equals(e2))</code>.
     *
     * <p>This definition ensures that the equals method works properly
     * across different implementations of the Seq interface.
     */
    boolean equals(@Nullable Object object);

    /**
     * Return the hash code value for this Seq.
     *
     * <p>The hash code of a Seq is defined to be the result of the
     * following calculation:
     * <pre>
     *         int hashCode = 1;
     *         for (Object e : this) {
     *             hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
     *         }
     * </pre>
     * <p>
     * This ensures that seq1.equals(seq2) implies that
     * seq1.hashCode()==seq2.hashCode() for any two Seqs, seq1 and seq2,
     * as required by the general contract of Object.hashCode().
     */
    int hashCode();
}
