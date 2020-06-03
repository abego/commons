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

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.util.List;

import static org.abego.commons.lang.IterableUtil.areEqual;
import static org.abego.commons.seq.SeqForArray.seqForArray;
import static org.abego.commons.seq.SeqForList.seqForList;

final class SeqHelper {
    SeqHelper() {
        throw new MustNotInstantiateException();
    }

    static <T> Seq<T> newSeq(List<T> list) {
        return list.isEmpty() ? Seq.emptySeq() : seqForList(list);
    }

    static <T> Seq<T> newSeq(T[] items) {
        return items.length == 0 ? Seq.emptySeq() : seqForArray(items);
    }

    static <T> Seq<T> emptySeq() {
        return EmptySeqImpl.emptySeqImpl();
    }

    static boolean seqsAreEquals(Seq<?> seq, Seq<?> other) {
        if (seq == other) return true;
        return areEqual(seq, other);
    }

}
