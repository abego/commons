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

package org.abego.commons.lang.exception;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.abego.commons.lang.exception.UncheckedException.newUncheckedException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UncheckedExceptionTest {

    @Test
    void uncheckedException_ok() {
        String message = "aMessage";
        ParseException ex = new ParseException(message, 1);
        UncheckedException uncheckedException = UncheckedException.newUncheckedException(ex);

        assertEquals(ex, uncheckedException.getCause());
        assertEquals(message, uncheckedException.getMessage());
    }

    @Test
    void uncheckedException_withMessageAndException() {
        String message = "outerMessage";
        ParseException ex = new ParseException("innerMessage", 1);
        UncheckedException uncheckedException = UncheckedException.newUncheckedException(message, ex);

        assertEquals(ex, uncheckedException.getCause());
        assertEquals(message, uncheckedException.getMessage());
    }

    @Test
    void uncheckedException_withMessage() {
        String message = "messageText";
        UncheckedException uncheckedException = newUncheckedException(message);

        assertNull(uncheckedException.getCause());
        assertEquals(message, uncheckedException.getMessage());
    }
}