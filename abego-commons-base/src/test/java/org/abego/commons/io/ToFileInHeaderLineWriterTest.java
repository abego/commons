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

package org.abego.commons.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import static org.abego.commons.io.FileUtil.file;
import static org.abego.commons.io.FileUtil.tempDirectoryForRun;
import static org.abego.commons.io.FileUtil.tempFileForRun;
import static org.abego.commons.io.ToFileInHeaderLineWriter.toFileInHeaderLineWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ToFileInHeaderLineWriterTest {

    @Test
    void case_happyPath() throws IOException {
        File file = tempFileForRun();
        String text =
                "hello\nworld!\r"; // end with \r to test special case code

        Writer writer = toFileInHeaderLineWriter();

        writer.write(file.getAbsolutePath());
        writer.write("\n");
        writer.write(text);
        writer.close();

        assertEquals(text, FileUtil.textOf(file));
    }

    @Test
    void flush_ok() throws IOException {
        File file = tempFileForRun();
        String text = "hello\nworld!";

        try (Writer writer = toFileInHeaderLineWriter()) {
            writer.write(file.getAbsolutePath());
            writer.write("\n");
            writer.write(text);

            writer.flush();
        }

        assertEquals(text, FileUtil.textOf(file));
    }

    @Test
    void close_missingFileInHeader() {
        IOException e = assertThrows(IOException.class, () -> {
            //noinspection EmptyTryBlock
            try (Writer ignored = toFileInHeaderLineWriter()) {
                // intentionally empty
            }
        });
        assertEquals(
                "No file found in first line of output.",
                e.getMessage());
    }

    @Test
    void bug_ToFileInHeaderLineWriter_fails_when_directory_for_output_is_missing() throws IOException {
        File dir = tempDirectoryForRun();

        File file = file(dir, "sub/dir/hello.txt");
        String text = "hello\nworld!";

        Writer writer = toFileInHeaderLineWriter();

        writer.write(file.getAbsolutePath());
        writer.write("\n");
        writer.write(text);
        writer.close();

        assertEquals(text, FileUtil.textOf(file));
    }
}
