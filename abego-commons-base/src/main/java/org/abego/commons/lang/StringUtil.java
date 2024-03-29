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

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.range.IntRange;
import org.abego.commons.util.LocaleUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
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
import static org.abego.commons.util.function.PredicateUtil.isAnyPredicateTrue;

@SuppressWarnings("WeakerAccess")
public final class StringUtil {

    /**
     * The string to represent a <code>null</code> value, i.e. {@code null}.
     */
    public static final String NULL_STRING = "null"; //NON-NLS

    private static final Pattern ESCAPED_CHAR = Pattern.compile("\\\\(.)");
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final Pattern END_OF_LINE_PATTERN = Pattern
            .compile("(\\r\\n)|(\\n\\r?)");

    //region Factories / Conversions

    StringUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Returns the {@code @Nullable} {@code strings} as
     * a {@code @Nullable String} array.
     */
    public static @Nullable String[] arrayOfNullables(@Nullable String... strings) {
        int length = strings.length;
        @Nullable String[] result = new @Nullable String[length];
        System.arraycopy(strings, 0, result, 0, length);
        return result;
    }

    /*
     * Returns the {@code strings} as a {@code String} array.
     */
    public static @NonNull String[] array(@NonNull String... strings) {
        int length = strings.length;
        @NonNull String[] result = new @NonNull String[length];
        System.arraycopy(strings, 0, result, 0, length);
        return result;
    }

