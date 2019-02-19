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

package org.abego.commons.lang;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.abego.commons.lang.IterableUtil.areEqual;
import static org.abego.commons.lang.IterableUtil.textOf;
import static org.abego.commons.util.ListUtil.list;
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
        Iterable<String> abNullc = list("a", "b", null, "c");

        assertEquals("", textOf(empty));
        assertEquals("abc", textOf(abc));
        assertEquals("abnullc", textOf(abNullc));

        assertEquals("", textOf(empty, ", "));
        assertEquals("a, b, c", textOf(abc, ", "));
        assertEquals("a, b, null, c", textOf(abNullc, ", "));

        assertEquals("", textOf(empty, ", ", String::toUpperCase));
        assertEquals("A, B, C", textOf(abc, ", ", String::toUpperCase));
        assertEquals("A, B, null, C", textOf(abNullc, ", ", String::toUpperCase));

        assertEquals("empty", textOf(empty, ", ", "empty", String::toUpperCase, "-"));
        assertEquals("A, B, C", textOf(abc, ", ", "empty", String::toUpperCase, "-"));
        assertEquals("A, B, -, C", textOf(abNullc, ", ", "empty", String::toUpperCase, "-"));
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
}
