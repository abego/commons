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

import org.abego.commons.lang.IterableUtil;
import org.abego.commons.util.ListUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.abego.commons.seq.SeqForArray.newSeqForArray;
import static org.abego.commons.seq.SeqNonEmptyWithAppendedDefault.newSeqNonEmptyWithAppendedDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeqNonEmptyWithAppendedDefaultTest extends AbstractSeqTest {


    @Override
    Seq<String> singleItemSeq() {
        return newSeqNonEmptyWithAppendedDefault(newSeqForArray(SINGLE_ITEM_ARRAY));
    }

    @Override
    Seq<String> helloSeq() {
        return newSeqNonEmptyWithAppendedDefault(newSeqForArray(HELLO_ARRAY));
    }

    @Test
    void appended_Array() {
        SeqNonEmptyWithAppendedDefault<String> seq = newSeqNonEmptyWithAppendedDefault(newSeqForArray(HELLO_ARRAY));
        SeqNonEmptyWithAppended<String> seq2 = seq.appended(" world", "!");
        SeqNonEmptyWithAppended<String> seq3 = seq.appended(" Dolly", "!");
        SeqNonEmptyWithAppended<String> seq4 = seq2.appended(); // nothing appended
        SeqNonEmptyWithAppended<String> seq5 = seq4.appended(new HashSet<>()); // nothing appended, as Iterable
        SeqNonEmptyWithAppended<String> seq6 = seq5.appended(" (", "again", ")");

        assertEquals("hello", IterableUtil.textOf(seq));
        assertEquals("hello world!", IterableUtil.textOf(seq2));
        assertEquals("hello Dolly!", IterableUtil.textOf(seq3));
        assertEquals("hello world!", IterableUtil.textOf(seq4));
        assertEquals("hello world!", IterableUtil.textOf(seq5));
        assertEquals("hello world! (again)", IterableUtil.textOf(seq6));
    }

    @Test
    void appended_List() {
        SeqNonEmptyWithAppendedDefault<String> seq = newSeqNonEmptyWithAppendedDefault(newSeqForArray(HELLO_ARRAY));
        SeqNonEmptyWithAppended<String> seq2 = seq.appended(ListUtil.toList(" world", "!"));
        SeqNonEmptyWithAppended<String> seq3 = seq.appended(ListUtil.toList(" Dolly", "!"));
        SeqNonEmptyWithAppended<String> seq4 = seq2.appended(ListUtil.toList(" (", "again", ")"));

        assertEquals("hello", IterableUtil.textOf(seq));
        assertEquals("hello world!", IterableUtil.textOf(seq2));
        assertEquals("hello Dolly!", IterableUtil.textOf(seq3));
        assertEquals("hello world! (again)", IterableUtil.textOf(seq4));
    }

    @Test
    void item_OK() {
        SeqNonEmptyWithAppendedDefault<String> seq = newSeqNonEmptyWithAppendedDefault(newSeqForArray(HELLO_ARRAY));
        SeqNonEmptyWithAppended<String> otherSeq = seq.appended(HELLO_ARRAY);

        // First the "original" seq ...

        // ... valid index
        assertEquals("h", seq.item(0));
        assertEquals("e", seq.item(1));
        assertEquals("l", seq.item(2));
        assertEquals("l", seq.item(3));
        assertEquals("o", seq.item(4));

        // ... invalid index
        IndexOutOfBoundsException e = assertThrows(IndexOutOfBoundsException.class, () -> seq.item(10));
        assertEquals("Index: 10, Size: 5", e.getMessage());

        // ...  boundaries
        e = assertThrows(IndexOutOfBoundsException.class, () -> seq.item(5));
        assertEquals("Index: 5, Size: 5", e.getMessage());

        e = assertThrows(IndexOutOfBoundsException.class, () -> seq.item(-1));
        assertEquals("Index: -1, Size: 5", e.getMessage());

        // Now the "other", longer seq ...
        // (This is an important test case, as in the current implementation
        // both Seq objects (`seq` and `otherSeq`) share the same ArrayList
        // holding 10 items. In the `seq` of size 5 only the first five items
        // can be accessed. Bugs in the "size" handling in the implementation
        // may become visible in these tests.)

        // ... valid index
        assertEquals("h", otherSeq.item(0));
        assertEquals("e", otherSeq.item(1));
        assertEquals("l", otherSeq.item(2));
        assertEquals("l", otherSeq.item(3));
        assertEquals("o", otherSeq.item(4));
        assertEquals("h", otherSeq.item(5));
        assertEquals("e", otherSeq.item(6));
        assertEquals("l", otherSeq.item(7));
        assertEquals("l", otherSeq.item(8));
        assertEquals("o", otherSeq.item(9));

        // ... invalid index
        e = assertThrows(IndexOutOfBoundsException.class, () -> otherSeq.item(15));
        assertEquals("Index: 15, Size: 10", e.getMessage());

        // ...  boundaries
        e = assertThrows(IndexOutOfBoundsException.class, () -> otherSeq.item(10));
        assertEquals("Index: 10, Size: 10", e.getMessage());

        e = assertThrows(IndexOutOfBoundsException.class, () -> otherSeq.item(-1));
        assertEquals("Index: -1, Size: 10", e.getMessage());
    }

}
