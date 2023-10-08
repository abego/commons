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

package org.abego.commons.util.function;

import org.abego.commons.lang.ThreadUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.abego.commons.util.function.BooleanSupplierUtil.not;
import static org.abego.commons.util.function.BooleanSupplierUtil.waitUntil;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BooleanSupplierUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class,
                BooleanSupplierUtil::new);
    }

    @Test
    void not_OK() {
        assertTrue(not(() -> false).getAsBoolean());
        assertFalse(not(() -> true).getAsBoolean());
    }

    @Test
    void waitUntil_OK() {
        boolean[] var = new boolean[]{false};
        StringBuilder sb = new StringBuilder();

        new Thread(() -> {
            waitUntil(() -> var[0]);
            sb.append("foo");
        }).start();

        assertEquals("", sb.toString());
        ThreadUtil.sleep(BooleanSupplierUtil.DEFAULT_WAIT_MILLIS * 2);
        assertEquals("", sb.toString());

        var[0] = true;

        waitUntil(() -> "foo".equals(sb.toString()));
        assertEquals("foo", sb.toString());
    }

    @Test
    void waitUntil_interrupted() {
        AtomicBoolean var = new AtomicBoolean();
        StringBuilder sb = new StringBuilder();

        Thread otherThread = new Thread(() -> {
            waitUntil(var::get);
            sb.append("foo");
        });
        otherThread.start();

        assertEquals("", sb.toString());
        ThreadUtil.sleep(BooleanSupplierUtil.DEFAULT_WAIT_MILLIS * 2);
        assertEquals("", sb.toString());

        otherThread.interrupt();

        // The interrupts end the waitUntil, i.e. the code following the
        // waitUntil is executed and writes the "foo".
        waitUntil(() -> "foo".equals(sb.toString()));
        assertEquals("foo", sb.toString());

        // Just to verify: the "normal" waitUntil condition is still false:
        assertFalse(var.get());
    }
}
