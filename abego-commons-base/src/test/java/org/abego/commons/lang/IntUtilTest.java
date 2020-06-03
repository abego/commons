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
import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, IntUtil::new);
    }

    @Test
    void intSubRangeArray_1Arg_rangeError() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> IntUtil.intSubRangeArray(0));
        assertEquals("n > 0 expected. {n: 0}", e.getMessage());
    }

    @Test
    void intSubRangeArray_1Arg_OK() {
        int[] a = IntUtil.intSubRangeArray(3);

        assertEquals(3, a.length);
        assertEquals(0, a[0]);
        assertEquals(1, a[1]);
        assertEquals(2, a[2]);
    }

    @Test
    void intSubRangeArray_2Args_rangeError() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> IntUtil.intSubRangeArray(2, 1));
        assertEquals("start <= end expected. {start: 2, end: 1}", e.getMessage());
    }

    @Test
    void intSubRangeArray_2Args_OK() {
        int[] a = IntUtil.intSubRangeArray(2, 4);

        assertEquals(3, a.length);
        assertEquals(2, a[0]);
        assertEquals(3, a[1]);
        assertEquals(4, a[2]);

    }

    @Test
    void checkBoundsEndOpenOK() {
        IndexOutOfBoundsException e = assertThrows(IndexOutOfBoundsException.class, () ->
                IntUtil.checkBoundsEndOpen(1, 2, 4));
        assertEquals("Expected 2 <= i < 4, got 1", e.getMessage());

        IntUtil.checkBoundsEndOpen(2, 2, 4);

        IntUtil.checkBoundsEndOpen(3, 2, 4);

        e = assertThrows(IndexOutOfBoundsException.class, () ->
                IntUtil.checkBoundsEndOpen(4, 2, 4));
        assertEquals("Expected 2 <= i < 4, got 4", e.getMessage());
    }

    @Test
    void limit_1Arg() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                IntUtil.limit(2, -1));
        assertEquals("lowerBound must not be larger than upperBound", e.getMessage());

        assertEquals(0, IntUtil.limit(-1, 3));
        assertEquals(0, IntUtil.limit(0, 3));
        assertEquals(1, IntUtil.limit(1, 3));
        assertEquals(2, IntUtil.limit(2, 3));
        assertEquals(3, IntUtil.limit(3, 3));
        assertEquals(3, IntUtil.limit(4, 3));
    }

    @Test
    void limit_2Args() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                IntUtil.limit(2, 2, 1));
        assertEquals("lowerBound must not be larger than upperBound", e.getMessage());

        assertEquals(2, IntUtil.limit(1, 2, 4));
        assertEquals(2, IntUtil.limit(2, 2, 4));
        assertEquals(3, IntUtil.limit(3, 2, 4));
        assertEquals(4, IntUtil.limit(4, 2, 4));
        assertEquals(4, IntUtil.limit(5, 2, 4));
    }

    @Test
    void parseInt() {
        // OK case
        OptionalInt i = IntUtil.parseInt("123");
        assertTrue(i.isPresent());
        assertEquals(123, i.getAsInt());

        // not a number
        i = IntUtil.parseInt("foo");
        assertFalse(i.isPresent());
    }
}