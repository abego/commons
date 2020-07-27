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

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.range.IntRange;
import org.abego.commons.seq.AbstractSeq;
import org.abego.commons.seq.Seq;
import org.abego.commons.util.LocaleUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
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
import static org.abego.commons.lang.IntUtil.limit;
import static org.abego.commons.lang.IterableUtil.textOf;
import static org.abego.commons.lang.IterableUtil.toIterable;
import static org.abego.commons.seq.SeqUtil.newSeq;

@SuppressWarnings("WeakerAccess")
public final class StringUtil {

    /**
     * The string to represent represent a <code>null</code> value, i.e.
     * <code>"null"</code>.
     */
    public static final String NULL_STRING = "null"; //NON-NLS

    private static final Pattern ESCAPED_CHAR = Pattern.compile("\\\\(.)");
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final Pattern END_OF_LINE_PATTERN = Pattern
            .compile("\\r?\\n");

    StringUtil() {
        throw new MustNotInstantiateException();
    }

    public static @Nullable String[] arrayOfNullables(@Nullable String... items) {
        int length = items.length;
        @Nullable String[] result = new @Nullable String[length];
        System.arraycopy(items, 0, result, 0, length);
        return result;
    }

    public static @NonNull String[] array(@NonNull String... items) {
        int length = items.length;
        @NonNull String[] result = new @NonNull String[length];
        System.arraycopy(items, 0, result, 0, length);
        return result;
    }

    public static @Nullable String[] arrayNullable(@Nullable String... items) {
        int length = items.length;
        @Nullable String[] result = new @Nullable String[length];
        System.arraycopy(items, 0, result, 0, length);
        return result;
    }

    public static boolean isNullOrEmpty(@Nullable String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Return true if {@code s} has text, i.e. is not {@code null}
     * and is not the empty string.
     */
    public static boolean hasText(@Nullable String s) {
        return !isNullOrEmpty(s);
    }

    /**
     * Returns {@code string} when it is a non-empty String; returns
     * {@code defaultValue} when {@code string} is {@code null} or empty.
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
    @Nullable
    public static String stringOrNull(@Nullable Object object) {
        return object == null ? null : object.toString();
    }

    public static String repeat(String text, int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            result.append(text);
        }
        return result.toString();
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
                : NULL_STRING;
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

    private static String quotedHelper(
            @Nullable String text,
            String nullResult,
            char quoteChar,
            String singleQuoteText,
            String doubleQuoteText,
            StringBuilderAppender nonAsciiCharHandler) {

        if (text == null) {
            return nullResult;
        }
        StringBuilder result = new StringBuilder();
        result.append(quoteChar);
        appendEscapedString(result, text, singleQuoteText, doubleQuoteText, nonAsciiCharHandler);
        result.append(quoteChar);
        return result.toString();
    }

    /**
     * @param s the String to escape
     * @return the escaped string, as it would be written inside a Java String literal
     */
    public static String escaped(@Nullable String s) {
        return appendEscapedString(new StringBuilder(), s, "'", "\\\"",
                StringUtil::appendUnicodeEscaped).toString();
    }

    /**
     * @param s the String to escape
     * @return the escaped string, as it would be written inside a Java String literal
     */
    public static @Nullable String escapedOrNull(@Nullable String s) {
        return s == null ? null : escaped(s);
    }

    /**
     * @param c the character to escape
     * @return the escaped character as a String, as it would be written inside
     * a Java String literal
     */
    public static String escaped(char c) {
        return escaped(String.valueOf(c));
    }

    public static String substringSafe(String s, int beginIndex, int endIndex) {
        int len = s.length();
        int b = limit(beginIndex, 0, len);
        int e = limit(endIndex, 0, len);

        return b <= e ? s.substring(b, e) : "";
    }

    public static String substringSafe(String s, int beginIndex) {
        return substringSafe(s, beginIndex, s.length());
    }

    /**
     * Append the <code>text</code> to the stringBuilder, but in an escaped form,
     * as if the string would be written inside a String literal.
     *
     * @param stringBuilder        the StringBuilder to append to
     * @param text                 the String to be escaped and appended
     * @param singleQuoteText      the text to use to "escape" a single quote char
     * @param nonASCIICharAppender used to append the text for non-ASCII chars
     * @return the escaped string, as it would be written inside a String literal
     */
    private static StringBuilder appendEscapedString(
            StringBuilder stringBuilder,
            @Nullable String text,
            String singleQuoteText,
            String doubleQuoteText,
            StringBuilderAppender nonASCIICharAppender) {
        if (text != null) {
            int length = text.length();
            for (int i = 0; i < length; i++) {
                char c = text.charAt(i);
                appendEscapedChar(stringBuilder, c, singleQuoteText, doubleQuoteText, nonASCIICharAppender);
            }
        }
        return stringBuilder;
    }

    private static void appendEscapedChar(
            StringBuilder stringBuilder,
            char character,
            String singleQuoteText,
            String doubleQuoteText,
            StringBuilderAppender nonASCIICharAppender) {

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
                stringBuilder.append(doubleQuoteText);
                break;

            case SINGLE_QUOTE_CHAR:
                stringBuilder.append(singleQuoteText);
                break;

            default:
                if (character < FIRST_PRINTABLE_ASCII_CHAR_VALUE) {
                    appendUnicodeEscaped(stringBuilder, character);
                } else if (character > LAST_PRINTABLE_ASCII_CHAR_VALUE) {
                    nonASCIICharAppender.append(stringBuilder, character);
                } else {
                    stringBuilder.append(character);
                }
                break;
        }
    }

