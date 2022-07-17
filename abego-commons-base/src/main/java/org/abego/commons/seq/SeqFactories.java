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

import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.stream.Stream;

import static org.abego.commons.seq.SeqForArray.newSeqForArray;
import static org.abego.commons.seq.SeqForList.newSeqForList;

final class SeqFactories {
    /**
     * Return a {@link Seq} with the given <code>items</code>.
     *
     * <p> Instead of multiple individual items you may also pass an array of type
     * <code>T[]</code> with <code>items</code>.
     */
    @SafeVarargs
    static <T> Seq<T> newSeq(T... items) {
        return items.length == 0 ? emptySeq() : newSeqForArray(items);
    }

    /**
     * Return a {@link Seq} with the items of the given <code>list</code>.
     *
     * <p>The list must not change after the Seq is created.</p>
     */
    static <T> Seq<T> newSeq(List<T> list) {
        return list.isEmpty() ? emptySeq() : newSeqForList(list);
    }

    @SuppressWarnings("unchecked")
    static <T> Seq<T> newSeq(Stream<T> stream) {
        return newSeq((T @NonNull []) stream.toArray());
    }

    static <T> Seq<T> emptySeq() {
        return EmptySeq.newEmptySeq();
    }
}
