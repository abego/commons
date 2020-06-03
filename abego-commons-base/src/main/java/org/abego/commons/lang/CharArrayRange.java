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

package org.abego.commons.lang;

public final class CharArrayRange {

    private final char[] charArray;
    private final int startOffset;
    private final int length;

    private CharArrayRange(char[] charArray, int startOffset, int length) {
        this.charArray = charArray;
        this.startOffset = startOffset;
        this.length = length;

        if (startOffset < 0) {
            throw new IllegalArgumentException("startOffset must be >= 0"); // NON-NLS
        }
        if (length < 0) {
            throw new IllegalArgumentException("length must be >= 0"); // NON-NLS
        }
    }

    public static CharArrayRange charArrayRange(
            char[] charArray, int startOffset, int length) {
        return new CharArrayRange(charArray, startOffset, length);
    }

    public char[] charArray() {
        return charArray;
    }

    public int startOffset() {
        return startOffset;
    }

    public int length() {
        return length;
    }

    public String text() {
        return String.valueOf(charArray, startOffset, length);
    }

    public String toString() {
        return text();
    }
}
