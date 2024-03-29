/*
 * MIT License
 *
 * Copyright (c) 2021 Udo Borkowski, (ub@abego.org)
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.abego.commons.io.ByteArrayOutputStreamUtil.textOf;

/**
 * A PrintStream that prints to an internal buffer.
 *
 * <p>Use {@link #text()} to retrieve the printed text.</p>
 */
public final class PrintStreamToBuffer extends PrintStream {
    private final ByteArrayOutputStream outputStream;

    private PrintStreamToBuffer(
            ByteArrayOutputStream outputStream, String encoding)
            throws IOException {
        super(outputStream, true, encoding);
        this.outputStream = outputStream;
    }

    static PrintStreamToBuffer newPrintStreamToBuffer(
            ByteArrayOutputStream outputStream, String encoding) {
        try {
            return new PrintStreamToBuffer(outputStream, encoding);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static PrintStreamToBuffer newPrintStreamToBuffer() {
        return newPrintStreamToBuffer(new ByteArrayOutputStream(), StandardCharsets.UTF_8.name());
    }

    public String text() {
        return textOf(outputStream);
    }

    @Override
    public String toString() {
        return text();
    }
}
