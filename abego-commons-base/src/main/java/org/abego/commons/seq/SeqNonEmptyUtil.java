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

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.util.List;
import java.util.function.Supplier;

import static org.abego.commons.seq.SeqNonEmptyWrapper.newSeqNonEmptyWrapped;

public final class SeqNonEmptyUtil {
    SeqNonEmptyUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Return a {@link Seq} with the items of the given <code>list</code> or
     * throw a {@link IllegalArgumentException} when the list is empty.
     *
     * <p>The list must not change after the Seq is created.</p>
     */
    public static <T> SeqNonEmpty<T> newSeqNonEmpty(List<T> list) {
        return SeqForList.newSeqForList(list);
    }

    /**
     * Return a {@link Seq} with the given <code>items</code> or
     * throw a {@link IllegalArgumentException} when no items are given.
     *
     * <p> Instead of multiple individual items you may also pass a non-empty
     * array of type <code>T[]</code> with <code>items</code>.
     */
    @SafeVarargs
    public static <T> SeqNonEmpty<T> newSeqNonEmpty(T... items) {
        return seqNonEmpty(SeqUtil.newSeq(items));
    }

    /**
     * Return the <code>seq</code> as a {@link SeqNonEmpty} or throw a
     * {@link IllegalArgumentException} when seq is empty.
     */
    public static <T> SeqNonEmpty<T> seqNonEmpty(Seq<T> seq) {
        return seqNonEmptyOrElseThrow(seq, IllegalArgumentException::new);
    }

    /**
     * Return the <code>seq</code> as a {@link SeqNonEmpty} or <code>other</code>
     * when seq is empty.
     */
    public static <T> SeqNonEmpty<T> seqNonEmptyOrElse(Seq<T> seq, SeqNonEmpty<T> other) {
        if (seq instanceof SeqNonEmpty) {
            return (SeqNonEmpty<T>) seq;
        }
        return seq.isEmpty() ? other : newSeqNonEmptyWrapped(seq);
    }

    public static <T> SeqNonEmpty<T> seqNonEmptyOrElseGet(Seq<T> seq, Supplier<SeqNonEmpty<T>> other) {
        if (seq instanceof SeqNonEmpty) {
            return (SeqNonEmpty<T>) seq;
        }
        return seq.isEmpty() ? other.get() : newSeqNonEmptyWrapped(seq);
    }

    public static <T, X extends Throwable> SeqNonEmpty<T> seqNonEmptyOrElseThrow(Seq<T> seq, Supplier<? extends X> exceptionSupplier) throws X {
        if (seq instanceof SeqNonEmpty) {
            return (SeqNonEmpty<T>) seq;
        }
        if (seq.isEmpty()) {
            throw exceptionSupplier.get();
        }
        return newSeqNonEmptyWrapped(seq);
    }
}
