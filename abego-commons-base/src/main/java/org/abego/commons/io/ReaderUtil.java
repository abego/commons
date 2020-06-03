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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class ReaderUtil {

    ReaderUtil() {
        throw new MustNotInstantiateException();
    }

    // --- Factories ---

    /**
     * Returns a {@link Reader} for the given {@code file}, assuming its text
     * is encoded using the given {@code charset}.
     */
    public static Reader reader(File file, Charset charset)
            throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(file), charset));
    }

    /**
     * Returns a {@link Reader} for the given {@code file}, assuming its text
     * is encoded using the given {@code charset}.
     *
     * <p>Same as {@link #reader(File, Charset)}, but throws
     * {@link UncheckedIOException}</p>s instead of checked exceptions.
     */
    public static Reader newReader(File file, Charset charset) {
        try {
            return reader(file, charset);
        } catch (FileNotFoundException e) {
            //noinspection DuplicateStringLiteralInspection
            throw new UncheckedIOException(
                    String.format(
                            "Error when creating reader for file '%s'", //NON-NLS
                            file.getAbsolutePath()),
                    e);
        }
    }

    /**
     * Returns a {@link Reader} for the given {@code file}, assuming its text
     * is UTF-8 encoded.
     *
     * <p>Similar to {@link #reader(File, Charset)}, but throws
     * {@link UncheckedIOException}</p>s instead of checked exceptions.
     */
    public static Reader newReader(File file) {
        return newReader(file, StandardCharsets.UTF_8);
    }

    /**
     * Returns a {@link Reader} for the given {@code text}.
     */
    public static Reader newReader(String text) {
        return new StringReader(text);
    }
}
