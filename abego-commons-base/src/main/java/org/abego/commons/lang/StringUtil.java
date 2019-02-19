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
import org.abego.commons.seq.Seq;

import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.abego.commons.lang.CharacterUtil.BACKSLASH_CHAR;
import static org.abego.commons.lang.CharacterUtil.BEL_CHAR;
import static org.abego.commons.lang.CharacterUtil.CARRIAGE_RETURN_CHAR;
import static org.abego.commons.lang.CharacterUtil.DOUBLE_QUOTE_CHAR;
import static org.abego.commons.lang.CharacterUtil.FIRST_PRINTABLE_ASCII_CHAR_VALUE;
import static org.abego.commons.lang.CharacterUtil.FORM_FEED_CHAR;
import static org.abego.commons.lang.CharacterUtil.LAST_PRINTABLE_ASCII_CHAR_VALUE;
import static org.abego.commons.lang.CharacterUtil.NEWLINE_CHAR;
import static org.abego.commons.lang.CharacterUtil.SINGLE_QUOTE_CHAR;
import static org.abego.commons.lang.CharacterUtil.TAB_CHAR;
import static org.abego.commons.lang.IterableUtil.textOf;

public class StringUtil {

    private static final Pattern ESCAPED_CHAR = Pattern.compile("\\\\(.)");

    StringUtil() {
        throw new MustNotInstantiateException();
    }

    public static boolean isNullOrEmpty(@Nullable String s) {
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
            @Nullable String string,
            String defaultValue) {

        return string == null || string.isEmpty() ? defaultValue : string;
    }

