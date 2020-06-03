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


import org.abego.commons.lang.CharArrayRange;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.abego.commons.lang.CharArrayRange.charArrayRange;
import static org.abego.commons.lang.CharacterUtil.CARRIAGE_RETURN_CHAR;
import static org.abego.commons.lang.CharacterUtil.CARRIAGE_RETURN_LINEFEED_STRING;
import static org.abego.commons.lang.CharacterUtil.CARRIAGE_RETURN_STRING;
import static org.abego.commons.lang.CharacterUtil.NEWLINE_CHAR;
import static org.abego.commons.lang.CharacterUtil.NEWLINE_STRING;
import static org.abego.commons.lang.CharacterUtil.isLineSeparatorChar;

/**
 * A writer that recognizes the text as a sequence of <em>lines</em>, separated
 * by <em>line separators</em>.
 *
 * <p>A line separator may be "\n", "\r" or "\r\n". </p>
 */
public abstract class LineSplittingWriter extends Writer {

    private boolean mustProcessCarriageReturn = false;
    private int lineIndex = 0;

    protected LineSplittingWriter() {
        super();
    }

    public int lineIndex() {
        return lineIndex;
    }

    /**
     * Called when line content is detected.
     *
     * <p>Can be called more than once per line.</p>
     */
    protected abstract void processLineContent(
            char[] characterArray, int startOffset, int length)
            throws IOException;

    /**
     * Called when a line separator is detected.
     */
    protected abstract void processLineSeparator(String lineSeparator)
            throws IOException;

    @Override
    public void write(
            char[] characterArray, int startOffset, int length)
            throws IOException {
        LinesInText linesInText =
                new LinesInText(characterArray, startOffset, length);

        handleUnprocessedCarriageReturn(linesInText.isFirstCharNewline());

        for (Line line : linesInText) {
            handleLine(line);
        }

        rememberUnprocessedCarriageReturn(linesInText);
    }

    @Override
    public void close() throws IOException {
        handleUnprocessedCarriageReturn(false);
    }

    private void handleUnprocessedCarriageReturn(boolean isNewLineFollowing)
            throws IOException {
        if (isNewLineFollowing) {
            handleLineSeparator(mustProcessCarriageReturn
                    ? CARRIAGE_RETURN_LINEFEED_STRING : NEWLINE_STRING);
        } else {
            if (mustProcessCarriageReturn) {
                handleLineSeparator(CARRIAGE_RETURN_STRING);
            }
        }
        mustProcessCarriageReturn = false;
    }

    private void handleLine(Line line) throws IOException {
        CharArrayRange lineContent = line.getLineContent();
        processLineContent(lineContent.charArray(),
                lineContent.startOffset(),
                lineContent.length());

        if (line.isWithLineSeparator()) {
            handleLineSeparator(line.getLineSeparator());
        }
    }

    private void rememberUnprocessedCarriageReturn(
            LinesInText linesInText) {
        mustProcessCarriageReturn = linesInText.isLastCharCarriageReturn();
    }

    private void handleLineSeparator(String lineSeparator) throws IOException {
        processLineSeparator(lineSeparator);
        lineIndex++;
    }

    private static class Line {
        private final CharArrayRange lineContent;
        private final String lineSeparator;

        private Line(
                CharArrayRange lineContent, String lineSeparator) {
            this.lineContent = lineContent;
            this.lineSeparator = lineSeparator;
        }

        CharArrayRange getLineContent() {
            return lineContent;
        }

        boolean isWithLineSeparator() {
            return !lineSeparator.isEmpty();
        }

        String getLineSeparator() {
            return lineSeparator;
        }
    }

    private static class LinesInText implements Iterable<Line> {
        private final char[] characterArray;
        private final int startOffset;
        private final int length;

        private LinesInText(
                char[] characterArray, int startOffset, int length) {
            this.characterArray = characterArray;
            this.startOffset = startOffset;
            this.length = length;
        }

        boolean isFirstCharNewline() {
            return length > 0 && characterArray[startOffset] == NEWLINE_CHAR;
        }

        boolean isLastCharCarriageReturn() {
            return length > 0 &&
                    characterArray[startOffset + length - 1] == CARRIAGE_RETURN_CHAR;
        }


        @Override
        @Nonnull
        public Iterator<Line> iterator() {
            return new LinesIterator();
        }

        private class LinesIterator implements Iterator<Line> {
            private final int endIndex =
                    startOffset + length - (isLastCharCarriageReturn() ? 1 : 0);
            private int i = startOffset + (isFirstCharNewline() ? 1 : 0);

            @Override
            public boolean hasNext() {
                return i < endIndex;
            }

            @Override
            public Line next() {
                if (!hasNext()) {
                    // The following line in never executed as all callers of
                    // `next()` (of this private class) are checking for
                    // `hasNext()`. However we need this check to silence
                    // a warning.
                    throw new NoSuchElementException();
                }

                CharArrayRange lineContent = scanLineContent();
                String lineSeparator = scanLineSeparator();
                return new Line(lineContent, lineSeparator);
            }

            private CharArrayRange scanLineContent() {
                int start = i;
                while (hasNext() && !isLineSeparatorChar(peekNextChar())) {
                    i++;
                }
                return charArrayRange(characterArray, start, i - start);
            }

            private String scanLineSeparator() {
                if (consumeChar(NEWLINE_CHAR)) {
                    return NEWLINE_STRING;
                }

                if (consumeChar(CARRIAGE_RETURN_CHAR)) {
                    return consumeChar(NEWLINE_CHAR)
                            ? CARRIAGE_RETURN_LINEFEED_STRING : CARRIAGE_RETURN_STRING;
                }
                return "";
            }

            private char peekNextChar() {
                return i < endIndex ? characterArray[i] : 0;
            }

            private boolean consumeChar(char expectedChar) {
                char c = peekNextChar();
                if (c == expectedChar) {
                    i++;
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
