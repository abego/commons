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

package org.abego.commons.lang;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.abego.commons.TestData.SAMPLE_TEXT;
import static org.abego.commons.TestData.SAMPLE_TEXT_2;
import static org.abego.commons.TestData.SAMPLE_TEXT_3;
import static org.abego.commons.lang.ArrayUtil.checkArrayIndex;
import static org.abego.commons.lang.ArrayUtil.indexOf;
import static org.abego.commons.lang.ArrayUtil.itemOrDefault;
import static org.abego.commons.lang.ArrayUtil.iterator;
import static org.abego.commons.lang.StringUtil.array;
import static org.abego.commons.lang.StringUtil.arrayOfNullables;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArrayUtilTest {


    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, ArrayUtil::new);
    }

    @Test
    void arrayOk() {
        String[] array = array(SAMPLE_TEXT, SAMPLE_TEXT_2, SAMPLE_TEXT_3);

        assertEquals(3, array.length);
        assertEquals(SAMPLE_TEXT, array[0]);
        assertEquals(SAMPLE_TEXT_2, array[1]);
        assertEquals(SAMPLE_TEXT_3, array[2]);
    }

    @Test
    void arrayTestCaseEmptyArrayOk() {
        assertEquals(0, array().length);
    }

    @Test
    void iteratorOk() {
        Iterator<String> iter = iterator(new @NonNull String[]{"a", "b", "c"});

        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertEquals("c", iter.next());

    }

    @Test
    void iteratorTestCaseWrongNextAccessFails() {
        Iterator<Object> iter = iterator(new @NonNull Object[0]);

        assertThrows(NoSuchElementException.class, iter::next);

    }

    @Test
    void indexOf_OK() {
        String[] array = arrayOfNullables("foo", "bar", null, "baz");

        assertEquals(0, indexOf(array, "foo"));
        assertEquals(1, indexOf(array, "bar"));
        assertEquals(2, indexOf(array, null));
        assertEquals(3, indexOf(array, "baz"));
        assertEquals(-1, indexOf(array, "qux"));
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    void itemOrDefault_OK() {
        String[] array = arrayOfNullables("foo", "bar", null, "baz");


        assertEquals("foo", itemOrDefault(array, 0, "quux"));
        assertEquals("bar", itemOrDefault(array, 1, "quux"));
        assertEquals(null, itemOrDefault(array, 2, "quux"));
        assertEquals("baz", itemOrDefault(array, 3, "quux"));
        assertEquals("quux", itemOrDefault(array, 4, "quux"));
        assertEquals("quux", itemOrDefault(array, -1, "quux"));

        // array == null
        assertEquals("quux", itemOrDefault(null, 0, "quux"));
    }

    @Test
    void checkArrayIndexOK() {
        assertThrows(IndexOutOfBoundsException.class, () -> checkArrayIndex(-1, 3));
        checkArrayIndex(0, 3);
        checkArrayIndex(1, 3);
        checkArrayIndex(2, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> checkArrayIndex(3, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> checkArrayIndex(4, 3));
    }
}