    /**
     * Returns the {@code @Nullable} {@code strings} as
     * a {@code @Nullable String} array.
     */
    public static @Nullable String[] arrayNullable(@Nullable String... strings) {
        return arrayOfNullables(strings);
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

    /**
     * Returns a {@code String} with the given {@code text} repeated {@code n}
     * times, or an empty string when {@code n <= 0}.
     */
    public static String repeat(String text, int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            result.append(text);
        }
        return result.toString();
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

    /**
     * Return the first character of the string.
     *
     * @param string [!s.isEmpty()]
     * @return the first character of the string
     */
    public static char firstChar(String string) {
        return string.charAt(0);
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

    /**
     * Returns the given {@code text} converted to an HTML text fragment
     * so rendering the result as HTML will display the original {@code text}.
     */
    @Nullable
    public static String toHtml(@Nullable String text) {
        if (text == null) {
            return null;
        }

        StringBuilder result = new StringBuilder(text.length());
        int len = text.length();
        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
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
     * Returns the given {@code text} `HTML-escaped`, i.e. any character with a
     * special meaning in HTML is properly escaped so rendering the result as
     * HTML will display the original {@code text}.
     * <p>
     * (Same as {@link #toHtml(String)}).
     */
    @Nullable
    public static String escapeForHtml(@Nullable String s) {
        return toHtml(s);
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
    public static String join(CharSequence delimiter, @Nullable Object... elements) {
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
    public static String joinWithEmptyStringForNull(CharSequence delimiter, @Nullable Object... elements) {
        return textOf(toIterable(elements), delimiter, "",
                o -> o != null ? o.toString() : "", "");

    }

    /**
     * Return the {@code string} if it has {@code maxLen} or fewer characters,
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
     * Return the {@code string} if it has {@code maxLen} or fewer characters,
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

    public static String[] lines(String text) {
        return END_OF_LINE_PATTERN.split(text, -1);
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

    /**
     * Returns the {@code string} with the {@code prefix} removed or the full
     * {@code string} when {@code string} does not start with {@code prefix} or
     * {@code prefix} is {@code null}.
     */
    public static String removePrefix(String string, @Nullable String prefix) {
        return prefix != null && string.startsWith(prefix)
                ? string.substring(prefix.length()) : string;
    }

    /**
     * Returns the string as a "unix String", i.e. using newline ('\n') as a
     * line separator.
     * <p>
     * The method assumes the string passed in either uses "\r\n", "\n\r" or
     * "\n" as line separators.
     */
    public static String unixString(String string) {
        return string.replace("\r", "");
    }

    /**
     * Sorts the lines of the string and returns the result as a "unix String",
     * i.e. using newline ('\n') as a line separator.
     * <p>
     * The returned string will not end with a line separator.
     * Empty lines are removed.
     */
    public static String sortedUnixLines(String string) {
        String[] lines = org.abego.commons.lang.StringUtil.lines(string);
        Arrays.sort(lines);
        return org.abego.commons.lang.StringUtil.join("\n", (Object[]) lines)
                .trim();
    }

    /**
     * Returns the {@code string} with all slashes ("/") replaced by dots (".").
     */
    public static String slashesToDots(String string) {
        return string.replaceAll("/", ".");
    }

    /**
     * Returns the prefix of the {@code string} before the {@code delimiter}
     * or the full {@code string} when {@code string} does not contain the
     * {@code delimiter}.
     */
    public static String prefixBefore(String string, String delimiter) {
        int i = string.indexOf(delimiter);
        return i >= 0 ? string.substring(0, i) : string;
    }

    // endregion

    //region Queries

    /**
     * Return the last character of the string.
     *
     * @param string [!s.isEmpty()]
     * @return the last character of the string
     */
    public static char lastChar(String string) {
        return string.charAt(string.length() - 1);
    }

    public static int lineCount(String text) {
        int i = 1;
        Matcher m = END_OF_LINE_PATTERN.matcher(text);
        while (m.find()) {
            i++;
        }
        return i;
    }

    public static String firstLine(String text) {
        return lines(text)[0];
    }

    //region Checks
    public static boolean isNullOrEmpty(@Nullable String s) {
        return s == null || s.isEmpty();
    }

    //endregion

    /**
     * Return true if {@code s} has text, i.e. is not {@code null}
     * and is not the empty string.
     */
    public static boolean hasText(@Nullable String s) {
        return !isNullOrEmpty(s);
    }

    /**
     * Returns {@code true} if the {@code text} contains any of the
     * {@code expectedSubStrings}, otherwise {@code false}.
     */
    public static boolean containsAnyOf(String text, String... expectedSubStrings) {
        for (String s : expectedSubStrings) {
            if (text.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Same as {@link String#compareToIgnoreCase(String)}, but if the strings
     * are equal when ignoring the case they are compared again case sensitively
     * to ensure a stable order between the two strings.
     *
     * <p>This method does not take locale into account, and may result in an
     * unsatisfactory ordering for certain locales.</p>
     */
    public static int compareToIgnoreCaseStable(String s1, String s2) {
        return ObjectUtil.compareAsTexts(s1, s2);
    }

    /**
     * Tests if string {@code s} ends with {@code suffix}, ignoring case
     * considerations.
     * <p>
     * Similar to {@link String#endsWith(String)} but differences just in the
     * case of corresponding characters are ignored.
     *
     * <p>This method does not take locale into account, and may result in an
     * unsatisfactory ordering for certain locales.</p>
     *
     * @param s      the String to check for its suffix
     * @param suffix the suffix
     * @return true if the character sequence represented by {@code suffix} is a
     * suffix of the character sequence represented by {@code s}, ignoring case
     * considerations; false otherwise.
     */
    public static boolean endsWithIgnoreCase(String s, String suffix) {
        int suffixStart = s.length() - suffix.length();
        if (suffixStart < 0) {
            return false;
        }

        //noinspection CallToSuspiciousStringMethod
        return s.substring(suffixStart).equalsIgnoreCase(suffix);
    }

    private interface StringBuilderAppender {
        void append(StringBuilder builder, char c);
    }

    /**
     * Returns a {@link Predicate} on {@link String} objects that tests if
     * a given String is one of the given {@code options}, with options
     * ending with {@code '*'} matching any String that starts with the text
     * left to the {@code '*'} (i.e. wildcards at the end are supported).
     */
    public static Predicate<String> newIncludesStringPredicate(String[] options) {
        return new IncludesStringPredicate(options);
    }

    /**
     * As {@link #newIncludesStringPredicate(String[])}, with every line
     * of {@code linesWithOptions} defining one option.
     */
    public static Predicate<String> newIncludesStringPredicate(String linesWithOptions) {
        return new IncludesStringPredicate(StringUtil.lines(linesWithOptions));
    }

    private static final class IncludesStringPredicate implements Predicate<String> {
        private final List<Predicate<String>> allChecks = new ArrayList<>();

        private IncludesStringPredicate(String[] options) {
            for (String line : options) {
                int length = line.length();
                if (length > 0) {
                    if (line.endsWith("*")) {
                        allChecks.add(t -> t.startsWith(line.substring(0, length - 1)));
                    } else {
                        //noinspection CallToSuspiciousStringMethod
                        allChecks.add(t -> t.equals(line));
                    }
                }
            }
        }

        @Override
        public boolean test(String valueToTest) {
            return isAnyPredicateTrue(allChecks, valueToTest);
        }
    }

    /**
     * Returns a {@link Consumer} that receives the text passed to the given
     * {@code consumer} indented by one tab ('\t').
     */
    public static Consumer<String> indent(Consumer<String> consumer) {
        return s -> consumer.accept("\t" + s);
    }

    // endregion


}
