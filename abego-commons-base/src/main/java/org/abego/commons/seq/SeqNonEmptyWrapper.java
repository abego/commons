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
import java.util.stream.Stream;

@SuppressWarnings("squid:S2160")
// --> 'Subclasses that add fields should override "equals"'
// (No need to override "equals" as AbstractSeq implements "equals" in an abstract way using the iterator)
final class SeqNonEmptyWrapper<T> extends AbstractSeq<T> implements SeqNonEmpty<T> {
    static final String SEQ_MUST_NOT_BE_EMPTY_MESSAGE = "seq must not be empty"; //NON-NLS
    private final Seq<T> seq;

    private SeqNonEmptyWrapper(Seq<T> seq) {
        if (seq.isEmpty()) {
            throw new IllegalArgumentException(SEQ_MUST_NOT_BE_EMPTY_MESSAGE);
        }
        this.seq = seq;
    }

    static <T> SeqNonEmptyWrapper<T> newSeqNonEmptyWrapped(Seq<T> seq) {
        return new SeqNonEmptyWrapper<>(seq);
    }

    @Override
    public int size() {
        return seq.size();
    }

    @Override
    public T item(int index) {
        return seq.item(index);
    }

    @Override
    public Stream<T> stream() {
        return seq.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return seq.iterator();
    }

}
