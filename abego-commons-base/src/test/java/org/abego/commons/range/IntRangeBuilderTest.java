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

import static org.abego.commons.range.IntRangeBuilder.newIntRangeBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntRangeBuilderTest {

    @Test
    void invalidStartAndEnd() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> newIntRangeBuilder(5, 2));
        assertEquals("start must be <= end. Got start = 5, end = 2",
                e.getMessage());
    }

    @Test
    void invalidSetStart() {
        IntRangeBuilder i = newIntRangeBuilder(2, 5);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> i.withStart(8));
        assertEquals("start must be <= end. Got start = 8, end = 5",
                e.getMessage());
    }

    @Test
    void smokeTest() {
        IntRangeBuilder i = newIntRangeBuilder(2, 5);
        assertEquals("IntRangeBuilder{start=2, end=5}", i.toString());

        i.withStart(1);
        assertEquals(newIntRangeBuilder(1, 5), i);
        assertEquals("IntRangeBuilder{start=1, end=5}", i.toString());

        i.withEnd(8);
        assertEquals(newIntRangeBuilder(1, 8), i);
        assertEquals("IntRangeBuilder{start=1, end=8}", i.toString());

        // cascade
        assertEquals("IntRangeBuilder{start=3, end=4}",
                i.withStart(3).withEnd(4).toString());

        // factory IntRangeBuilder.of
        assertEquals("IntRangeBuilder{start=4, end=6}",
                IntRangeBuilder.of(4, 6).toString());
    }

    @Test
    void invalidSetEnd() {
        IntRangeBuilder i = newIntRangeBuilder(2, 5);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> i.withEnd(1));
        assertEquals("start must be <= end. Got start = 2, end = 1",
                e.getMessage());
    }

    @Test
    void equalsHashCode() {
        IntRangeBuilder a = newIntRangeBuilder(2, 5);
        IntRangeBuilder b = newIntRangeBuilder(3, 8);
        IntRangeBuilder b2 = newIntRangeBuilder(3, 8);

        assertNotEquals(a, b2);
        assertEquals(b, b2);
        assertEquals(b.hashCode(), b2.hashCode());
    }
}