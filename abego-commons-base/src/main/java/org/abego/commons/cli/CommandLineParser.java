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

/**
 * <b>Definitions</b>
 * <ul>
 * <li>Option: item in the command line starting with "-", like "-i" or "--help"</li>
 * <li>Argument: item in the command line that is not an option</li>
 * </ul>
 */
@SuppressWarnings("WeakerAccess")
public interface CommandLineParser {

    /**
     * Return true when there are still items in the command line, false otherwise.
     */
    boolean hasNextItem();

    /**
     * Return true when the next item is one of the {@code possibleOptions}, false otherwise.
     *
     * @param possibleOptions must not be empty.
     */
    boolean isOptionNext(@NonNull String... possibleOptions);

    /**
     * Return true when the next item is an argument.
     *
     * <p>I.e. there are still items in the command line and the next item is not an option.
     */
    boolean isArgumentNext();


    /**
     * When the next item is one of the {@code possibleOptions},
     * consume and return the option, otherwise return null.
     */
    @Nullable
    String nextOptionOrNull(@NonNull String... possibleOptions);

    /**
     * When the next item is the given {@code option} consume the option, and consume and
     * return the following argument, otherwise return null.
     *
     * <p>Throw an {@link IllegalArgumentException} when next item is the given {@code option}
     * but not argument follows.</p>
     */
    @Nullable
    String nextOptionWithArgumentOrNull(String option);

    /**
     * When the next item is an argument, consume and return the argument,
     * otherwise return {@code null}.
     */
    @Nullable
    String nextArgumentOrNull();

    /**
     * When the next item is an argument, consume and return the argument,
     * otherwise return the {@code defaultValue}.
     */
    String nextArgumentOrDefault(String defaultValue);

    /**
     * When the next item is an argument, consume and return the argument,
     * otherwise throw an {@link IllegalArgumentException} with the given {@code failMessage}.
     */
    String nextArgumentOrFail(String failMessage);
}
