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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class WriterUtil {

    WriterUtil() {
        throw new MustNotInstantiateException();
    }

    // --- Factories ---

    public static Writer writer(File file) {

        return writer(file, StandardCharsets.UTF_8);
    }

    public static Writer writer(File file, Charset charset) {
        try {
            FileUtil.ensureFileExists(file);
            return new OutputStreamWriter(new FileOutputStream(file), charset);
        } catch (FileNotFoundException e) {
            //noinspection DuplicateStringLiteralInspection
            throw new UncheckedIOException(
                    String.format(
                            "Error when creating Writer for '%s'", //NON-NLS
                            file.getAbsolutePath()),
                    e);
        }
    }

    public static Writer writer(File file, String charsetName) {

        return writer(file, Charset.forName(charsetName));
    }


    /**
     * Write {@code text} to {@code file} using the UTF-8 charset.
     */
    public static void write(final File file, final String text) {
        write(file, text, StandardCharsets.UTF_8);
    }

    /**
     * Write {@code text} to {@code file} using {@code charset}.
     */
    public static void write(
            final File file,
            final String text,
            final Charset charset) {

        write(file, text, charset.name());
    }

    /**
     * Write {@code text} to {@code file} using the {@link Charset} named
     * <code>charsetName</code>.
     */
    public static void write(
            final File file,
            final String text,
            final String charsetName) {

        try (Writer output = writer(file, charsetName)) {
            output.write(text);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
