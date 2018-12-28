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

import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.abego.commons.lang.exception.MustNotInstantiateException.throwMustNotInstantiate;

public class StringUtil {

    private static final Pattern ESCAPED_CHAR = Pattern.compile("\\\\(.)");

    StringUtil() {
        throwMustNotInstantiate();
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Return true if {@code s} has text, i.e. is not {@code null}
     * and is not the empty string.
     */
    public static boolean hasText(String s) {
        return !isNullOrEmpty(s);
    }

    /**
     * Return {@code string} when it is a non-empty String. Return
     * {@code defaultValue} otherwise, i.e. when {@code string} is
     * {@code null} or empty.
     */
    public static String stringOrDefault(
            String string,
            String defaultValue) {

        return string == null || string.isEmpty() ? defaultValue : string;
    }

    /**
     * Return {@code string} when it is a non-empty {@link String}. Return
     * the value provided by the {@code defaultValueSupplier} otherwise,
     * i.e. when {@code string} is {@code null} or empty.
     */
    public static String stringOrDefault(
            String string,
            Supplier<String> defaultValueSupplier) {

        return string == null || string.isEmpty()
                ? defaultValueSupplier.get() : string;
    }

    /**
     * Return {@code object} as a String (as defined by the object's
     * {@link #toString()} method) when {@code object} is not {@code null}.
     * Return the empty String when {@code object} is {@code null}.
     *
     * <p>Note this is different from Objects#toString that returns a string
     * "null" for null values.</p>
     */
    public static String string(Object object) {
        return object == null ? "" : object.toString();
    }

    /**
     * Return {@code object} as a String (as defined by the object's
     * {@link #toString()} method) when {@code object} is not {@code null},
     * {@code null} otherwise.
     */
    public static String stringOrNull(Object object) {
        return object == null ? null : object.toString();
    }

    /**
     * Return the first character of the string.
     *
     * @param string [!s.isEmpty()]
     * @return the first character of the string
     */
    public static char firstChar(String string) {
        return string.charAt(0);
    }

    /**
     * Return the last character of the string.
     *
     * @param string [!s.isEmpty()]
     * @return the last character of the string
     */
    public static char lastChar(String string) {
        return string.charAt(string.length() - 1);
    }

    /**
     * Return a 'single-quoted' version of the toString representation of the
     * object or "null" when the object is null.
     *
     * <p>The text between the quotes is not escaped in any way.</p>
     *
     * @param object [nullable]
     * @return 'single-quoted' object.toString() or "null" if object is null.
     */
    public static String singleQuotedString_noEscapes(Object object) {
        return object != null
                ? String.format("'%s'", object) // NON-NLS
                : "null"; // NON-NLS
    }

    /**
     * Return the {@code text} with backslash-escaped characters (e.g. {@code
     * \"}) unescaped (e.g. {@code "}).
     *
     * <p>I.e. replace every occurrence of a character {@code c} that is
     * preceded by a {@code \} (backslash) by {@code c}.</p>
     *
     * <p>No characters are treated specially, e.g. "{@code \n}" will
     * <em>not</em> be converted to a newline character.</p>
     */
    public static String unescapeCharacters(String text) {
        return ESCAPED_CHAR.matcher(text).replaceAll("$1");
    }

}
