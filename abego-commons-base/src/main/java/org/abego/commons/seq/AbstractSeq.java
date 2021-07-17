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

import org.eclipse.jdt.annotation.Nullable;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.abego.commons.lang.IterableUtil.hashCodeForIterable;
import static org.abego.commons.lang.IterableUtil.toStringOfIterable;

public abstract class AbstractSeq<T> implements Seq<T> {

    @Override
    public Seq<T> filter(Predicate<T> condition) {
        return SeqUtil.filter(this, condition);
    }

    @Override
    public <R> Seq<R> map(Function<? super T, ? extends R> mapper) {
        return SeqUtil.map(this, mapper);
    }

    @Override
    public <S extends Comparable<S>> Seq<T> sortedBy(Function<T, S> sortKey) {
        return SeqUtil.sortedBy(this, sortKey);
    }

    @Override
    public Seq<T> sorted() {
        return SeqUtil.sorted(this);
    }

    @Override
    public Seq<T> sorted(Comparator<? super T> comparator) {
        return SeqUtil.sorted(this, comparator);
    }

    @Override
    public Seq<T> sortedByText() {
        return SeqUtil.sortedByText(this);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof Seq)) return false;
        return SeqHelper.seqsAreEqual(this, (Seq<?>) o);
    }

    @Override
    public int hashCode() {
        return hashCodeForIterable(this);
    }

    /**
     * Return this Seq as a String starting with the class name and followed
     * by a <tt>", "</tt> (comma and space) separated list of the items of the
     * Seq in brackets (`[...]`), each item converted using {@link String#valueOf(Object)}.
     */
    @Override
    public String toString() {
        return toStringOfIterable(this);
    }
}
