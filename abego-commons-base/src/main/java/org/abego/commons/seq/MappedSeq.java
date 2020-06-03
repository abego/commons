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

import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.function.Function;

import static org.abego.commons.seq.SeqUtil.emptySeq;
import static org.abego.commons.util.IteratorUsingAccessor.newIteratorUsingAccessor;

class MappedSeq<T, R> extends AbstractSeq<R> {

    private final Seq<T> originalSeq;
    private final Function<? super T, ? extends R> mapper;
    private final WeakHashMap<T, R> cache = new WeakHashMap<>();

    private MappedSeq(Seq<T> originalSeq, Function<? super T, ? extends R> mapper) {
        this.originalSeq = originalSeq;
        this.mapper = mapper;
    }

    public static <T, R> Seq<R> newMappedSeq(
            Seq<T> originalSeq, Function<? super T, ? extends R> mapper) {
        return new MappedSeq<>(originalSeq, mapper);
    }

    public static <T, R> Seq<R> newMappedSeqOrEmpty(
            @Nullable Seq<T> originalSeq,
            Function<? super T, ? extends R> mapper) {
        return originalSeq == null
                ? emptySeq() : newMappedSeq(originalSeq, mapper);
    }

    @Override
    public Iterator<R> iterator() {
        return newIteratorUsingAccessor(originalSeq.size(), this::item);
    }

    @Override
    public int size() {
        return originalSeq.size();
    }

    @Override
    public R item(int index) {
        T origItem = originalSeq.item(index);
        return cache.computeIfAbsent(origItem, mapper);
    }
}
