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

package org.abego.commons.io;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.abego.commons.io.InputStreamUtil.newInputStream;
import static org.abego.commons.io.PrintStreamUtil.newPrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrintStreamUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, PrintStreamUtil::new);
    }

    @Test
    void appendLines_OK() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream stream = newPrintStream(out);
        PrintStreamUtil.appendLines(stream, newInputStream("a\nbc\ndef\n"));
        PrintStreamUtil.appendLines(stream, newInputStream("a\nbc\ndef\n"));

        assertEquals("a\nbc\ndef\na\nbc\ndef\n", out.toString());
    }

    @Test
    void newPrintStreamToNullDevice_OK() {
        PrintStream stream = PrintStreamUtil.newPrintStreamToNullDevice();

        stream.println("foo");

        assertFalse(stream.checkError());
    }
}