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

import org.eclipse.jdt.annotation.Nullable;

import java.util.function.Function;

import static org.abego.commons.seq.SeqFactories.emptySeq;
import static org.abego.commons.seq.SeqFactories.newSeq;

abstract class MappedSeq<R> extends AbstractSeq<R> {

    protected MappedSeq() {
    }

    public static <T, R> Seq<R> newMappedSeq(
            Seq<T> originalSeq, Function<? super T, ? extends R> mapper) {

        // Currently no using LazyMappedSeq. Seems to be buggy.
        // new LazyMappedSeq<>(originalSeq, mapper);

        return mapDirectly(originalSeq, mapper);
    }

    public static <T, R> Seq<R> newMappedSeqOrEmpty(
            @Nullable Seq<T> originalSeq,
            Function<? super T, ? extends R> mapper) {
        return originalSeq == null
                ? emptySeq() : newMappedSeq(originalSeq, mapper);
    }

    private static <T, R> Seq<R> mapDirectly(
            Seq<T> originalSeq, Function<? super T, ? extends R> mapper) {
        @SuppressWarnings("unchecked")
        R[] arr = (R[]) new Object[originalSeq.size()];
        int i = 0;
        for (T item : originalSeq) {
            arr[i++] = mapper.apply(item);
        }
        return newSeq(arr);
    }

}
