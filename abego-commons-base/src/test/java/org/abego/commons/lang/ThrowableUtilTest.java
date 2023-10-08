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

package org.abego.commons.lang;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import static org.abego.commons.lang.ThrowableUtil.allMessages;
import static org.abego.commons.lang.ThrowableUtil.allMessagesOrClassName;
import static org.abego.commons.lang.ThrowableUtil.messageOrClassName;
import static org.abego.commons.lang.ThrowableUtil.messageOrToString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowableUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, ThrowableUtil::new);
    }

    @Test
    void messageOrClassName_ok() {
        assertEquals("java.lang.IllegalArgumentException",
                messageOrClassName(new IllegalArgumentException()));

        assertEquals("abc",
                messageOrClassName(new IllegalArgumentException("abc")));
    }

    @Test
    void messageOrToString_ok() {
        assertEquals("java.lang.IllegalArgumentException",
                messageOrToString(new IllegalArgumentException()));

        assertEquals("abc",
                messageOrClassName(new IllegalArgumentException("abc")));
    }

    @Test
    void allMessagesOK() {
        IllegalArgumentException e1 = new IllegalArgumentException("foo");
        IllegalArgumentException e2 = new IllegalArgumentException("bar", e1);

        assertEquals("bar---foo", allMessages(e2, "---"));
    }

    @Test
    void allMessagesOrClassNameOK() {
        IllegalArgumentException e1 = new IllegalArgumentException("foo");
        IllegalArgumentException e2 = new IllegalArgumentException("bar", e1);

        assertEquals("bar:\nfoo", allMessagesOrClassName(e2));

        IllegalStateException e3 = new IllegalStateException();

        assertEquals("java.lang.IllegalStateException", allMessagesOrClassName(e3, "---"));
    }

}
