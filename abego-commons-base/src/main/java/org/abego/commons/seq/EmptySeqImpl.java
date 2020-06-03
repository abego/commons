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

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

final class EmptySeqImpl<T> implements Seq<T> {

    private static final EmptySeqImpl<?> instance = new EmptySeqImpl<>();

    private EmptySeqImpl() {
    }

    @SuppressWarnings("unchecked")
    static <T> Seq<T> emptySeqImpl() {
        return (Seq<T>) instance;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public T item(int index) {
        throw new NoSuchElementException();
    }

    @Override
    public Stream<T> stream() {
        return Stream.empty();
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    // no need to overwrite equals/hashcode: there is only one EmptySeq
    // instance and every other object is different from it. So the default
    // implementation is fine.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seq)) return false;
        return ((Seq<?>) o).isEmpty();
    }

    @Override
    public int hashCode() {
        return 1;
    }

}
