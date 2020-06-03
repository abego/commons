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

package org.abego.commons.cli;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

import static org.abego.commons.lang.ArrayUtil.contains;

public final class CommandLineParserDefault implements CommandLineParser {
    private final @NonNull String[] args;
    private int i;

    @SuppressWarnings("ParameterHidesMemberVariable")
    private CommandLineParserDefault(@NonNull String... args) {
        this.args = args;
        i = 0;
    }

    static CommandLineParserDefault newCommandLineParserDefault(@NonNull String... args) {
        return new CommandLineParserDefault(args);
    }

    @Override
    public boolean hasNextItem() {
        return i < args.length;
    }

    @Nullable
    @Override
    public String nextOptionWithArgumentOrNull(String option) {
        return isOptionNext(option) ? consumeAndConsumeArgumentForOption(option) : null;
    }

    @Nullable
    @Override
    public String nextOptionOrNull(@NonNull String... possibleOptions) {
        return isOptionNext(possibleOptions) ? nextItem() : null;
    }

    @Override
    @Nullable
    public String nextArgumentOrNull() {
        return isArgumentNext() ? nextItem() : null;
    }

    @Override
    public String nextArgumentOrDefault(String defaultValue) {
        return isArgumentNext() ? nextItem() : defaultValue;
    }

    @Override
    public String nextArgumentOrFail(String failMessage) {
        if (!isArgumentNext()) {
            throw new IllegalArgumentException(failMessage);
        }
        return nextItem();
    }

    @Override
    public boolean isArgumentNext() {
        return hasNextItem() && !peekNextItem().startsWith("-");
    }

    @Override
    public boolean isOptionNext(@NonNull String... possibleOptions) {
        return hasNextItem() && contains(possibleOptions, peekNextItem());
    }

    @Override
    public String toString() {
        //noinspection HardCodedStringLiteral,StringConcatenation,MagicCharacter
        return "ArgumentsDefault{" +
                "args=" + Arrays.toString(args) +
                ", i=" + i +
                '}';
    }

    private String peekNextItem() {
        return args[i];
    }

    private String nextItem() {
        String nextItem = peekNextItem();
        consume();
        return nextItem;
    }

    private void consume() {
        i++;
    }

    private String consumeAndConsumeArgumentForOption(String option) {
        consume();
        if (i >= args.length) {
            //noinspection StringConcatenation
            throw new IllegalArgumentException("Missing argument for option " + option); //NON-NLS
        }
        return nextItem();
    }

}
