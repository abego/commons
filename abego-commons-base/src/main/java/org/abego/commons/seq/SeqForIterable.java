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
import java.util.List;

import static org.abego.commons.util.ListUtil.toList;

@SuppressWarnings("squid:S2160")
// --> 'Subclasses that add fields should override "equals"'
// (No need to override "equals" as AbstractSeq implements "equals" in an abstract way using the iterator)
final class SeqForIterable<T> extends AbstractSeq<T> implements SeqNonEmpty<T> {

    private final Iterable<T> iterable;
    @Nullable
    private List<T> list;

    private SeqForIterable(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    static <T> SeqForIterable<T> newSeqForIterable(Iterable<T> iterable) {
        return new SeqForIterable<>(iterable);
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    @Override
    public T first() {
        return iterable.iterator().next();
    }

    @Override
    public int size() {
        return getList().size();
    }

    @Override
    public T item(int i) {
        return getList().get(i);
    }

    private List<T> getList() {
        // this code is a little bit more complicated 
        // than you may expect, but this makes the Nullable checks work
        @Nullable List<T> result = list;
        if (result == null) {
            result = list = toList(iterable);
        }
        return result;
    }

}
