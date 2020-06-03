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

package org.abego.commons.util;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LocaleUtil {

    public static final Locale DEFAULT_LOCALE = Locale.US;

    private static final Pattern LOWER_UPPER_CASE_BOUNDARY_OR_NON_DIGIT_OR_LETTER_SEQUENCE =
            Pattern.compile("(?<=[\\p{javaLowerCase}0-9])(?=[\\p{javaUpperCase}])|([^0-9\\p{javaLowerCase}\\p{javaUpperCase}]+)");
    private static final Pattern DIGIT_OR_LETTER_SEQUENCE =
            Pattern.compile("[0-9\\p{javaLowerCase}\\p{javaUpperCase}]+");

    LocaleUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Return the {@code locale} when it is not {@code null}, otherwise return the DEFAULT_LOCALE.
     */
    public static Locale localeOrDefault(@Nullable Locale locale) {
        return locale != null ? locale : DEFAULT_LOCALE;
    }

    /**
     * @param locale (Default: DEFAULT_LOCALE)
     */
    public static String camelCased(String s, Locale locale) {
        StringBuilder result = new StringBuilder();
        Matcher m = DIGIT_OR_LETTER_SEQUENCE.matcher(s);
        while (m.find()) {
            String word = m.group();
            String firstChar = word.substring(0, 1);
            result.append(result.length() > 0
                    ? firstChar.toUpperCase(locale)
                    : firstChar.toLowerCase(locale));
            result.append(word.substring(1));
        }
        return result.toString();
    }

    /**
     * Return the <code>string</code> converted to dash case, i.e. insert a
     * '-' between a lowercase character, uppercase character
     * subsequence and make the whole resulting string lowercase.
     *
     * <p>Handle digits as lowercase. Non-letter/digit sequences also become
     * a '-'.</p>
     *
     * <b>Examples:</b>
     * <ul>
     *    <li> "abcDef" -&gt; "abc-def"</li>
     *    <li>"param0Disabled" -&gt; "param0-disabled"</li>
     *    <li>"abc \ndef -_ GhI" -&gt; "abc-def-gh-i"</li>
     * </ul>
     *
     * @param locale (default DEFAULT_LOCALE)
     */
    public static String dashCased(String string, Locale locale) {
        return casedStringHelper(string, "-").toLowerCase(locale);
    }

    public static String snakeCased(String string, Locale locale) {
        return casedStringHelper(string, "_").toLowerCase(locale);
    }

    public static String snakeUpperCased(String string, Locale locale) {
        return casedStringHelper(string, "_").toUpperCase(locale);
    }

    private static String casedStringHelper(String string, String separator) {
        return LOWER_UPPER_CASE_BOUNDARY_OR_NON_DIGIT_OR_LETTER_SEQUENCE.
                matcher(string).replaceAll(separator);
    }


}