    private static void appendUnicodeEscaped(StringBuilder stringBuilder, char character) {
        String n = Integer.toHexString(character);
        stringBuilder.append("\\u"); //NON-NLS
        stringBuilder.append("0000".substring(n.length()));
        stringBuilder.append(n);
    }

    private static void append(StringBuilder stringBuilder, char character) {
        stringBuilder.append(character); //NON-NLS
    }

    /**
     * Returns the prefix of the {@code string} of the given {@code size} when
     * {@code size >= 0}; or the {@code string} without its last {@code -size}
     * characters when {@code size < 0}.
     *
     * <p>Returns {@code string} when {@code size >= string.length}.
     * Returns the empty string when {@code size <= -string.length}</p>
     */
    public static String prefix(String string, int size) {
        return string.substring(0,
                limit(
                        size >= 0 ? size : string.length() + size,
                        0, string.length()));
    }

    @Nullable
    public static String toHtml(@Nullable String s) {
        if (s == null) {
            return null;
        }

        StringBuilder result = new StringBuilder(s.length());
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            switch (c) {
                //noinspection MagicCharacter
                case '&':
                    result.append("&amp;"); //NON-NLS
                    break;
                //noinspection MagicCharacter
                case '<':
                    result.append("&lt;"); //NON-NLS
                    break;
                //noinspection MagicCharacter
                case '>':
                    result.append("&gt;"); //NON-NLS
                    break;
                //noinspection MagicCharacter
                case '"':
                    result.append("&quot;"); //NON-NLS
                    break;
                //noinspection MagicCharacter
                case '\'':
                    // The single quote has no name (like "&quote;") so use the
                    // generic "&#nnn;" construct
                    result.append("&#039;");
                    break;
                default:
                    result.append(c);
                    break;
            }
        }
        return result.toString();
    }

    /**
     * Return a (double) quoted version of <code>text</code>.
     *
     * <p>Non-ASCII characters are "backslash-u..." escaped, resulting in a
     * string only contains ASCII characters.</p>
     *
     * @param text       the string to quote
     * @param nullResult the String to be returned for null values.
     */
    public static String quoted(@Nullable String text, String nullResult) {
        return quotedHelper(text, nullResult, DOUBLE_QUOTE_CHAR, "'", "\\\"", StringUtil::appendUnicodeEscaped);
    }

    /**
     * Return a (double) quoted version of <code>text</code>, with non-ASCII
     * characters not escaped.
     *
     * @param text       the string to quote
     * @param nullResult the String to be returned for null values.
     */
    public static String quoted2(@Nullable String text, String nullResult) {
        return quotedHelper(text, nullResult, DOUBLE_QUOTE_CHAR, "'", "\\\"", StringUtil::append);
    }

    public static String quoted2(@Nullable String text) {
        return quoted2(text, NULL_STRING);
    }

    /**
     * see {@link #quoted(String, String)}
     */
    public static String quoted(@Nullable String text) {
        return quoted(text, NULL_STRING);
    }

    /**
     * Return a single quoted version of <code>text</code>.
     *
     * @param text       the string to quote
     * @param nullResult the String to be returned for null values.
     */
    public static String singleQuoted(@Nullable String text, String nullResult) {
        return quotedHelper(text, nullResult, SINGLE_QUOTE_CHAR, "\\'", "\"", StringUtil::appendUnicodeEscaped);
    }

    /**
     * see {@link #singleQuoted(String, String)}
     */
    public static String singleQuoted(@Nullable String s) {
        return singleQuoted(s, NULL_STRING);
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
        return textOf(toIterable(elements), delimiter);
    }

    public static String camelCased(String string) {
        return LocaleUtil.camelCased(string, LocaleUtil.DEFAULT_LOCALE);
    }

    public static String dashCased(String string) {
        return LocaleUtil.dashCased(string, LocaleUtil.DEFAULT_LOCALE);
    }

    public static String snakeCased(String string) {
        return LocaleUtil.snakeCased(string, LocaleUtil.DEFAULT_LOCALE);
    }

    public static String snakeUpperCased(String string) {
        return LocaleUtil.snakeUpperCased(string, LocaleUtil.DEFAULT_LOCALE);
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
        return textOf(toIterable(elements), delimiter, "", Object::toString, "");

    }

    /**
     * Return the {@code string} if it has {@code maxLen} or less characters,
     * otherwise a string of length {@code maxLen} starting with the left of
     * {@code string} and ending with {@code truncatedStringSuffix}.
     */
    public static String limitString(String string, int maxLen, String truncatedStringSuffix) {

        if (truncatedStringSuffix.length() > maxLen) {
            throw new IllegalArgumentException(String.format(
                    "truncatedStringSuffix.length must be <= maxLen, got %d > %d", //NON-NLS
                    truncatedStringSuffix.length(), maxLen));
        }

        //noinspection StringConcatenation
        return string.length() <= maxLen
                ? string
                : prefix(string, maxLen - truncatedStringSuffix.length())
                + truncatedStringSuffix;
    }

    /**
     * Return the {@code string} if it has {@code maxLen} or less characters,
     * otherwise a string of length {@code maxLen} starting with the left of
     * {@code string} and ending with "...".
     */
    public static String limitString(String string, int maxLen) {
        return limitString(string, maxLen, "...");
    }

    public static @NonNull String[] splitWhitespaceSeparatedString(String s) {
        return s.split("\\s+", -1); //NON-NLS
    }

    public static String[] toArray(Collection<@NonNull String> collection) {
        return collection.isEmpty()
                ? EMPTY_STRING_ARRAY
                : collection.toArray(new String[0]);
    }

    /**
     * Return the {@code text} with all newlines ('\n') replaced by the system's
     * line separator.
     */
    public static String withLineSeparatorsForNewlines(final String text) {
        return text.replaceAll("\n", SystemUtil.getLineSeparator());
    }

    /**
     * Return the {@code string} in upper case, or {@code "null"} when {@code string} is {@code null}.
     *
     * <p>Use {@code Locale#ENGLISH} as locale.</p>
     */
    public static String toUpperCaseSafe(@Nullable String string) {
        return string != null ? string.toUpperCase(Locale.ENGLISH) : "null";
    }

    /**
     * Return the {@code string} in lower case, or {@code "null"} when {@code string} is {@code null}.
     *
     * <p>Use {@code Locale#ENGLISH} as locale.</p>
     */
    public static String toLowerCaseSafe(@Nullable String string) {
        return string != null ? string.toLowerCase(Locale.ENGLISH) : "null";
    }

    public static Seq<Character> characters(String text) {
        return new AbstractSeq<Character>() {
            @Override
            public Iterator<Character> iterator() {
                return new Iterator<Character>() {
                    private int i = 0;

                    @Override
                    public boolean hasNext() {
                        return i < text.length();
                    }

                    @Override
                    public Character next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return text.charAt(i++);
                    }
                };
            }

            @Override
            public int size() {
                return text.length();
            }

            @Override
            public Character item(int i) {
                return text.charAt(i);
            }
        };
    }

    public static Seq<String> lines(String text) {
        return newSeq(END_OF_LINE_PATTERN.split(text, -1));
    }

    public static String firstLine(String text) {
        return lines(text).first();
    }

    public static String replaceRange(
            String text, int startIndex, int endIndex, String newRangeText) {
        return join("",
                text.substring(0, startIndex),
                newRangeText,
                text.substring(endIndex));
    }

    public static String replaceRange(
            String text, IntRange range, String newRangeText) {
        return replaceRange(
                text, range.getStart(), range.getEnd(), newRangeText);
    }

    private interface StringBuilderAppender {
        void append(StringBuilder builder, char c);
    }

}
