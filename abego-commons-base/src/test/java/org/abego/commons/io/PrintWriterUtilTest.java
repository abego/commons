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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.abego.commons.TestData.SAMPLE_TEXT;
import static org.abego.commons.io.FileUtil.tempDirectoryForRun;
import static org.abego.commons.io.FileUtil.tempFileForRun;
import static org.abego.commons.io.PrintStreamToBuffer.printStreamToBuffer;
import static org.abego.commons.io.PrintWriterUtil.printWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrintWriterUtilTest {


    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, PrintWriterUtil::new);
    }

    @Test
    void printWriter_File_Charset_ok() throws IOException {
        File file = tempFileForRun();

        PrintWriter writer = printWriter(file, UTF_8);
        writer.print(SAMPLE_TEXT);
        writer.close();

        assertEquals(SAMPLE_TEXT, FileUtil.textOf(file));
    }

    @Test
    void printWriter_File_String_ok() throws IOException {
        File file = tempFileForRun();

        PrintWriter writer = printWriter(file, UTF_8.name());
        writer.print(SAMPLE_TEXT);
        writer.close();

        assertEquals(SAMPLE_TEXT, FileUtil.textOf(file));
    }

    @Test
    void printWriter_OutputStream_Charset_ok() {
        PrintStreamToBuffer outputStream = printStreamToBuffer();

        PrintWriter writer = printWriter(outputStream, UTF_8);
        writer.print(SAMPLE_TEXT);
        writer.close();

        assertEquals(SAMPLE_TEXT, outputStream.text());
    }

    @Test
    void printWriter_withMissingDirs() throws IOException {
        File dir = tempDirectoryForRun();

        File file = new File(dir, "sub/dir/text.txt");

        PrintWriter writer = printWriter(file, UTF_8);
        writer.print(SAMPLE_TEXT);
        writer.close();

        assertEquals(SAMPLE_TEXT, FileUtil.textOf(file));
    }
}