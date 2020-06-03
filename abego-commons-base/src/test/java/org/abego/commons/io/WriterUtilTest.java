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
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.abego.commons.TestData.SAMPLE_TEXT;
import static org.abego.commons.TestData.SAMPLE_TEXT_2;
import static org.abego.commons.io.FileUtil.file;
import static org.abego.commons.io.FileUtil.tempDirectoryForRun;
import static org.abego.commons.io.FileUtil.tempFileForRun;
import static org.abego.commons.io.FileUtil.textOf;
import static org.abego.commons.io.FileUtil.textOfFile;
import static org.abego.commons.io.WriterUtil.writer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WriterUtilTest {


    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, WriterUtil::new);
    }

    @Test
    void writer_File_ok() throws IOException {
        File file = tempFileForRun();

        Writer writer = writer(file);

        writer.write(SAMPLE_TEXT);
        writer.close();

        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void write_IOException(@TempDir File dir) {
        // force an IO exception by writing to a file that is a directory.
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () ->
                WriterUtil.write(dir, "foo", UTF_8));

        assertEquals(String.format("Error when creating Writer for '%s'",
                dir.getAbsolutePath()), e.getMessage());
    }

    @Test
    void write_UnsupportedCharsetException(@TempDir File dir) {
        UnsupportedCharsetException e =
                assertThrows(UnsupportedCharsetException.class, () ->
                        WriterUtil.write(dir, "foo", "Charset-Foo"));

        assertEquals("Charset-Foo", e.getMessage());
    }

    @Test
    void writer_File_Charset_ok() throws IOException {
        File file = tempFileForRun();

        Writer writer = writer(file, UTF_8);

        writer.write(SAMPLE_TEXT);
        writer.close();

        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void writer_FileInSubDirIsMissing_ok() throws IOException {
        File dir = tempDirectoryForRun();
        File file = new File(file(dir, "subDir"), "file1.txt");
        Writer writer = writer(file);

        writer.write(SAMPLE_TEXT);
        writer.close();

        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void writer_File_String_ok() throws IOException {
        File file = tempFileForRun();

        Writer writer = writer(file, UTF_8.name());

        writer.write(SAMPLE_TEXT);
        writer.close();

        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void write_ok() {
        File file = tempFileForRun();

        WriterUtil.write(file, SAMPLE_TEXT);

        assertEquals(SAMPLE_TEXT, textOfFile(file.getAbsolutePath()));
    }

    @Test
    void write_overwrite() {
        File file = tempFileForRun();

        WriterUtil.write(file, SAMPLE_TEXT);
        WriterUtil.write(file, SAMPLE_TEXT_2);

        assertEquals(SAMPLE_TEXT_2, textOfFile(file.getAbsolutePath()));
    }

    @Test
    void write_Charset_ok() {
        File file = tempFileForRun();
        @NonNull Charset sampleCharset = StandardCharsets.ISO_8859_1;
        WriterUtil.write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT,
                textOfFile(file.getAbsolutePath(), sampleCharset));
    }

    @Test
    void write_CharsetName_ok() {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        WriterUtil.write(file, SAMPLE_TEXT, sampleCharset.name());

        assertEquals(SAMPLE_TEXT,
                textOfFile(file.getAbsolutePath(), sampleCharset.name()));
    }

}