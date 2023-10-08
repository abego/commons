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

package org.abego.commons.text;

import java.io.InputStream;

/**
 * Used for "line-by-line" text processing.
 * <p>
 *
 * @deprecated Use {@link org.abego.commons.lineprocessing.LineProcessing} instead.
 */
@Deprecated
public interface LineProcessor {

    /**
     * Called when a {@code line} of text needs to be processed, also giving
     * the number of the line within the text (1-based).
     * <p>
     * Line separators are not included in the {@code line}.
     * <p>
     * One can assume a LineProcessor is called with increasing lineNumbers.
     * <p>
     * See {@link org.abego.commons.io.InputStreamUtil#readLineWise(InputStream, LineProcessor)}.
     */
    void processLine(String line, int lineNumber);

    /**
     * Called before processing starts
     */
    default void start() {
    }

    /**
     * Called after all lines are processed, also providing the total number
     * of lines processed.
     */
    default void end(@SuppressWarnings("unused") int lineCount) {
    }
}
