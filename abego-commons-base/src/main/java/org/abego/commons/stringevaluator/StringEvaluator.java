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

package org.abego.commons.stringevaluator;

import org.eclipse.jdt.annotation.Nullable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.abego.commons.io.FileUtil.runIOCode;
import static org.abego.commons.lang.StringUtil.unescapeCharacters;

@SuppressWarnings("WeakerAccess")
public final class StringEvaluator {
    public static final char ESCAPE_CHARACTER = '\\';
    public static final char LEFT_TERM_DELIMITER_DEFAULT = '{';
    public static final char RIGHT_TERM_DELIMITER_DEFAULT = '}';

    private final char leftTermDelimiter;
    private final char rightTermDelimiter;
    private final Pattern textOrTermSegmentPattern;
    private final Map<String, String> termValues = new HashMap<>();
    private final List<TermEvaluator> termEvaluators = new ArrayList<>();

    private StringEvaluator(char leftTermDelimiter, char rightTermDelimiter) {
        if (leftTermDelimiter == ESCAPE_CHARACTER ||
                rightTermDelimiter == ESCAPE_CHARACTER) {
            throw new InvalidDelimiterException();
        }

        this.leftTermDelimiter = leftTermDelimiter;
        this.rightTermDelimiter = rightTermDelimiter;
        textOrTermSegmentPattern = textOrTermSegmentPattern(
                leftTermDelimiter, rightTermDelimiter);
    }

    private StringEvaluator() {
        this(LEFT_TERM_DELIMITER_DEFAULT, RIGHT_TERM_DELIMITER_DEFAULT);
    }

    public static StringEvaluator stringEvaluator(
            char leftTermDelimiter, char rightTermDelimiter) {
        return new StringEvaluator(leftTermDelimiter, rightTermDelimiter);
    }

    public static StringEvaluator stringEvaluator() {
        return new StringEvaluator();
    }

    /**
     * Return a Pattern to match "<em>textSegment</em><code>{</code><em
     * >termSegment</em><code>}</code>",
     * with
     * <ul>
     * <li>group 1 holding <em>textSegment</em>, and</li>
     * <li>group 2 holding <em>termSegment</em></li>
     * </ul>
     *
     * <p>Both groups may be empty/{@code null}.
     * </p>
     * <p>The characters for the term delimiters (<code>{</code> and
     * <code>}</code> in the example above) can be configured.
     * </p>
     * <p>Use "\" to escape characters in <em>textSegment</em>, especially
     * '<code>{</code>'.
     * </p>
     *
     * @param leftTermDelimiter  the character to be used as the left delimiter
     *                           for terms (<code>{</code> in the example above)
     * @param rightTermDelimiter the character to be used as the right delimiter
     *                           for terms (<code>}</code> in the example above)
     */
    private static Pattern textOrTermSegmentPattern(
            char leftTermDelimiter, char rightTermDelimiter) {

        String regex = MessageFormat.format(
                "((?:[^\\{0}\\{2}]|(?:\\{2}.))*)(?:\\{0}((?:[^\\{1}]|(?:\\{2}.))+)\\{1})?",
                leftTermDelimiter,
                rightTermDelimiter,
                ESCAPE_CHARACTER);
        return Pattern.compile(regex);
    }

    public String valueOf(String string) {
        return runIOCode(() -> {
            StringWriter writer = new StringWriter();
            evaluateString(string, writer);
            return writer.toString();
        });
    }

    public void setTermValue(String term, String termValue) {
        termValues.put(term, termValue);
    }

    public void addTermEvaluator(TermEvaluator termEvaluator) {
        termEvaluators.add(0, termEvaluator);
    }

    public char leftTermDelimiter() {
        return leftTermDelimiter;
    }

    public char rightTermDelimiter() {
        return rightTermDelimiter;
    }

    @Nullable
    private String termValue(String term) {
        for (TermEvaluator e : termEvaluators) {
            @Nullable String s = e.valueOf(term);
            if (s != null) {
                return s;
            }
        }
        return termValues.get(term);
    }

    private void evaluateTerm(String term,
                              Writer writer) throws IOException {
        @Nullable String value = termValue(term);
        if (value == null) {
            writer.write(leftTermDelimiter());
            writer.write(term);
            writer.write(rightTermDelimiter());

        } else {
            evaluateString(value, writer);
        }
    }

    private void evaluateString(String string, Writer writer)
            throws IOException {
        Matcher m = textOrTermSegmentPattern.matcher(string);
        while (m.find()) {
            String text = m.group(1);
            String term = m.group(2);

            if (text != null && !text.isEmpty()) {
                writer.write(unescapeCharacters(text));
            }

            if (term != null) {
                evaluateTerm(term, writer);
            }
        }
    }

    @FunctionalInterface
    public interface TermEvaluator {

        /**
         * Return the value of the term, or {@code null} when the term cannot be evaluated (by this TermEvaluator)
         */
        @Nullable
        String valueOf(String term);
    }

    @SuppressWarnings("serial")
    public static class StringEvaluatorException extends RuntimeException {

        private StringEvaluatorException(String message) {
            super(message);
        }
    }

    @SuppressWarnings("serial")
    public static class InvalidDelimiterException
            extends StringEvaluatorException {

        private InvalidDelimiterException() {
            super(String.format("'%s' must not be used as a term delimiter", //NON-NLS
                    ESCAPE_CHARACTER));
        }
    }

}
