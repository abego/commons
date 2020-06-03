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

package org.abego.commons.util;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, MapUtil::new);
    }

    @Test
    void getStringObjectMap_oneEntry() {
        Map<String, @Nullable Object> map = MapUtil.getStringObjectMap("foo", "bar");

        assertEquals(1, map.size());
        assertEquals("bar", map.get("foo"));
    }

    @Test
    void getStringObjectMap_twoEntries() {
        Map<String, @Nullable Object> map = MapUtil.getStringObjectMap(
                "foo", "bar",
                "baz", 42);

        assertEquals(2, map.size());
        assertEquals("bar", map.get("foo"));
        assertEquals(42, map.get("baz"));
    }

    @Test
    void getStringObjectMap_threeEntries() {
        Map<String, @Nullable Object> map = MapUtil.getStringObjectMap(
                "foo", "bar",
                "baz", 42,
                "doo", 3.14);

        assertEquals(3, map.size());
        assertEquals("bar", map.get("foo"));
        assertEquals(42, map.get("baz"));
        assertEquals(3.14, map.get("doo"));
    }

    @Test
    void getStringObjectMap_incompleteEntry() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> MapUtil.getStringObjectMap(
                "foo", "bar",
                "baz"));

        assertEquals("namesAndValues must contain an even number of objects, as we expect (name,value) pairs", e.getMessage());
    }

    @Test
    void getStringObjectMap_keyIsNoString() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> MapUtil.getStringObjectMap(
                "foo", "bar",
                42, 3.14));

        assertEquals("Expected name to be a String, got java.lang.Integer", e.getMessage());
    }

    @Test
    void getStringObjectMap_duplicatedKey() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> MapUtil.getStringObjectMap(
                "foo", "bar",
                "foo", "baz"));

        assertEquals("Name 'foo' already defined", e.getMessage());
    }
}