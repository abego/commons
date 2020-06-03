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

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.abego.commons.io.FileUtil.ensureDirectoryExists;

/**
 * A writer that handles the first line as a file pathname and writes the
 * remaining lines to the file specified in the first line.
 */
public final class ToFileInHeaderLineWriter extends LineSplittingWriter {

    private final StringBuilder firstLineContent = new StringBuilder();
    private final Charset charset;
    private Writer fileWriter;

    private ToFileInHeaderLineWriter(Charset charset) {
        this.charset = charset;
    }

    public static ToFileInHeaderLineWriter toFileInHeaderLineWriter(
            Charset charset) {
        return new ToFileInHeaderLineWriter(charset);
    }

    public static ToFileInHeaderLineWriter toFileInHeaderLineWriter() {
        return toFileInHeaderLineWriter(StandardCharsets.UTF_8);
    }

    @Override
    protected void processLineContent(char[] characterArray,
                                      int startOffset, int length)
            throws IOException {
        if (lineIndex() == 0) {
            firstLineContent.append(characterArray, startOffset, length);
        } else {
            fileWriter.write(characterArray, startOffset, length);
        }
    }

    @Override
    protected void processLineSeparator(String lineSeparator)
            throws IOException {
        if (lineIndex() == 0) {
            File outputFile = new File(firstLineContent.toString());
            ensureDirectoryExists(outputFile.getParentFile());
            fileWriter = WriterUtil.writer(outputFile, charset);
        } else {
            fileWriter.write(lineSeparator);
        }
    }

    @Override
    public void flush() throws IOException {
        if (fileWriter != null) {
            fileWriter.flush();
        }
    }

    @Override
    public void close() throws IOException {
        super.close();

        if (fileWriter != null) {
            fileWriter.close();
        } else {
            throw new IOException("No file found in first line of output.");  // NON-NLS
        }
    }
}
