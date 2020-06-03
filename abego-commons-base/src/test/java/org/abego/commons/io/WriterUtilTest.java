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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.abego.commons.TestData.SAMPLE_TEXT;
import static org.abego.commons.io.WriterUtil.writer;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WriterUtilTest {


    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, WriterUtil::new);
    }

    @Test
    void writer_File_ok() throws IOException {
        File file = FileUtil.tempFileForRun();

        Writer writer = writer(file);

        writer.write(SAMPLE_TEXT);
        writer.close();

        Assertions.assertEquals(SAMPLE_TEXT, FileUtil.textOf(file));
    }

    @Test
    void writer_File_Charset_ok() throws IOException {
        File file = FileUtil.tempFileForRun();
        Charset charset = StandardCharsets.UTF_8;

        Writer writer = WriterUtil.writer(file, charset);

        writer.write(SAMPLE_TEXT);
        writer.close();

        Assertions.assertEquals(SAMPLE_TEXT, FileUtil.textOf(file));
    }

    @Test
    void writer_File_String_ok() throws IOException {
        File file = FileUtil.tempFileForRun();
        Charset charset = StandardCharsets.UTF_8;

        Writer writer = WriterUtil.writer(file, charset.name());

        writer.write(SAMPLE_TEXT);
        writer.close();

        Assertions.assertEquals(SAMPLE_TEXT, FileUtil.textOf(file));
    }
}