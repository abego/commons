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

import java.io.ByteArrayOutputStream;
import java.io.UncheckedIOException;

import static org.abego.commons.TestData.SAMPLE_TEXT;
import static org.abego.commons.io.PrintStreamToBuffer.newPrintStreamToBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrintStreamToBufferTest {

    @Test
    void case_happyPath() {
        try (PrintStreamToBuffer stream = newPrintStreamToBuffer()) {

            // printed text can be accessed via #getText (and #toString)
            stream.print(SAMPLE_TEXT);

            assertEquals(SAMPLE_TEXT, stream.text());
            assertEquals(SAMPLE_TEXT, stream.toString());

            // printing more text appends it to the old one
            stream.print(SAMPLE_TEXT);

            assertEquals(SAMPLE_TEXT + SAMPLE_TEXT, stream.text());
        }
    }

    @Test
    void forceException_forBetterCodeCoverage() {
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () ->
                newPrintStreamToBuffer(
                        new ByteArrayOutputStream(), "unknown encoding"));
        assertEquals("java.io.UnsupportedEncodingException: unknown encoding",
                e.getMessage());
    }
}
