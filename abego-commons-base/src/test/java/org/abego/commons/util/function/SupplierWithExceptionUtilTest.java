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

package org.abego.commons.util.function;

import org.abego.commons.lang.exception.UncheckedException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.abego.commons.util.function.SupplierWithException.unchecked;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SupplierWithExceptionUtilTest {

    private static final String SUPPLIED_VALUE = "ok";

    @Test
    void unchecked_ok() {
        GetOnlyOnceSupplier supplier = new GetOnlyOnceSupplier();

        assertEquals(SUPPLIED_VALUE, unchecked(supplier));

        // the second access throws an exception
        UncheckedException e = assertThrows(
                UncheckedException.class, () -> unchecked(supplier));

        assertEquals(ParseException.class, e.getCause().getClass());
    }

    private static class GetOnlyOnceSupplier
            implements SupplierWithException<String, ParseException> {

        boolean firstCall = true;

        @Override
        public String get() throws ParseException {
            if (firstCall) {
                firstCall = false;
                return SUPPLIED_VALUE;
            } else {
                throw new ParseException("Test", 0);
            }
        }
    }
}