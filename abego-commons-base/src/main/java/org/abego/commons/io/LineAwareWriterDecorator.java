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


import java.io.IOException;
import java.io.Writer;

/**
 * A writer that handles the text as a sequence of <em>lines</em>, separated by
 * <em>line separators</em>.
 *
 * <p>A line separator may be "\n", "\r" or "\r\n". </p>
 *
 * <p>Subclasses typically override the methods
 * {@link #processLineContent(char[], int, int)} and/or
 * {@link #processLineSeparator(String)}.</p>
 *
 * <p>This class is designed to be used as a
 * <a href="https://en.wikipedia.org/wiki/Decorator_pattern">decorator</a>
 * for a {@link Writer} object. {@link #originalWriter()} returns this
 * Writer.</p>
 */
public class LineAwareWriterDecorator extends LineSplittingWriter {
    private final Writer originalWriter;

    protected LineAwareWriterDecorator(Writer originalWriter) {
        this.originalWriter = originalWriter;
    }

    public Writer originalWriter() {
        return originalWriter;
    }


    @Override
    public void flush() throws IOException {
        originalWriter().flush();
    }

    @Override
    public void close() throws IOException {
        super.close();

        originalWriter().close();
    }

    /**
     * Called when line content is detected.
     *
     * <p>Can be called more than once per line.</p>
     *
     * <p>The default implementation writes the line content (defined by
     * {@code characterArray}, {@code startOffset} and {@code length}) with
     * the original writer.</p>
     */
    @Override
    protected void processLineContent(
            char[] characterArray, int startOffset, int length)
            throws IOException {
        originalWriter().write(characterArray, startOffset, length);
    }

    /**
     * Called when a line separator is detected.
     *
     * <p>The default implementation writes the {@code lineSeparator}
     * with the original writer.</p>
     */
    @Override
    protected void processLineSeparator(String lineSeparator)
            throws IOException {
        originalWriter().write(lineSeparator);
    }
}
