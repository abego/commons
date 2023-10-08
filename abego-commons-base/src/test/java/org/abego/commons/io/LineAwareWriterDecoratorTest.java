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
import java.io.StringWriter;
import java.io.Writer;

import static org.abego.commons.io.FileUtil.tempFileForRun;
import static org.abego.commons.io.WriterUtil.writer;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LineAwareWriterDecoratorTest {

    @Test
    void case_mixedLineSeparators() throws IOException {
        helper("a\nb\r\nc\rd", "1: a\n2: b\r\n3: c\r4: d");
    }

    @Test
    void case_endWithNewline() throws IOException {
        helper("a\n", "1: a\n");
    }

    @Test
    void case_endWithCarriageReturn() throws IOException {
        helper("a\r", "1: a\r");
    }

    @Test
    void case_endWithCarriageReturnNewline() throws IOException {
        helper("a\r\n", "1: a\r\n");
    }

    @Test
    void case_emptyText() throws IOException {
        helper("", "");
    }

    @Test
    void flush_ok() throws IOException {
        File file = tempFileForRun();
        Writer fileWriter = writer(file);

        try (WriterWithLineNumbers writer = new WriterWithLineNumbers(fileWriter)) {

            assertEquals(fileWriter, writer.originalWriter());

            writer.write("a\nb\r\nc\rd");
            writer.flush(); // after flush the text is written to file

            assertEquals("1: a\n2: b\r\n3: c\r4: d", FileUtil.textOf(file));
        }
    }

    @Test
    void lineIndex_ok() throws IOException {
        StringWriter output = new StringWriter();

        try (WriterWithLineNumbers writer = new WriterWithLineNumbers(output)) {

            assertEquals(0, writer.lineIndex());

            writer.write("a\n");

            assertEquals(1, writer.lineIndex());

            writer.write("ä\rñ\r\n");

            assertEquals(3, writer.lineIndex());
        }
    }

    private void helper(String s, String s2) throws IOException {
        File file = tempFileForRun();
        Writer fileWriter = writer(file);

        WriterWithLineNumbers writer = new WriterWithLineNumbers(fileWriter);

        assertEquals(fileWriter, writer.originalWriter());

        writer.write(s);
        writer.close();

        assertEquals(s2, FileUtil.textOf(file));
    }

    private static class WriterWithLineNumbers
            extends LineAwareWriterDecorator {
        private int lineNumber = 1;

        WriterWithLineNumbers(Writer originalWriter) {
            super(originalWriter);
        }

        @Override
        public void processLineSeparator(String lineSeparator)
                throws IOException {
            super.processLineSeparator(lineSeparator);
            lineNumber++;
        }

        @Override
        public void processLineContent(char[] characterArray,
                                       int startOffset, int length)
                throws IOException {
            //noinspection resource
            originalWriter().write(Integer.toString(lineNumber));
            //noinspection resource
            originalWriter().write(": ");
            super.processLineContent(characterArray, startOffset, length);
        }
    }
}
