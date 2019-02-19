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

package org.abego.commons.lang;

import org.abego.commons.io.PrintStreamToBuffer;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.abego.commons.io.PrintStreamToBuffer.printStreamToBuffer;
import static org.abego.commons.lang.SystemUtil.systemErrRedirect;
import static org.abego.commons.lang.SystemUtil.systemOutRedirect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SystemUtilTest {

    private static final String FOO = "foo";

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, SystemUtil::new);
    }

    @Test
    void systemOutRedirect_ok() {
        PrintStreamToBuffer myPrintStream = printStreamToBuffer();
        PrintStream oldStream = System.out;

        RunOnClose r = systemOutRedirect(myPrintStream);

        System.out.print(FOO);

        assertEquals(FOO, myPrintStream.text());
        assertEquals(myPrintStream, System.out);

        r.close();

        assertEquals(oldStream, System.out);
    }

    @Test
    void systemErrRedirect_ok() {
        PrintStreamToBuffer myPrintStream = printStreamToBuffer();
        PrintStream oldStream = System.err;

        RunOnClose r = systemErrRedirect(myPrintStream);

        System.err.print(FOO);

        assertEquals(FOO, myPrintStream.text());
        assertEquals(myPrintStream, System.err);

        r.close();

        assertEquals(oldStream, System.err);
    }
}