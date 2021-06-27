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

package org.abego.commons.seq;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import static org.abego.commons.lang.IterableUtil.textOf;
import static org.abego.commons.seq.SeqUtil.emptySeq;
import static org.abego.commons.seq.SeqUtil.newSeq;
import static org.abego.commons.seq.SeqUtil.newSeqOfNullable;
import static org.abego.commons.seq.SeqUtil.reverse;
import static org.abego.commons.seq.SeqUtil.toSeq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeqUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, SeqUtil::new);
    }


    @Test
    void sorted_OK() {
        Seq<String> items = newSeq("c", "aaa", "BB");

        Seq<String> result = items.sorted();

        assertEquals("BBaaac", textOf(result));
    }

    @Test
    void sortedBy_OK() {
        Seq<String> items = newSeq("c", "aaa", "BB");

        Seq<String> result = items.sortedBy(String::length);

        assertEquals("cBBaaa", textOf(result));
    }

    @Test
    void sortedBy_WithNullsOK() {
        Seq<@Nullable String> items = newSeq("c", null, "aaa", null, "BB");

        Seq<@Nullable String> result = items.sortedBy(
                s -> s == null ? Integer.MAX_VALUE : s.length());

        assertEquals("c,BB,aaa,null,null", textOf(result, ","));
    }

    @Test
    void newSeq_emptyIterable() {
        Seq<String> items = newSeq();

        Seq<String> result = newSeq(items);

        assertEquals(0, result.size());
    }

    @Test
    void emptySeq_OK() {
        Seq<String> items = emptySeq();

        assertEquals(0, items.size());
        assertTrue(items.isEmpty());
    }

    @Test
    void reverse_empty() {
        Seq<String> items = emptySeq();

        Seq<String> actual = reverse(items);

        assertTrue(actual.isEmpty());
    }

    @Test
    void reverse_oneItem() {
        Seq<String> items = newSeq("A");

        Seq<String> actual = reverse(items);

        assertEquals(1, actual.size());
        assertEquals("A", actual.item(0));
    }

    @Test
    void reverse_twoItem() {
        Seq<String> items = newSeq("A", "B");

        Seq<String> actual = reverse(items);

        assertEquals(2, actual.size());
        assertEquals("B", actual.item(0));
        assertEquals("A", actual.item(1));
    }

    @Test
    void reverse_threeItem() {
        Seq<String> items = newSeq("A", "B", "C");

        Seq<String> actual = reverse(items);

        assertEquals(3, actual.size());
        assertEquals("C", actual.item(0));
        assertEquals("B", actual.item(1));
        assertEquals("A", actual.item(2));
    }

    @Test
    void newSeqOfNullable_NotNull() {
        Seq<String> s1 = newSeqOfNullable("foo");

        assertEquals(1, s1.size());
        assertEquals("foo", s1.item(0));
    }

    @Test
    void newSeqOfNullable_Null() {
        Seq<String> s1 = newSeqOfNullable(null);

        assertEquals(0, s1.size());
    }

    @Test
    void groupedAndMerged() {
        // Example: the groups are "even and odd", and the merge just converts
        // the integers of each group into a space separated string.
        Seq<Integer> seq = newSeq(2, 9, 7, 3, 4, 2, 6, 1);
        @NonNull
        Comparator<Integer> evenAndOdd = Comparator.comparingInt(a -> a % 2);
        Function<SeqNonEmpty<Integer>, String> spaceSeparated =
                s -> s.joined(" ");

        Seq<String> result = SeqUtil.groupedAndMerged(
                seq, evenAndOdd, spaceSeparated);

        assertEquals("2 4 2 6\n9 7 3 1", result.joined("\n"));
    }

    @Test
    void toSeq_withSeqAsArgument() {
        Iterable<String> seqAsIterable = newSeq("foo", "bar");

        Seq<String> toSeqResult = toSeq(seqAsIterable);

        assertEquals(2, toSeqResult.size());
        assertEquals("foo", toSeqResult.item(0));
        assertEquals("bar", toSeqResult.item(1));
        // as the Iterable was already a Seq it is used, not wrapped.
        assertSame(seqAsIterable, toSeqResult);
    }

    @Test
    void toSeq_withNonSeqAsArgument() {
        Iterable<String> list = Arrays.asList("foo", "bar");

        Seq<String> toSeqResult = toSeq(list);
        assertEquals(2, toSeqResult.size());
        assertEquals("foo", toSeqResult.item(0));
        assertEquals("bar", toSeqResult.item(1));
        // a List is not a Seq, therefore toSeq returns a new Seq instance
        assertNotSame(list, toSeqResult);
    }


}