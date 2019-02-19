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

package org.abego.commons.blackboard;

import org.abego.commons.seq.Seq;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.abego.commons.seq.Seq.newSeq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractBlackboardTest {

    abstract <T> Blackboard<T> newBlackboard();

    @Test
    void isEmptyOk() {
        Blackboard<String> bb = newBlackboard();

        assertTrue(bb.isEmpty());

        bb.add("x");

        assertFalse(bb.isEmpty());
    }

    @Test
    void itemsOk() {
        Blackboard<String> bb = newBlackboard();
        bb.add("a");
        bb.add("b");
        bb.add("c");

        Seq<String> items = bb.items();
        assertEquals(newSeq("a", "b", "c"), items);
    }

    @Test
    void itemWithOrNullOk() {
        Blackboard<String> bb = newBlackboard();
        bb.add("a");
        bb.add("ab");
        bb.add("c");

        assertEquals("ab", bb.itemWithOrNull(s -> s.startsWith("a")));

        assertNull(bb.itemWithOrNull(s -> false));
    }

    @Test
    void itemWithOk() {
        Blackboard<String> bb = newBlackboard();
        bb.add("a");
        bb.add("ab");
        bb.add("c");

        assertEquals("ab", bb.itemWith(s -> s.startsWith("a")));

        assertThrows(NoSuchElementException.class, () -> bb.itemWith(s -> false));
    }

    @Test
    void containsItemWithOk() {
        Blackboard<String> bb = newBlackboard();
        bb.add("a");
        bb.add("ab");
        bb.add("c");

        assertTrue(bb.containsItemWith(s -> s.startsWith("a")));
        assertFalse(bb.containsItemWith(s -> false));
    }

    @Test
    void containsOk() {
        Blackboard<String> bb = newBlackboard();
        bb.add("a");
        bb.add("ab");
        bb.add("c");

        assertTrue(bb.contains("a"));
        assertTrue(bb.contains("ab"));
        assertTrue(bb.contains("c"));
        assertFalse(bb.contains("d"));
    }

    @Test
    void textOk() {
        Blackboard<String> bb = newBlackboard();
        bb.add("a");
        bb.add("ab");
        bb.add("c");

        assertEquals("a\nab\nc", bb.text());
    }


    @Test
    void clearOk() {
        Blackboard<String> bb = newBlackboard();
        bb.add("a");
        bb.add("b");
        bb.add("c");

        assertEquals(newSeq("a", "b", "c"), bb.items());
        assertFalse(bb.isEmpty());


        bb.clear();

        assertTrue(bb.isEmpty());
    }

    @Test
    void toStringOk() {
        Blackboard<String> bb = newBlackboard();
        bb.add("a");
        bb.add("ab");
        bb.add("c");

        String s = bb.toString();
        assertTrue(s.startsWith("org.abego.commons.blackboard.BlackboardDefault@"));
        assertTrue(s.endsWith("Text:\na\nab\nc"));
    }
}