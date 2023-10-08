/*
 * MIT License
 *
 * Copyright (c) 2022 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.lineprocessing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;

/**
 * Process the lines of a text using a sequence of rules.
 * <p>
 * A rule consists of a condition and an action to be executed when the
 * condition becomes true.
 * <p>
 * A script is a sequence of rules.
 * <p>
 * A script is executed on each line of a given text. The script checks its
 * rules in their defined order. It then executes the action of the first rule
 * that condition becomes true. When no rule's condition became true the
 * {@code onDefault} action is executed.
 * <p>
 * Within the action code one may refer to context-related information (like
 * the line number of the line currently processed, or just the line text
 * itself).
 * <p>
 * When defining a Script one also defines a custom type holding the "state"
 * of the script execution. The action code has access to the state and may
 * modify it.
 */
public interface LineProcessing {
    /**
     * Returns a new {@link ScriptBuilder}, to create {@link Script}s that use
     * the state as provided by the {@code stateProvider} when processing lines.
     * <p>
     * A Script created with the returned {@code ScriptBuilder } calls
     * {@code stateProvider} once at the beginning of every
     * {@code Script.process(...)} execution and uses the provided
     * state instance throughout that execution.
     */
    static <S> ScriptBuilder<S> newScriptBuilder(Supplier<S> stateProvider) {
        return new ScriptBuilderImpl<>(stateProvider);
    }

    interface Context {
        /**
         * Returns the text of the line currently processed.
         */
        String line();

        /**
         * Returns the (1-based) number of the line currently processed.
         */
        int lineNumber();

        /**
         * Returns {@code true} when processing a pattern matching rule is
         * executed ({@link ScriptBuilder#onMatch(BiPredicate, BiConsumer)}),
         * {@code false} otherwise.
         * <p>
         * Only in a pattern matching rule a {@link Matcher} is defined and
         * {@link #m()} can be called to access it.
         */
        boolean isPatternMatchingRule();

        /**
         * Returns the {@link Matcher} of a pattern matching rule
         * ({@link ScriptBuilder#onMatch(BiPredicate, BiConsumer)}).
         * <p>
         * Typically, the matcher is used to access the content of individual
         * groups in the regular expression (e.g. {@code c.m().group(2)}).
         * <p>
         * Accessing the matcher in actions of non-pattern matching rules will
         * throw an Exception. You may use {@link #isPatternMatchingRule()}
         * to check if currently working with a pattern matching.
         */
        Matcher m();

        /**
         * Continue looking for another rule that matches the current line.
         * <p>
         * By default, when a matching rule is found for a given line the
         * rule's action is performed and no more rules are checked for this
         * line. However, when the action code of the matching rule calls
         * {@code more()} the lookup for a matching rule for that line
         * continues.
         * <p>
         * Calling this method will only ensure one more matching rule will be
         * executed, unless calling {@code more()} again in the later action.
         */
        void more();
    }

    interface Script {

        /**
         * Process the lines of the given {@code text}.
         */
        void process(String text);

        /**
         * Process the lines read from the given {@code reader}.
         */
        void process(BufferedReader reader);

        /**
         * Process the lines read from the given {@code inputStream}, assuming
         * the inputStream uses the given {@code charset} as encoding.
         */
        void process(InputStream inputStream, Charset charset);

        /**
         * Process the lines read from the given {@code inputStream}, assuming
         * the inputStream uses {@code UTF-8} as encoding.
         */
        void process(InputStream inputStream);
    }

    interface ScriptBuilder<S> {
        /**
         * Adds a rule to the script that executes the {@code action} when
         * the {@code condition} evaluates to {@code true}.
         *
         * @param condition receives both the current {@link Context} (including
         *                  the current line text) and the {@link Script}'s
         *                  state as parameters and can use this to decide on
         *                  its result. However, it must NOT modify the state
         *                  or call {@link Context#more()}.
         * @param action    receives both the current {@link Context}
         *                  (including the current line text) and the
         *                  {@link Script}'s state as parameters and can use is
         *                  in its code. It may also modify the state
         *                  or call {@link Context#more()}
         */
        void onMatch(
                BiPredicate<Context, S> condition,
                BiConsumer<LineProcessing.Context, S> action);

        /**
         * Adds a rule to the script that executes the {@code action} when
         * the current line matches the regular expression {@code regex}.
         *
         * @param regex  a regular expression as used in
         *               {@link java.util.regex.Pattern#compile(String)}
         * @param action receives both the current {@link Context}
         *               (including the current line text) and the
         *               {@link Script}'s state as parameters and can use is
         *               in its code. It may also modify the state
         *               or call {@link Context#more()}
         */
        void onMatch(String regex, BiConsumer<Context, S> action);

        /**
         * Adds the {@code action} to the script that is executed when no rule
         * matched the current line.
         * <p>
         * When multiple `default` actions are defined for a Script only
         * the last action is executed.
         *
         * @param action receives both the current {@link Context}
         *               (including the current line text) and the
         *               {@link Script}'s state as parameters and can use is
         *               in its code. It may also modify the state.
         */
        void onDefault(BiConsumer<Context, S> action);

        /**
         * Adds the {@code action} to the script that is executed when the end
         * of the text is reached.
         * <p>
         * When multiple `end of text` actions are defined for a Script only
         * the last action is executed.
         * <p>
         * The `end of text` is the last action to be executed in a Script.
         *
         * @param action receives both the current {@link Context}
         *               (including the current line text) and the
         *               {@link Script}'s state as parameters and can use is
         *               in its code.
         */
        void onEndOfText(BiConsumer<Context, S> action);

        /**
         * Returns a new {@link Script} instance as defined by the previous
         * calls to the builder.
         * <p>
         * The method {@code build()} may be called multiple times, always
         * returning a new {@link Script} instance. Modifications on the builder
         * between subsequent {@code build()} calls are additive, i.e. new
         * rules are `appended` to the old ones.
         */
        Script build();
    }
}
