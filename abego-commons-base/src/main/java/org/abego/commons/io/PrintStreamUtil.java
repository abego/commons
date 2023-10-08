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

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.abego.commons.io.BufferedReaderUtil.newBufferedReader;
import static org.abego.commons.io.FileUtil.runIOCode;

public final class PrintStreamUtil {

    PrintStreamUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Read the inputStream line by line and append the lines to the printStream.
     */
    public static void appendLines(PrintStream printStream, InputStream inputStream) {
        runIOCode(() -> {
            try (BufferedReader stdInput = newBufferedReader(inputStream)) {
                String s;
                while ((s = stdInput.readLine()) != null) {
                    printStream.println(s);
                }
            }
        });
    }

    public static PrintStream newPrintStream(OutputStream outputStream, Charset charset) {
        return runIOCode(() -> new PrintStream(outputStream, false, charset.name()));
    }

    public static PrintStream newPrintStream(OutputStream outputStream) {
        return newPrintStream(outputStream, StandardCharsets.UTF_8);
    }

    public static PrintStream newPrintStreamToBufferedFile(File file, Charset charset) {
        return runIOCode(() -> new PrintStream(
                new BufferedOutputStream(Files.newOutputStream(file.toPath())),
                false, charset.name()));
    }

    public static PrintStream newPrintStreamToBufferedFile(File file) {
        return newPrintStreamToBufferedFile(file, StandardCharsets.UTF_8);
    }

    /**
     * Returns a new PrintStream that behaves like a
     * <a href="https://en.wikipedia.org/wiki/Null_device">Null device</a>, i.e.
     * discards all data written to it.
     */
    public static PrintStream newPrintStreamToNullDevice() {
        return newPrintStream(new OutputStream() {
            public void write(int b) {
                // do nothing
            }
        });
    }
}
