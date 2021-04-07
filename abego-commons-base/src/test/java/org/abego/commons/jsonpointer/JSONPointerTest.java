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

package org.abego.commons.jsonpointer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.abego.commons.jsonpointer.JSONPointer.newJSONPointer;
import static org.abego.commons.jsonpointer.JSONPointer.referencedValue;
import static org.abego.commons.lang.StringUtil.array;
import static org.abego.commons.util.ListUtil.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JSONPointerTest {
    private final Object sample = getSample();
    private final @NonNull String[] sampleArray = getSampleArray();

    /**
     * Return the JSON document given in chapter 5 of RFC 6901
     * (https://tools.ietf.org/html/rfc6901).
     *
     * <p>
     * It looks like this:
     * <pre>
     *    {
     *       "foo": ["bar", "baz"],
     *       "": 0,
     *       "a/b": 1,
     *       "c%d": 2,
     *       "e^f": 3,
     *       "g|h": 4,
     *       "i\\j": 5,
     *       "k\"l": 6,
     *       " ": 7,
     *       "m~n": 8
     *       "~1": 9   // not in RFC example, but in chapter 4 of RFC
     *    }
     * </pre>
     */
    private static Object getSample() {
        Map<String, Object> result = new HashMap<>();

        result.put("foo", toList("bar", "baz"));
        result.put("", 0);
        result.put("a/b", 1);
        result.put("c%d", 2);
        result.put("e^f", 3);
        result.put("g|h", 4);
        result.put("i\\j", 5);
        result.put("k\"l", 6);
        result.put(" ", 7);
        result.put("m~n", 8);
        result.put("~1", 9);

        return result;
    }

    private @NonNull String[] getSampleArray() {
        return array("a", "b", "c");
    }

    @Test
    void referencedValue_root() {
        assertEquals(sample, referencedValue(sample, ""));
    }

    @Test
    void referencedValue_simpleKey() {
        assertEquals(toList("bar", "baz"), referencedValue(sample, "/foo"));
    }


    @Test
    void referencedValue_arrayItem() {
        assertEquals("bar", referencedValue(sample, "/foo/0"));
    }

    @Test
    void referencedValue_emptyKey() {
        assertEquals(0, referencedValue(sample, "/"));
    }

    @Test
    void referencedValue_keyWithSlash() {
        assertEquals(1, referencedValue(sample, "/a~1b"));
    }

    @Test
    void referencedValue_keyWithPercent() {
        assertEquals(2, referencedValue(sample, "/c%d"));
    }

    @Test
    void referencedValue_keyWithCircumflex() {
        assertEquals(3, referencedValue(sample, "/e^f"));
    }

    @Test
    void referencedValue_keyWithPipe() {
        assertEquals(4, referencedValue(sample, "/g|h"));
    }

    @Test
    void referencedValue_keyWithBackslash() {
        assertEquals(5, referencedValue(sample, "/i\\j"));
    }

    @Test
    void referencedValue_keyWithDoubleQuote() {
        assertEquals(6, referencedValue(sample, "/k\"l"));
    }

    @Test
    void referencedValue_keyWithTilde() {
        assertEquals(8, referencedValue(sample, "/m~0n"));
    }

    @Test
    void referencedValue_singleSpaceKey() {
        assertEquals(7, referencedValue(sample, "/ "));
    }

    @Test
    void referencedValue_keyWithTildeOne() {
        assertEquals(9, referencedValue(sample, "/~01"));
    }

    @Test
    void referencedValue_MissingStartingSlash() {
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> referencedValue(sample, "bar"));

        assertEquals("Error in 'bar': must start with '/' or be empty", e.getMessage());
    }

    @Test
    void referencedValue_MissingKey() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> referencedValue(sample, "/bar"));
    }

    @Test
    void referencedValue_Array() {
        assertEquals("a", referencedValue(sampleArray, "/0"));
        assertEquals("b", referencedValue(sampleArray, "/1"));
        assertEquals("c", referencedValue(sampleArray, "/2"));

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> referencedValue(sampleArray, "/-1"));
        assertEquals("Error in '/-1': expected 0 < index < 3, got index with: -1", e.getMessage());

        e = Assertions.assertThrows(IllegalArgumentException.class, () -> referencedValue(sampleArray, "/4"));
        assertEquals("Error in '/4': expected 0 < index < 3, got index with: 4", e.getMessage());
    }

    @Test
    void referencedValue_Iterable_OneItem() {
        Set<String> strings = new HashSet<>();
        strings.add("foo");

        assertEquals("foo", referencedValue(strings, "/0"));

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> referencedValue(strings, "/-1"));
        assertEquals("Error in '/-1': index must not be negative, got: -1", e.getMessage());

        e = Assertions.assertThrows(IllegalArgumentException.class, () -> referencedValue(strings, "/4"));
        assertEquals("Error in '/4': expected 0 < index < 1, got index with: 4", e.getMessage());
    }

    @Test
    void referencedValue_Iterable_MultipleItems() {
        Set<String> strings = new HashSet<>();
        strings.add("foo");
        strings.add("bar");

        @Nullable Object item0 = referencedValue(strings, "/0");
        @Nullable Object item1 = referencedValue(strings, "/1");

        assertNotEquals(item0, item1);
        assertTrue(item0 != null && (item0.equals("foo") || item0.equals("bar")));
        assertTrue(item1 != null && (item1.equals("foo") || item1.equals("bar")));

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> referencedValue(strings, "/2"));
        assertEquals("Error in '/2': expected 0 < index < 2, got index with: 2", e.getMessage());
    }

    @Test
    void referencedValue_InvalidData() {

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> assertEquals("Expected Map, array or Iterable, got: java.lang.String", referencedValue("wrongData", "/0")));
    }

    @Test
    void referencedValue_minusAsArrayIndex() {
        String jsonPointer = "/foo/-";

        IllegalArgumentException e =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> referencedValue(sample, jsonPointer));
        assertEquals(
                "Error in '/foo/-': expected index, got: '-'",
                e.getMessage());
    }

    @Test
    void newJSONPointer_ok() {
        JSONPointer jp = newJSONPointer("/foo");

        assertNotNull(jp);
    }

    @Test
    void JSONPointerOf_ok() {
        JSONPointer jp = JSONPointer.of("/foo");

        assertNotNull(jp);
    }

    @Test
    void isRoot() {
        assertFalse(JSONPointer.of("/foo").isRoot());
        assertFalse(JSONPointer.of("/").isRoot());
        assertTrue(JSONPointer.of("").isRoot());
    }

    @Test
    void getParent() {
        assertEquals("/foo",
                JSONPointer.of("/foo/bar").getParent().toString());
        assertEquals("", JSONPointer.of("/foo").getParent().toString());

        Exception e = assertThrows(Exception.class,
                () -> JSONPointer.of("").getParent());
        assertEquals(JSONPointer.UNSUPPORTED_FOR_ROOT, e.getMessage());
    }

    @Test
    void getLastToken() {
        assertEquals("bar", JSONPointer.of("/foo/bar").getLastToken());
        assertEquals("foo", JSONPointer.of("/foo").getLastToken());

        Exception e = assertThrows(Exception.class,
                () -> JSONPointer.of("").getLastToken());
        assertEquals(JSONPointer.UNSUPPORTED_FOR_ROOT, e.getMessage());
    }

    @Test
    void apply() {
        JSONPointer jp = newJSONPointer("/foo");

        assertEquals(toList("bar", "baz"), jp.apply(sample));
    }

    @Test
    void toString_ok() {
        JSONPointer jp = newJSONPointer("/foo/0");
        JSONPointer jp2 = newJSONPointer("/m~1n");
        JSONPointer jp3 = newJSONPointer("/k\"l");

        assertEquals("/foo/0", jp.toString());
        assertEquals("/m~1n", jp2.toString());
        assertEquals("/k\"l", jp3.toString());
    }

    @Test
    void equals_ok() {
        JSONPointer jp = newJSONPointer("/foo/0");
        JSONPointer jp2 = newJSONPointer("/foo/0");
        JSONPointer jp3 = newJSONPointer("/foo/1");
        String foo0String = "/foo/0";

        assertEquals(jp, jp);
        assertEquals(jp, jp2);
        Assertions.assertNotEquals(jp, jp3);
        //noinspection AssertBetweenInconvertibleTypes
        Assertions.assertNotEquals(jp, foo0String);
    }

    @Test
    void hashCode_ok() {
        JSONPointer jp = newJSONPointer("/foo/0");
        JSONPointer jp2 = newJSONPointer("/foo/0");
        JSONPointer jp3 = newJSONPointer("/foo/1");

        assertEquals(jp.hashCode(), jp.hashCode());
        assertEquals(jp.hashCode(), jp2.hashCode());
        Assertions.assertNotEquals(jp.hashCode(), jp3.hashCode());
    }
}