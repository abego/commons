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

import org.eclipse.jdt.annotation.Nullable;

import java.io.IOException;
import java.io.Writer;

/**
 * A {@link Writer} decorator that replaces the line separators in the original
 * text with a given replacement string.
 */
public class LineSeparatorsReplacingWriterDecorator
        extends LineAwareWriterDecorator {
    private final String lineSeparatorReplacement;

    private LineSeparatorsReplacingWriterDecorator(
            Writer originalWriter, String lineSeparatorReplacement) {
        super(originalWriter);
        this.lineSeparatorReplacement = lineSeparatorReplacement;
    }

    public static Writer lineSeparatorsReplacingWriterDecorator(
            Writer writer, @Nullable String lineSeparatorReplacement) {
        return lineSeparatorReplacement != null ?
                new LineSeparatorsReplacingWriterDecorator(
                        writer,
                        lineSeparatorReplacement) : writer;
    }

    @Override
    protected void processLineSeparator(String lineSeparator)
            throws IOException {
        //noinspection resource
        originalWriter().write(lineSeparatorReplacement);
    }
}
