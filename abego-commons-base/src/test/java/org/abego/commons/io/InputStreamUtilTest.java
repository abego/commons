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

import org.abego.commons.TestData;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.abego.commons.TestData.EMPTY_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.EMPTY_TXT_TEXT;
import static org.abego.commons.TestData.SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_ISO_8859_1_TXT_TEXT;
import static org.abego.commons.TestData.SAMPLE_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_TXT_TEXT;
import static org.abego.commons.io.FileUtil.tempFileForRun;
import static org.abego.commons.io.InputStreamUtil.newInputStream;
import static org.abego.commons.io.InputStreamUtil.readLineWise;
import static org.abego.commons.io.InputStreamUtil.textOf;
import static org.abego.commons.io.InputStreamUtil.write;
import static org.abego.commons.lang.ClassUtil.resourceAsStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputStreamUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, InputStreamUtil::new);
    }

    @Test
    void textOf_ok() {

        InputStream inputStream = resourceAsStream(TestData.class, SAMPLE_TXT_RESOURCE_NAME);

        assertEquals(SAMPLE_TXT_TEXT, textOf(inputStream));
    }

    @Test
    void textOf_withEmptyStream() {
        InputStream inputStream = resourceAsStream(TestData.class, EMPTY_TXT_RESOURCE_NAME);

        assertEquals(EMPTY_TXT_TEXT, textOf(inputStream));
    }

    @Test
    void textOf_withCharset() {
        InputStream inputStream = resourceAsStream(TestData.class, SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME);

        assertEquals(SAMPLE_ISO_8859_1_TXT_TEXT,
                textOf(inputStream, StandardCharsets.ISO_8859_1));
    }

    @Test
    void textOf_withCharsetName() {
        InputStream inputStream = resourceAsStream(TestData.class, SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME);

        assertEquals(SAMPLE_ISO_8859_1_TXT_TEXT,
                textOf(inputStream, StandardCharsets.ISO_8859_1.name()));
    }

    @Test
    void write_ok() {
        InputStream inputStream = resourceAsStream(TestData.class, SAMPLE_TXT_RESOURCE_NAME);
        File file = tempFileForRun();

        write(inputStream, file);

        assertEquals(SAMPLE_TXT_TEXT, FileUtil.textOf(file));
    }

    @NonNull
    private static String generateSampleText(int size) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (builder.length() < size) {
            builder.append(i);
            builder.append("\n");
        }
        return builder.toString();
    }

    @Test
    void readLineWiseSmokeTest() throws IOException {
        InputStream in = newInputStream("foo\nbar");
        StringBuilder out = new StringBuilder();
        readLineWise(in, (line, lineNumber) ->
                out.append(String.format("%d: %s\n", lineNumber, line)));

        assertEquals("1: foo\n2: bar\n", out.toString());
    }

    @Test
    void copyStream() {
        // the text to copy should have a certain size to ensure it does
        // not fit into the internal buffer of copyStream.
        String text = generateSampleText(10000);
        InputStream input = newInputStream(text);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        InputStreamUtil.copyStream(input, output);

        assertEquals(text, ByteArrayOutputStreamUtil.textOf(output));
    }
}
