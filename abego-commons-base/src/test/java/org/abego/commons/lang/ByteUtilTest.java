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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, ByteUtil::new);
    }


    @Test
    void reverse_Length0() {
        byte[] array = {10, 42, 123};

        ByteUtil.reverse(array, 1, 0);

        assertArrayEquals(array, new byte[]{10, 42, 123});

    }

    @Test
    void reverse_all() {
        byte[] array = {10, 42, 123};

        ByteUtil.reverse(array, 0, 3);

        assertArrayEquals(array, new byte[]{123, 42, 10});

    }

    @Test
    void reverse_subRange() {
        byte[] array = {10, 42, 123};

        ByteUtil.reverse(array, 1, 2);

        assertArrayEquals(array, new byte[]{10, 123, 42});

    }

    @Test
    void reverse_evenLength() {
        byte[] array = {10, 42, 123, 98};

        ByteUtil.reverse(array, 0, 4);

        assertArrayEquals(array, new byte[]{98, 123, 42, 10});

    }

    @Test
    void reverse_null() {

        // passing null does not throw an exception
        ByteUtil.reverse(null, 1, 2);
    }
}