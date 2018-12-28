/*
 * MIT License
 *
 * Copyright (c) 2018 Udo Borkowski, (ub@abego.org)
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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import static org.abego.commons.lang.exception.MustNotInstantiateException.throwMustNotInstantiate;

public class PrintWriterUtil {

    PrintWriterUtil() {
        throwMustNotInstantiate();
    }

    /**
     * Return a {@link PrintWriter} for the {@code file} and {@code charset}.
     *
     * <p>Ensure the directory containing the file exists.</p>
     */
    static PrintWriter printWriter(File file, Charset charset)
            throws IOException {
        return printWriter(file, charset.name());
    }

    /**
     * Return a {@link PrintWriter} for the {@code file} and a {@link Charset}
     * named {@code charsetName}.
     *
     * <p>Ensure the directory containing the file exists.</p>
     */
    static PrintWriter printWriter(File file, String charsetName)
            throws IOException {
        File dir = file.getParentFile();

        FileUtil.ensureDirectoryExists(dir);

        return new PrintWriter(file, charsetName);
    }

    /**
     * Return a  {@link PrintWriter} for the {@code outputStream} and
     * {@code charset}.
     */
    static PrintWriter printWriter(OutputStream outputStream, Charset charset) {
        return new PrintWriter(
                new OutputStreamWriter(outputStream, charset), true);
    }
}
