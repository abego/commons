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

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("squid:S2160")
// --> 'Subclasses that add fields should override "equals"'
// (No need to override "equals" as AbstractSeq implements "equals" in an abstract way using the iterator)
final class SeqForList<T> extends AbstractSeq<T> implements SeqNonEmpty<T> {

    static final String LIST_MUST_NOT_BE_EMPTY_MESSAGE = "list must not be empty"; //NON-NLS
    private final List<T> list;

    private SeqForList(List<T> list) {
        this.list = list;
    }

    static <T> SeqForList<T> newSeqForList(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException(LIST_MUST_NOT_BE_EMPTY_MESSAGE);
        }
        return new SeqForList<>(list);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public T item(int i) {
        return list.get(i);
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

}
