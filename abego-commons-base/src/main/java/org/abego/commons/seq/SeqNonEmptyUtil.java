/*
 * MIT License
 *
 * Copyright (c) 2023 Udo Borkowski, (ub@abego.org)
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
     * Returns a {@link SeqNonEmpty} with the items of the given <code>list</code> or
     * throws an {@link IllegalArgumentException} when the list is empty.
     *
     * <p>The list must not change after the Seq is created.</p>
     *
     * @param list a non-empty {@link List} to generate the {@link SeqNonEmpty} from
     * @param <T>  the type of the items in the {@link List}/{@link SeqNonEmpty}
     * @return a {@link SeqNonEmpty} with te item of {@code list}
     */
    public static <T> SeqNonEmpty<T> newSeqNonEmpty(List<T> list) {
        return SeqForList.newSeqForList(list);
    }

    /**
     * Returns a {{@link SeqNonEmpty} with the given <code>items</code> or
     * throws an {@link IllegalArgumentException} when no items are given.
     *
     * <p> Instead of multiple individual items you may also pass a non-empty
     * array of type <code>T[]</code> with <code>items</code>.
     *
     * @param items the items to include in the {@link SeqNonEmpty}
     * @param <T>   the type of the {@code items}
     * @return a {@link SeqNonEmpty} with the {@code items}
     */
    @SafeVarargs
    public static <T> SeqNonEmpty<T> newSeqNonEmpty(T... items) {
        return seqNonEmpty(SeqFactories.newSeq(items));
    }

    /**
     * Returns the <code>seq</code> as a {@link SeqNonEmpty} or throws an
     * {@link IllegalArgumentException} when seq is empty.
     *
     * @param seq a {@link Seq} with the items to include in the result
     * @param <T> the type of the {@code items}
     * @return a {@link SeqNonEmpty} with the items of {@code seq}
     */
    public static <T> SeqNonEmpty<T> seqNonEmpty(Seq<T> seq) {
        return seqNonEmptyOrElseThrow(seq, IllegalArgumentException::new);
    }

    /**
     * Returns the <code>seq</code> as a {@link SeqNonEmpty} or <code>other</code>
     * when seq is empty.
     *
     * @param seq   a {@link Seq} with the items to include in the result
     * @param other a {@link SeqNonEmpty} to return when {@code seq} is empty
     * @param <T>   the type of the items in {@code seq}, {@code other} and result
     * @return a {@link SeqNonEmpty} with the items of {@code seq},
     * or {@code other} when {@code seq} is empty.
     */
    public static <T> SeqNonEmpty<T> seqNonEmptyOrElse(Seq<T> seq, SeqNonEmpty<T> other) {
        if (seq instanceof SeqNonEmpty) {
            return (SeqNonEmpty<T>) seq;
        }
        return seq.isEmpty() ? other : newSeqNonEmptyWrapped(seq);
    }

    /**
     * Returns the <code>seq</code> as a {@link SeqNonEmpty} or the
     * {@link SeqNonEmpty} supplied by <code>other</code>
     * when seq is empty.
     *
     * @param seq   a {@link Seq} with the items to include in the result
     * @param other a {@link Supplier} of a {@link SeqNonEmpty} to return when
     *              {@code seq} is empty
     * @param <T>   the type of the items in {@code seq}
     * @return a {@link SeqNonEmpty} with the items of {@code seq},
     * or the {@link SeqNonEmpty} supplied by {@code other} when {@code seq} is empty.
     */
    public static <T> SeqNonEmpty<T> seqNonEmptyOrElseGet(Seq<T> seq, Supplier<SeqNonEmpty<T>> other) {
        if (seq instanceof SeqNonEmpty) {
            return (SeqNonEmpty<T>) seq;
        }
        return seq.isEmpty() ? other.get() : newSeqNonEmptyWrapped(seq);
    }

    /**
     * Returns a {{@link SeqNonEmpty} with the items of <code>seq</code> or
     * throws a {@link Throwable} supplied by {@code exceptionSupplier} when
     * <code>seq</code> is empty.
     *
     * @param seq               a {@link Seq} with the items to include in the result
     * @param exceptionSupplier supplies the {@code Throwable} to throw when
     *                          {@code seq} is empty.
     * @param <T>               the type of the {@code items}
     * @param <X>               the type of the {@code Throwable} to throw when {@code seq}
     *                          is empty.
     * @return a {@link SeqNonEmpty} with the {@code seq}
     */
    public static <T, X extends Throwable> SeqNonEmpty<T> seqNonEmptyOrElseThrow(
            Seq<T> seq, Supplier<? extends X> exceptionSupplier) throws X {
        if (seq instanceof SeqNonEmpty) {
            return (SeqNonEmpty<T>) seq;
        }
        if (seq.isEmpty()) {
            throw exceptionSupplier.get();
        }
        return newSeqNonEmptyWrapped(seq);
    }
}
