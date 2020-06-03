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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

    /**
     * Return a {@link Seq} with the items of the given <code>list</code>.
     *
     * <p>The list must not change after the Seq is created.</p>
     */
    static <T> Seq<T> newSeq(List<T> list) {
        return SeqHelper.newSeq(list);
    }

    /**
     * Return a {@link Seq} with the given <code>items</code>.
     *
     * <p> Instead of multiple individual items you may also pass an array of type
     * <code>T[]</code> with <code>items</code>.
     */
    @SafeVarargs
    static <T> Seq<T> newSeq(T... items) {
        return SeqHelper.newSeq(items);
    }

    /**
     * Return an (/the) empty Seq, i.e. a Seq with no items.
     */
    static <T> Seq<T> emptySeq() {
        return SeqHelper.emptySeq();
    }

    /**
     * Return the size of the sequence.
     */
    long size();

    /**
     * Return the <code>index</code>-ed item in the sequence.
     *
     * <p><code>index</code> is zero-based.</p>
     */
    T item(int index);

    /**
     * Return the items of the sequence as a {@link Stream}.
     */
    Stream<T> stream();

    /**
     * Return <code>true</code> when the sequence is empty,
     * <code>false</code> otherwise.
     */
    default boolean isEmpty() {
        return size() == 0;
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
     * Returns a {@link Seq} consisting of the items of this sequence that match
     * the <code>predicate</code>.
     */
    default Seq<T> filter(Predicate<T> condition) {
        return newSeq(stream().filter(condition).collect(Collectors.toList()));
    }

    /**
     * Return the one and only item of this sequence.
     * <p>
     * Throw a {@link NoSuchElementException} when the sequence is empty or
     * contains more than one item.
     */
    default T singleItem() {
        if (!hasSingleItem()) {
            throw new NoSuchElementException();
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
    default T singleItemOrNull() {
        if (!hasSingleItem()) {
            if (isEmpty()) {
                return null;
            }
            throw new NoSuchElementException();
        }
        return first();
    }

    /**
     * Return any item of this sequence, or <code>null</code> when
     * the sequence is empty.
     */
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

    /**
     * Return <code>true</code> if the specified <code>object</code> is a
     * {@link Seq} and equal to this Seq, return <code>false</code> otherwise.
     *
     * <p>Two Seqs are equal if they contain the same elements in the same
     * order. Two elements <code>e1</code> and <code>e2</code> are the same
     * elements if <code>(e1==null ? e2==null : e1.equals(e2))</code>.
     *
     * <p>This definition ensures that the equals method works properly
     * across different implementations of the Seq interface.
     */
    boolean equals(Object object);

    /**
     * Return the hash code value for this {@link Seq}.
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
