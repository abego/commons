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

package org.abego.commons.lang;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.abego.commons.lang.IterableUtil.appendTextOf;
import static org.abego.commons.lang.IterableUtil.areEqual;
import static org.abego.commons.lang.IterableUtil.firstOrNull;
import static org.abego.commons.lang.IterableUtil.hashCodeForIterable;
import static org.abego.commons.lang.IterableUtil.join;
import static org.abego.commons.lang.IterableUtil.size;
import static org.abego.commons.lang.IterableUtil.textOf;
import static org.abego.commons.lang.IterableUtil.toStringOfIterable;
import static org.abego.commons.util.ListUtil.list;
import static org.abego.commons.util.ListUtil.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IterableUtilTest {
    @Test
    void constructorFails() {
        Assertions.assertThrows(MustNotInstantiateException.class,
                IterableUtil::new);
    }

    @Test
    void textOfOk() {
        Iterable<String> empty = list();
        Iterable<String> abc = list("a", "b", "c");
        Iterable<@Nullable String> abNullc = list("a", "b", null, "c");

        assertEquals("", textOf(empty));
        assertEquals("abc", textOf(abc));
        assertEquals("abnullc", textOf(abNullc));

        assertEquals("", textOf(empty, ", "));
        assertEquals("a, b, c", textOf(abc, ", "));
        assertEquals("a, b, null, c", textOf(abNullc, ", "));

        assertEquals("", textOf(empty, ", ", String::toUpperCase));
        assertEquals("A, B, C", textOf(abc, ", ", String::toUpperCase));
        assertEquals("A, B, null, C", textOf(abNullc, ", ", StringUtil::toUpperCaseSafe));

        assertEquals("empty", textOf(empty, ", ", "empty", String::toUpperCase, "-"));
        assertEquals("A, B, C", textOf(abc, ", ", "empty", String::toUpperCase, "-"));
        assertEquals("A, B, -, C", textOf(abNullc, ", ", "empty", StringUtil::toUpperCaseSafe, "-"));
    }

    @Test
    void join_Ok() {
        Iterable<String> empty = list();
        Iterable<String> abc = list("a", "b", "c");
        Iterable<@Nullable String> abNullc = list("a", "b", null, "c");

        assertEquals("", join(", ", empty));
        assertEquals("a, b, c", join(", ", abc));
        assertEquals("a, b, null, c", join(", ", abNullc));
    }

    @Test
    void appendTextOfWithEllipsis() {
        StringBuilder out = new StringBuilder();
        List<String> items = toList("foo", null, "bar", "", "baz", "doo");

        appendTextOf(
                out, items, ",", "empty", String::toString, "NULL", 16);

        assertEquals("foo,NULL,bar,,baz,...", out.toString());
    }

    @Test
    void appendTextOfWithEmptyText() {
        StringBuilder out = new StringBuilder();
        List<String> items = toList();

        appendTextOf(
                out, items, ",", "empty", String::toString, "null", 2);

        assertEquals("empty", out.toString());
    }

    @Test
    void toStringOfIterableOk() {
        List<String> items = toList("foo", null, "bar", "", "baz", "doo");

        String actual = toStringOfIterable(items);

        assertEquals("java.util.ArrayList[foo, null, bar, , baz, doo]", actual);

    }

    @Test
    void areEqualOk() {
        List<String> abc = list("a", "b", "c");
        List<String> abc2 = list("a", "b", "c");
        List<String> d = list("d");

        assertTrue(areEqual(abc, abc2));
        assertTrue(areEqual(abc, abc));
        assertFalse(areEqual(abc, d));

        // identical case
        assertTrue(areEqual(abc, abc));
    }

    @Test
    void hashCodeForIterableOk() {
        List<String> abc = list("a", "b", "c");
        List<String> abc2 = list("a", "b", "c");
        List<@Nullable String> withNull = list("a", null, "b");

        // calling the
        assertEquals(hashCodeForIterable(abc), hashCodeForIterable(abc));
        assertEquals(hashCodeForIterable(abc), hashCodeForIterable(abc2));
        assertEquals(hashCodeForIterable(withNull), hashCodeForIterable(withNull));
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    void firstOrNull_Ok() {
        Iterable<String> items = list("foo", "bar");

        assertEquals("foo", firstOrNull(items, s -> s.startsWith("f")));
        assertEquals("bar", firstOrNull(items, s -> s.startsWith("b")));
        assertEquals("foo", firstOrNull(items, s -> s.length() == 3));
        assertEquals(null, firstOrNull(items, s -> false));
    }

    @Test
    void sizeOK() {
        Iterable<String> empty = list();
        Iterable<String> abc = list("a", "b", "c");
        Iterable<@Nullable String> abNullc = list("a", "b", null, "c");

        assertEquals(0, size(empty));
        assertEquals(3, size(abc));
        assertEquals(4, size(abNullc));
    }
}
