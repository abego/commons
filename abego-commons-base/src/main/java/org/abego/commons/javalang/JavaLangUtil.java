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
package org.abego.commons.javalang;

import static java.lang.Character.isJavaIdentifierPart;
import static java.lang.Character.isJavaIdentifierStart;
import static org.abego.commons.lang.exception.MustNotInstantiateException.throwMustNotInstantiate;

public class JavaLangUtil {

    /**
     * The String used instead of "invalid" characters when converting a text
     * to a Java identifier.
     *
     * <p>See {@link #toJavaIdentifier(String)}</p>
     */
    public static final String INVALID_CHAR_REPLACEMENT_STRING = "_"; // NON-NLS

    JavaLangUtil() {
        throwMustNotInstantiate();
    }

    public static boolean isValidJavaIdentifierCharButNotAtStart(
            char character) {
        return isJavaIdentifierPart(character) &&
                !isJavaIdentifierStart(character);
    }

    public static boolean isJavaIdentifier(String text) {
        if (text.isEmpty() || !isJavaIdentifierStart(text.charAt(0))) {
            return false;
        }

        char[] chars = text.toCharArray();

        // all characters from 1 .. end must be JavaIdentifierPart chars
        for (int i = 1; i < chars.length; i++) {
            if (!isJavaIdentifierPart(chars[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Convert the text into a valid Java identifier.
     *
     * <p>Invalid characters are replaced by '_'. The empty text is converted
     * into a single "_". When the text starts with a valid Java identifier
     * character but is not a valid start character the result starts with an
     * extra '_'.</p>
     *
     * <p>Note: different texts may result in the same Java identifier.</p>
     */
    public static String toJavaIdentifier(String text) {
        if (text.isEmpty()) {
            return INVALID_CHAR_REPLACEMENT_STRING;
        }

        boolean isValidCharButNotAtStart =
                isValidJavaIdentifierCharButNotAtStart(text.charAt(0));
        StringBuilder result =
                new StringBuilder(isValidCharButNotAtStart
                        ? INVALID_CHAR_REPLACEMENT_STRING : "");

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            result.append(isJavaIdentifierPart(ch)
                    ? ch : INVALID_CHAR_REPLACEMENT_STRING);
        }

        return result.toString();
    }

}
