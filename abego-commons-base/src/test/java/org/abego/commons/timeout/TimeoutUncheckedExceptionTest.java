/*
 * MIT License
 *
 * Copyright (c) 2018 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.timeout;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TimeoutUncheckedExceptionTest {

    @Test
    void constructorOk() {
        String message = "Timeout";
        Throwable cause = new RuntimeException("Cause");
        String messageForCause = "java.lang.RuntimeException: Cause";

        TimeoutUncheckedException e;

        e = new TimeoutUncheckedException();
        assertNull(e.getMessage());
        assertNull(e.getCause());

        e = new TimeoutUncheckedException(message);
        assertEquals(message, e.getMessage());
        assertNull(e.getCause());

        e = new TimeoutUncheckedException(cause);
        assertEquals(messageForCause, e.getMessage());
        assertEquals(cause, e.getCause());

        e = new TimeoutUncheckedException(message, cause);
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getCause());

        e = new TimeoutUncheckedException(message, cause, true, true);
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getCause());
    }
}