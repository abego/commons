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

package org.abego.commons.lang;

import org.abego.commons.lang.exception.MustNotInstantiateException;

public final class CharacterUtil {

    public static final char NEWLINE_CHAR = '\n'; // NON-NLS
    public static final char CARRIAGE_RETURN_CHAR = '\r'; // NON-NLS
    public static final char BACKSLASH_CHAR = '\\'; // NON-NLS
    public static final char BEL_CHAR = '\b'; // NON-NLS
    public static final char DOUBLE_QUOTE_CHAR = '"'; // NON-NLS
    public static final char FORM_FEED_CHAR = '\f'; // NON-NLS
    public static final char SINGLE_QUOTE_CHAR = '\''; // NON-NLS
    public static final char TAB_CHAR = '\t'; // NON-NLS
    public static final String CARRIAGE_RETURN_LINEFEED_STRING = "\r\n"; // NON-NLS
    public static final String NEWLINE_STRING = "\n"; // NON-NLS
    public static final String CARRIAGE_RETURN_STRING = "\r"; // NON-NLS
    public static final int FIRST_PRINTABLE_ASCII_CHAR_VALUE = 32;
    public static final int LAST_PRINTABLE_ASCII_CHAR_VALUE = 127;

    CharacterUtil() {
        throw new MustNotInstantiateException();
    }

    public static boolean isLineSeparatorChar(char c) {
        return c == NEWLINE_CHAR || c == CARRIAGE_RETURN_CHAR;
    }


}
