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

package org.abego.commons.pair;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class IntPairDefaultTest {

    @Test
    void smokeTest() {
        // factories
        IntPair p = IntPairDefault.newIntPair(1, 2);
        IntPairDefault p2 = IntPairDefault.of(1, 2);
        IntPairDefault p3 = IntPairDefault.of(3, 2);
        IntPairDefault p4 = IntPairDefault.of(1, 3);

        // first/second
        assertEquals(1, p.first());
        assertEquals(2, p.second());

        // equals
        assertEquals(p, p);
        assertEquals(p, p2);
        assertNotEquals(p, p3);
        assertNotEquals(p, p4);
        assertNotEquals(p, "(1,2)");

        // hashCode
        assertEquals(p.hashCode(), p2.hashCode());
        assertNotEquals(p.hashCode(), p3.hashCode());

        // toString
        assertEquals("(1,2)", p.toString());
    }


}