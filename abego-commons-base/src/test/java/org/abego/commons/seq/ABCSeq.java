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

import static org.abego.commons.lang.IterableUtil.areEqual;
import static org.abego.commons.lang.IterableUtil.hashCodeForIterable;

/**
 * A sequence with the strings "A","B","C".
 *
 * <p>This class is used in tests that check the behaviour of non-empty
 * sequences implemented in types that don't implement the SeqNonEmpty
 * interface. Currently this is handled by SeqNonEmptyWrapper.
 * </p>
 */
final class ABCSeq implements Seq<String> {
    private final String[] array = new String[]{"A", "B", "C"};

    @Override
    public long size() {
        return array.length;
    }

    @Override
    public String item(int index) {
        return array[index];
    }

    @Override
    public Stream<String> stream() {
        return Arrays.stream(array);
    }

    @Override
    public Iterator<String> iterator() {
        return ArrayUtil.iterator(array);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seq)) return false;
        Seq<?> that = (Seq<?>) o;
        return areEqual(this, that);
    }

    @Override
    public int hashCode() {
        return hashCodeForIterable(this);
    }

}
