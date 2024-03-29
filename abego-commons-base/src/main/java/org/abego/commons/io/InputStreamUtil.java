/*
 * MIT License
 *
 * Copyright (c) 2022 Udo Borkowski, (ub@abego.org)
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
import org.abego.commons.text.LineProcessor;
import org.abego.commons.util.ScannerUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.abego.commons.io.FileUtil.runIOCode;

public final class InputStreamUtil {

    InputStreamUtil() {
        throw new MustNotInstantiateException();
    }

    // --- Factories ---

    public static InputStream newInputStream(String text, Charset charset) {
        return new ByteArrayInputStream(text.getBytes(charset));
    }

    public static InputStream newInputStream(String text) {
        return newInputStream(text, StandardCharsets.UTF_8);
    }

    // --- Queries ---

    public static String textOf(InputStream inputStream) {
        return textOf(inputStream, StandardCharsets.UTF_8);
    }

    public static String textOf(InputStream inputStream, String charsetName) {
        return ScannerUtil.textOf(() -> new Scanner(inputStream, charsetName));
    }

    public static String textOf(InputStream inputStream, Charset charset) {
        return textOf(inputStream, charset.name());
    }

    // --- Commands ---

    /**
     * Write the content of the <code>inputStream</code> to <code>file</code>.
     */
    public static void write(InputStream inputStream, File file) {
        runIOCode(() -> copy(inputStream, file.toPath(), REPLACE_EXISTING));
    }

    private static final int COPY_STREAM_BUFFER_SIZE = 4096;

    /**
     * Reads the (UTF-8 encoded) {@code inputStream} line-by-line and passes
     * the lines to the {@code lineProcessor}.
     * <p>
     * The {@code lineProcessor} is also informed when the reading starts/ends.
     * <p>
     *
     * @deprecated Use {@link org.abego.commons.lineprocessing.LineProcessing} instead.
     */
    @Deprecated
    public static void readLineWise(InputStream inputStream, LineProcessor lineProcessor) throws IOException {
        lineProcessor.start();
        int lineNumber = 0;
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(
                             inputStream, StandardCharsets.UTF_8))) {
            String line;
            do {
                line = reader.readLine();
                if (line != null) {
                    lineNumber++;
                    lineProcessor.processLine(line, lineNumber);
                }
            } while (line != null);
        }
        lineProcessor.end(lineNumber);
    }

    public static void copyStream(InputStream is, OutputStream os) {
        try {
            byte[] buf = new byte[COPY_STREAM_BUFFER_SIZE];

            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


}
