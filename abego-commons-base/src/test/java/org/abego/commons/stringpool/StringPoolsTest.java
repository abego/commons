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

package org.abego.commons.stringpool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class StringPoolsTest {

    @Test
    void builder() {
        StringPoolBuilder builder = StringPools.builder();

        assertNotNull(builder);
    }

    @Test
    void newStringPool() {
        StringPoolBuilder builder = StringPools.builder();
        int id = builder.add("foo");
        StringPool sp = builder.build();

        StringPool sp2 = StringPools.newStringPool(sp.getBytes());

        assertNotEquals(sp, sp2);
        assertEquals("foo", sp.getString(id));
        assertEquals("foo", sp2.getString(id));
    }

    @Test
    void mutableStringPoolSmokeTest() {
        MutableStringPool stringPool = StringPools.newMutableStringPool();

        int a = stringPool.add("a");
        int a2 = stringPool.add("a");

        assertEquals(a, a2);

        Iterable<StringPool.StringAndID> all = stringPool.allStringAndIDs();
        Iterator<StringPool.StringAndID> iterator = all.iterator();
        assertNull(iterator.next().getString());
        assertEquals("a", iterator.next().getString());

        int b = stringPool.add("b");

        assertEquals("a", stringPool.getString(a));
        assertEquals("b", stringPool.getString(b));
        assertEquals("b", stringPool.getStringOrNull(b));

        assertNull(stringPool.getStringOrNull(0));

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> stringPool.getStringOrNull(-1));
        assertEquals("Invalid id", e.getMessage());
    }
}
