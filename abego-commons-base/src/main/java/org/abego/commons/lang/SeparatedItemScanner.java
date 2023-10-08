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

package org.abego.commons.lang;

/**
 * Scans a given text for non-empty `items`, separated by definable separator
 * characters.
 * <p>
 * Each call to {@link #nextItem()} return the next item or an empty String when
 * there are no items. Text with enclosing "&lt;...&gt;" is treated as a single
 * item (also considering nested "&lt;...&gt;").
 * <p>
 * When not defined explicitly with {@link #newSeparatedItemScanner(String, CharPredicate)}
 * the separator characters are white spaces and comma.
 */
public class SeparatedItemScanner {
    private final String text;
    private final CharPredicate isSeparator;
    private int pos = 0;

    private SeparatedItemScanner(String text, CharPredicate isSeparator) {
        this.isSeparator = isSeparator;
        this.text = text;
    }

    public static SeparatedItemScanner newSeparatedItemScanner(String text, CharPredicate isSeparator) {
        return new SeparatedItemScanner(text, isSeparator);
    }

    public static SeparatedItemScanner newSeparatedItemScanner(String text) {
        //noinspection MagicCharacter
        return new SeparatedItemScanner(text, ch -> Character.isSpaceChar(ch) || ch == ',');
    }

    @SuppressWarnings("MagicCharacter")
    public String nextItem() {
        skipSeparator();
        int startPos = pos;
        int level = 0;
        while (hasNextChar() && (!isSeparator(peekChar()) || level > 0)) {
            char c = nextChar();
            if (c == '<') {
                level++;
            } else if (c == '>') {
                level--;
            }
        }
        return text.substring(startPos, pos);
    }

    private boolean hasNextChar() {
        return pos < text.length();
    }

    private char nextChar() {
        return text.charAt(pos++);
    }

    private void skipSeparator() {
        while (hasNextChar() && isSeparator(peekChar())) {
            skipChar();
        }
    }

    private void skipChar() {
        pos++;
    }

    private char peekChar() {
        return text.charAt(pos);
    }

    private boolean isSeparator(char ch) {
        return isSeparator.test(ch);
    }

    public interface CharPredicate {
        boolean test(char ch);
    }
}