    /**
     * Return {@code string} when it is a non-empty {@link String}. Return
     * the value provided by the {@code defaultValueSupplier} otherwise,
     * i.e. when {@code string} is {@code null} or empty.
     */
    public static String stringOrDefault(
            @Nullable String string,
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
    public static String string(@Nullable Object object) {
        return object == null ? "" : object.toString();
    }

    /**
     * Return {@code object} as a String (as defined by the object's
     * {@link #toString()} method) when {@code object} is not {@code null},
     * {@code null} otherwise.
     */
    public static String stringOrNull(@Nullable Object object) {
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
    public static String singleQuotedStringWithoutEscapes(@Nullable Object object) {
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

    /**
     * Return a quoted version of <code>text</code>.
     *
     * @param text           the string to quote
     * @param nullResult     the String to be returned for null values.
     * @param useSingleQuote [default:false] when true use single quote ' instead of ".
     */
    public static String quoted(
            @Nullable String text,
            String nullResult,
            boolean useSingleQuote) {

        if (text == null) {
            return nullResult;
        }
        StringBuilder result = new StringBuilder();
        appendQuoteChar(result, useSingleQuote);
        appendEscapedString(result, text, useSingleQuote);
        appendQuoteChar(result, useSingleQuote);
        return result.toString();
    }

    /**
     * @param s the String to escape
     * @return the escaped string, as it would be written inside a Java String literal
     */
    public static String escaped(String s) {
        return appendEscapedString(new StringBuilder(), s, false).toString();
    }

    /**
     * @param c the character to escape
     * @return the escaped character as a String, as it would be written inside
     * a Java String literal
     */
    public static String escaped(char c) {
        return appendEscapedChar(new StringBuilder(), c, false).toString();
    }

    /**
     * Append the <code>text</code> to the stringBuilder, but in an escaped form,
     * as if the string would be written inside a String literal.
     *
     * @param stringBuilder  the StringBuilder to append to
     * @param text           the String to be escaped and appended
     * @param useSingleQuote when true the String is escaped as it would be
     *                       written inside a single quoted literal '...' (e.g. like in JavaScript).
     *                       When true a (double quoted) String literal is assumed ("...")
     * @return the escaped string, as it would be written inside a String literal
     */
    private static StringBuilder appendEscapedString(
            StringBuilder stringBuilder,
            String text,
            boolean useSingleQuote) {
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            appendEscapedChar(stringBuilder, c, useSingleQuote);
        }
        return stringBuilder;
    }

    private static StringBuilder appendEscapedChar(
            StringBuilder stringBuilder,
            char character,
            boolean useSingleQuote) {

        switch (character) {
            case BEL_CHAR:
                stringBuilder.append("\\b"); //NON-NLS
                break;

            case FORM_FEED_CHAR:
                stringBuilder.append("\\f"); //NON-NLS
                break;

            case NEWLINE_CHAR:
                stringBuilder.append("\\n"); //NON-NLS
                break;

            case CARRIAGE_RETURN_CHAR:
                stringBuilder.append("\\r"); //NON-NLS
                break;

            case TAB_CHAR:
                stringBuilder.append("\\t"); //NON-NLS
                break;

            case BACKSLASH_CHAR:
                stringBuilder.append("\\\\");
                break;

            case DOUBLE_QUOTE_CHAR:
                stringBuilder.append(useSingleQuote ? "\"" : "\\\"");
                break;

            case SINGLE_QUOTE_CHAR:
                stringBuilder.append(useSingleQuote ? "\\\'" : "'");
                break;

            default:
                if (character < FIRST_PRINTABLE_ASCII_CHAR_VALUE
                        || character > LAST_PRINTABLE_ASCII_CHAR_VALUE) {

                    String n = Integer.toHexString(character);
                    stringBuilder.append("\\u"); //NON-NLS
                    stringBuilder.append("0000".substring(n.length()));
                    stringBuilder.append(n);
                } else {
                    stringBuilder.append(character);
                }
                break;
        }
        return stringBuilder;
    }

    public static String quoted(@Nullable String text, String nullResult) {
        return quoted(text, nullResult, false);
    }

    /**
     * see {@link #quoted(String, String)}
     */
    public static String quoted(@Nullable String text) {
        return quoted(text, "null");
    }

    /**
     * see {@link #quoted(String, String, boolean)}
     */
    public static String singleQuoted(@Nullable String s, String nullResult) {
        return quoted(s, nullResult, true);
    }

    /**
     * see {@link #singleQuoted(String, String)}
     */
    public static String singleQuoted(@Nullable String s) {
        return singleQuoted(s, "null");
    }

    /**
     * Return a String composed of the {@link #toString()} representations of
     * the {@code elements}, separated with the given {@code delimiter}.
     *
     * <blockquote>For example,
     * <pre>{@code
     *     String message = StringUtil.join("-","Java", 3.14, null, true, '!');
     *     // message returned is: "Java-3.14-null-true-!"
     * }</pre></blockquote>
     * <p>
     * Note that if an element is {code null}, then {@code ""} is added.
     * <p>
     * (The method is a generalization of
     * {@link String#join(CharSequence, CharSequence...)}).
     * </p>
     */
    public static String join(CharSequence delimiter, Object... elements) {
        return textOf(Seq.newSeq(elements), delimiter);
    }

    /**
     * Return a String composed of the {@link #toString()} representations of
     * the {@code elements}, separated with the given {@code delimiter}.
     *
     * <blockquote>For example,
     * <pre>{@code
     *     String message = StringUtil.joinWithEmptyStringForNull("-","Java", 3.14, null, true, '!');
     *     // message returned is: "Java-3.14--true-!"
     * }</pre></blockquote>
     * <p>
     * Note that if an element is {code null}, then {@code ""} is added.
     * <p>
     * (The method is a generalization of
     * {@link String#join(CharSequence, CharSequence...)}), but adds {@code ""}
     * instead of {code "null"} for {code null} elements.
     * </p>
     */
    public static String joinWithEmptyStringForNull(CharSequence delimiter, Object... elements) {
        return textOf(Seq.newSeq(elements), delimiter, "", Object::toString, "");

    }

    private static void appendQuoteChar(
            StringBuilder stringBuilder, boolean useSingleQuote) {

        stringBuilder.append(useSingleQuote ? SINGLE_QUOTE_CHAR : DOUBLE_QUOTE_CHAR);
    }


}
