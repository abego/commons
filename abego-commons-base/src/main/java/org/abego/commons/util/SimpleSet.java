/*
 * MIT License
 *
 * Copyright (c) 2021 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.util;

import org.abego.commons.seq.Seq;
import org.eclipse.jdt.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import static org.abego.commons.seq.SeqUtil.newSeq;

public class SimpleSet<S> {
    private final Set<S> set = new HashSet<>();
    private @Nullable Seq<S> seq;

    private SimpleSet() {
    }

    public static <T> SimpleSet<T> newSimpleSet() {
        return new SimpleSet<>();
    }

    public boolean add(S item) {
        if (set.add(item)) {
            seq = null; // ensure the Seq is recalculated, if needed
            return true;
        }
        return false;
    }

    public boolean remove(S item) {
        if (set.remove(item)) {
            seq = null; // ensure the Seq is recalculated, if needed
            return true;
        }
        return false;
    }

    public boolean contains(S item) {
        return set.contains(item);
    }

    public Seq<S> asSeq() {
        @Nullable Seq<S> result = seq;
        if (result != null) {
            return result;
        }

        Seq<S> newSeq = newSeq(set);
        seq = newSeq;
        return newSeq;
    }
}
