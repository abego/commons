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

import org.abego.commons.lang.IterableUtil;
import org.abego.commons.util.CollectionUtil;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.abego.commons.lang.IterableUtil.hashCodeForIterable;
import static org.abego.commons.seq.SeqUtil.seqsAreEqual;


public final class SeqNonEmptyWithAppendedDefault<T> extends AbstractSeq<T> implements SeqNonEmptyWithAppended<T> {

    private final List<T> sharedList;
    private final int size;


    private SeqNonEmptyWithAppendedDefault(List<T> list) {
        sharedList = list;
        size = list.size();
    }

    private static <T> List<T> toList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T e : iterable) {
            list.add(e);
        }
        return list;
    }

    public static <T> SeqNonEmptyWithAppendedDefault<T> newSeqNonEmptyWithAppendedDefault(SeqNonEmpty<T> seq) {
        return new SeqNonEmptyWithAppendedDefault<>(toList(seq));
    }

    @Override
    @SafeVarargs
    public final SeqNonEmptyWithAppended<T> appended(T... items) {
        // when there are no items to be added there is no need to create 
        // an object, just return this object.
        if (items.length == 0) {
            return this;
        }

        List<T> listToAppend = getListToAppend();
        Collections.addAll(listToAppend, items);
        return new SeqNonEmptyWithAppendedDefault<>(listToAppend);
    }

    private List<T> getListToAppend() {
        // Optimization: when the list was not yet "appended" we can just append
        // the new items at the end of the "sharedList" and share the list with
        // the new Seq. For the old Seq we will just use the first "size"
        // elements. Therefore, we remembered the size in an extra field.
        return (sharedList.size() == size) ? sharedList : new ArrayList<>(getList());
    }

    @Override
    public SeqNonEmptyWithAppended<T> appended(Iterable<T> items) {
        // when there are no items to be added there is no need to create 
        // an object, just return this object.
        if (IterableUtil.isEmpty(items)) {
            return this;
        }

        List<T> listToAppend = getListToAppend();
        CollectionUtil.addAll(listToAppend, items);
        return new SeqNonEmptyWithAppendedDefault<>(listToAppend);
    }

    @Override
    public Iterator<T> iterator() {
        return getList().iterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T item(int i) {
        if (i >= size || i < 0) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", i, size)); //NON-NLS
        }
        return sharedList.get(i);
    }

    @Override
    public Stream<T> stream() {
        return getList().stream();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof Seq)) return false;
        return seqsAreEqual(this, (Seq<?>) o);
    }

    @Override
    public int hashCode() {
        return hashCodeForIterable(this);
    }

    private List<T> getList() {
        return sharedList.size() == size ? sharedList : sharedList.subList(0, size);
    }
}
