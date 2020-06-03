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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractSeqTest {

    private Seq<String> noItemSeq() {
        return Seq.emptySeq();
    }

    /**
     * Return a Seq containing the string <code>"a"</code>.
     */
    abstract Seq<String> singleItemSeq();

    /**
     * Return a Seq containing the strings <code>"h","e","l","l","o"</code>.
     */
    abstract Seq<String> helloSeq();

    @Test
    void seq_Array_ok() {
        Seq<String> s = Seq.newSeq("a");

        assertNotNull(s);
    }

    @Test
    void seq_List_ok() {
        List<String> list = new ArrayList<>();
        list.add("a");
        Seq<String> s = Seq.newSeq(list);

        assertNotNull(s);
    }

    @Test
    void isEmpty_ok() {
        assertTrue(noItemSeq().isEmpty());
        assertFalse(singleItemSeq().isEmpty());
        assertFalse(helloSeq().isEmpty());
    }

    @Test
    void hasSingleItem_ok() {
        assertFalse(noItemSeq().hasSingleItem());
        assertTrue(singleItemSeq().hasSingleItem());
        assertFalse(helloSeq().hasSingleItem());
    }

    @Test
    void first_ok() {
        assertThrows(Exception.class, () -> noItemSeq().first());
        assertEquals("a", singleItemSeq().first());
        assertEquals("h", helloSeq().first());
    }

    @Test
    void filter_ok() {
        Seq<String> r1 = noItemSeq().filter(s -> s.equals("l"));
        assertTrue(r1.isEmpty());

        Seq<String> r2 = singleItemSeq().filter(s -> s.equals("l"));
        assertTrue(r2.isEmpty());

        Seq<String> r3 = singleItemSeq().filter(s -> true);
        assertEquals(1, r3.size());
        assertEquals("a", r3.item(0));

        Seq<String> r4 = helloSeq().filter(s -> s.equals("l"));
        assertEquals(2, r4.size());
        assertEquals("l", r4.item(0));
        assertEquals("l", r4.item(1));
    }

    @Test
    void singleItem_ok() {
        assertThrows(NoSuchElementException.class, () -> noItemSeq().singleItem());
        assertEquals("a", singleItemSeq().singleItem());
        assertThrows(NoSuchElementException.class, () -> helloSeq().singleItem());
    }

    @Test
    void singleItemOrNull_ok() {
        assertNull(noItemSeq().singleItemOrNull());
        assertEquals("a", singleItemSeq().singleItemOrNull());
        assertThrows(NoSuchElementException.class, () -> helloSeq().singleItemOrNull());
    }

    @Test
    void anyItem_ok() {
        assertThrows(NoSuchElementException.class, () -> noItemSeq().anyItem());
        assertEquals("a", singleItemSeq().anyItem());

        String r = helloSeq().anyItem();
        assertTrue(r.equals("h") || r.equals("e") || r.equals("l") || r.equals("o"));
    }

    @Test
    void anyItemOrNull_ok() {
        assertNull(noItemSeq().anyItemOrNull());
        assertEquals("a", singleItemSeq().anyItemOrNull());

        String r = helloSeq().anyItemOrNull();
        assertTrue(r.equals("h") || r.equals("e") || r.equals("l") || r.equals("o"));
    }

    private void checkForEachOrderedForSeq(Seq<String> seq, int seqSize) {
        assertEquals(seqSize, seq.size());

        List<String> r = new ArrayList<>();

        seq.forEach(r::add);

        assertEquals(seqSize, r.size());
        for (int i = 0; i < seqSize; i++) {
            assertEquals(seq.item(i), r.get(i));
        }
    }

    @Test
    void forEachOrdered_ok() {
        checkForEachOrderedForSeq(noItemSeq(), 0);
        checkForEachOrderedForSeq(singleItemSeq(), 1);
        checkForEachOrderedForSeq(helloSeq(), 5);
    }

    private void checkForEachForSeq(Seq<String> seq, int uniqueItemCount) {
        Set<String> r = new HashSet<>();

        seq.forEach(r::add);

        assertEquals(uniqueItemCount, r.size());

        for (String s : seq) {
            assertTrue(r.contains(s));
        }
    }

    @Test
    void forEach_ok() {
        checkForEachForSeq(noItemSeq(), 0);
        checkForEachForSeq(singleItemSeq(), 1);
        checkForEachForSeq(helloSeq(), 4 /* "hello" contains 4 different chars */);
    }

    @Test
    void equals_ok() {
        assertEquals(noItemSeq(), noItemSeq());
        assertEquals(singleItemSeq(), singleItemSeq());
        assertEquals(helloSeq(), helloSeq());

        assertNotEquals(noItemSeq(), singleItemSeq());
        assertNotEquals(singleItemSeq(), helloSeq());
        assertNotEquals(helloSeq(), noItemSeq());

        // identical check
        Seq<String> n = noItemSeq();
        assertEquals(n, n);
        Seq<String> s = singleItemSeq();
        assertEquals(s, s);
        Seq<String> h = helloSeq();
        assertEquals(h, h);

        // null check
        assertNotEquals(noItemSeq(), null);
        assertNotEquals(null, noItemSeq());
        assertNotEquals(singleItemSeq(), null);
        assertNotEquals(null, singleItemSeq());
        assertNotEquals(helloSeq(), null);
        assertNotEquals(null, helloSeq());

    }

    @Test
    void hashCode_ok() {
        assertEquals(1, noItemSeq().hashCode());
        assertEquals(128, singleItemSeq().hashCode());
        assertEquals(127791473, helloSeq().hashCode());
    }

}