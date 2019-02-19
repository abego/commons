/*
 * MIT License
 *
 * Copyright (c) 2018 Udo Borkowski, (ub@abego.org)
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

import org.abego.commons.lang.ArrayUtil;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.abego.commons.lang.IterableUtil.hashCodeForIterable;
import static org.abego.commons.seq.SeqHelper.seqsAreEquals;

final class SeqForArray<T> implements SeqNonEmpty<T> {
    static final String ARRAY_MUST_NOT_BE_EMPTY_MESSAGE = "array must not be empty"; //NON-NLS

    private final T[] array;

    private SeqForArray(T[] array) {
        this.array = array;
    }

    static <T> SeqForArray<T> seqForArray(T[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException(ARRAY_MUST_NOT_BE_EMPTY_MESSAGE);
        }
        return new SeqForArray<>(array);
    }

    @Override
    public Iterator<T> iterator() {
        return ArrayUtil.iterator(array);
    }

    @Override
    public long size() {
        return array.length;
    }

    @Override
    public T item(int i) {
        return array[i];
    }

    @Override
    public Stream<T> stream() {
        return Arrays.stream(array);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Seq)) return false;
        return seqsAreEquals(this, (Seq<?>) o);
    }

    @Override
    public int hashCode() {
        return hashCodeForIterable(this);
    }
}
