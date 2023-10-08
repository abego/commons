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

import org.eclipse.jdt.annotation.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.abego.commons.util.ListUtil.toList;

@SuppressWarnings("squid:S2160")
// --> 'Subclasses that add fields should override "equals"'
// (No need to override "equals" as AbstractSeq implements "equals" in an abstract way using the iterator)
final class SeqForSet<T> extends AbstractSeq<T> implements SeqNonEmpty<T> {
    @SuppressWarnings("DuplicateStringLiteralInspection")
    static final String SET_MUST_NOT_BE_EMPTY_MESSAGE = "Set must not be empty"; //NON-NLS
    private final Set<T> set;
    @Nullable
    private List<T> itemsAsList;

    private SeqForSet(Set<T> set) {
        this.set = set;
    }

    static <T> SeqForSet<T> newSeqForSet(Set<T> set) {
        if (set.isEmpty()) {
            throw new IllegalArgumentException(SET_MUST_NOT_BE_EMPTY_MESSAGE);
        }
        return new SeqForSet<>(set);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public T item(int i) {
        // The items in a Set have no well-defined order. To allow for random
        // access with the #item(int) method we mirror the set's items in a
        // list, ordering the items as provides by this object's iterator.
        return itemsAsList().get(i);
    }

    @Override
    public Stream<T> stream() {
        return set.stream();
    }

    private List<T> itemsAsList() {
        if (itemsAsList == null) {
            itemsAsList = toList(this);
        }
        return itemsAsList;
    }

}
