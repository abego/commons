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

package org.abego.commons.range;

import org.junit.jupiter.api.Test;

import static org.abego.commons.range.IntRangeDefault.newIntRange;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntRangeDefaultTest {

    @Test
    void testIsEmpty() {
        IntRange i = newIntRange(2, 5);
        IntRange i2 = newIntRange(3, 3);

        assertFalse(i.isEmpty());
        assertTrue(i2.isEmpty());
    }

    @Test
    void testIsNotEmpty() {
        IntRange i = newIntRange(2, 5);
        IntRange i2 = newIntRange(3, 3);

        assertTrue(i.isNotEmpty());
        assertFalse(i2.isNotEmpty());
    }

    @Test
    void invalidStartAndEnd() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> newIntRange(5, 2));
        assertEquals("start must be <= end, got 5 and 2", e.getMessage());
    }

    @Test
    void newIntRangeOK() {
        IntRange r = newIntRange(2, 5);

        assertEquals(2, r.getStart());
        assertEquals(5, r.getEnd());
    }

    @Test
    void contains() {
        IntRange r = newIntRange(2, 5);
        IntRange r2 = newIntRange(2, 5);
        IntRange r3 = newIntRange(2, 4);
        IntRange r4 = newIntRange(3, 5);
        IntRange r5 = newIntRange(1, 5);

        assertTrue(r.contains(r));
        assertTrue(r.contains(r2));
        assertTrue(r.contains(r3));
        assertTrue(r.contains(r4));
        assertFalse(r.contains(r5));

        assertTrue(r2.contains(r));
        assertFalse(r3.contains(r));
        assertFalse(r4.contains(r));
        assertTrue(r5.contains(r));

    }

    @Test
    void newIntRange_endBeforeStart() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> newIntRange(5, 2));
        assertEquals("start must be <= end, got 5 and 2", e.getMessage());
    }


    @Test
    void testToString() {
        IntRange r = newIntRange(2, 5);

        assertEquals("2..5", r.toString());
    }

    @Test
    void testEquals() {
        IntRange r = newIntRange(2, 5);
        IntRange r2 = newIntRange(2, 5);
        IntRange r3 = newIntRange(2, 4);
        IntRange r4 = newIntRange(1, 5);

        assertEquals(r, r);
        assertEquals(r, r2);
        assertNotEquals(r, r3);
        assertNotEquals(r, r4);
    }

    @Test
    void testHashCode() {
        IntRange r = newIntRange(2, 5);
        IntRange r2 = newIntRange(2, 5);

        assertEquals(r.hashCode(), r2.hashCode());
    }

}