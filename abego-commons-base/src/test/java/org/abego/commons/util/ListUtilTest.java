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

package org.abego.commons.util;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.abego.commons.lang.IterableUtil.textOf;
import static org.abego.commons.util.ListUtil.list;
import static org.abego.commons.util.ListUtil.map;
import static org.abego.commons.util.ListUtil.nthItemAsStringOrNull;
import static org.abego.commons.util.ListUtil.sortedList;
import static org.abego.commons.util.ListUtil.toList;
import static org.abego.commons.util.ListUtil.toListWithMapping;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListUtilTest {

    @Test
    void sortedList_OK() {
        List<String> items = toList("c", "aaa", "BB");

        List<String> sorted = sortedList(items);

        assertEquals("BB,aaa,c", textOf(sorted, ","));
    }

    @Test
    void sortedList_withComparatorOK() {
        List<String> items = toList("c", "aaa", "BB");

        List<String> sorted = sortedList(items, String::compareToIgnoreCase);

        assertEquals("aaa,BB,c", textOf(sorted, ","));
    }

    @Test
    void sortedList_withSortKeyOK() {
        List<String> items = toList("c", "aaa", "BB");

        List<String> sorted = sortedList(items, String::length);

        assertEquals("c,BB,aaa", textOf(sorted, ","));
    }

    @Test
    void toListWithMapping_OK() {
        List<String> items = toList("c", "aaa", "BB");

        List<Integer> lengths = toListWithMapping(items, String::length);

        assertEquals(3, lengths.size());
        assertEquals(1, (int) lengths.get(0));
        assertEquals(3, (int) lengths.get(1));
        assertEquals(2, (int) lengths.get(2));
    }

    @Test
    void map_OK() {
        List<String> items = toList("c", "aaa", "BB");

        List<Integer> lengths = map(items, String::length);

        assertEquals(3, lengths.size());
        assertEquals(1, (int) lengths.get(0));
        assertEquals(3, (int) lengths.get(1));
        assertEquals(2, (int) lengths.get(2));
    }

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, ListUtil::new);
    }

    @Test
    void list_ok() {
        //noinspection DataFlowIssue
        List<?> c = list("a", 1, true, null);

        assertEquals(4, c.size());
        assertEquals("a", c.get(0));
        assertEquals(1, c.get(1));
        assertEquals(true, c.get(2));
        assertNull(c.get(3));
    }

    @Test
    void list_empty() {
        List<?> c = list();

        assertEquals(0, c.size());
    }

    @Test
    void nthItemAsStringOrNull_ok() {
        //noinspection DataFlowIssue
        List<?> c = list("a", 1, true, null);

        assertNull(nthItemAsStringOrNull(c, -1));
        assertEquals("a", nthItemAsStringOrNull(c, 0));
        assertEquals("1", nthItemAsStringOrNull(c, 1));
        assertEquals("true", nthItemAsStringOrNull(c, 2));
        assertNull(nthItemAsStringOrNull(c, 3));
        assertNull(nthItemAsStringOrNull(c, 4));
    }
}
