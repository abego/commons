/*
 * MIT License
 *
 * Copyright (c) 2021 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.polling;

import org.abego.commons.timeout.TimeoutSupplier;
import org.abego.commons.timeout.TimeoutUncheckedException;
import org.abego.commons.timeout.Timeoutable;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Operations related to Polling (the process of waiting for a certain state and
 * periodically checking if the state is reached).
 */
public interface PollingService extends TimeoutSupplier {

    /**
     * Return the first value polled from {@code functionToPoll} that makes
     * {@code isResult(value)} evaluate to {@code true}.
     *
     * <p>Throw a {@link TimeoutUncheckedException} when {@code isResult} did not
     * return {@code true} within {@code timeout}.</p>
     *
     * <p><em>Polling</em> refers to the process of waiting for a certain state and
     * periodically checking if the state is reached.</p>
     *
     * @param <T>            the type of the value to be polled
     * @param functionToPoll the function used to poll for the value
     * @param isResult       function that returns {@code true} when the
     *                       passed value is a possible result value,
     *                       {@code false} otherwise.
     * @param timeout        the duration the method will poll the value
     *                       before throwing a {@link TimeoutUncheckedException}.
     *                       [Default: {@code timeout()}]
     * @return the first value polled from {@code functionToPoll} that makes
     * {@code isResult(value)} evaluate to {@code true}
     */
    @Timeoutable
    <T> T poll(Supplier<T> functionToPoll, Predicate<T> isResult, Duration timeout);

    /**
     * Return the first value polled from {@code functionToPoll} that makes
     * {@code isResult(value)} evaluate to {@code true}.
     *
     * <p>Throw a {@link TimeoutUncheckedException} when {@code isResult} did not
     * return {@code true} within {@code timeout()}.</p>
     *
     * <p><em>Polling</em> refers to the process of waiting for a certain state and
     * periodically checking if the state is reached.</p>
     *
     * @param <T>            the type of the value to be polled
     * @param functionToPoll the function used to poll for the value
     * @param isResult       function that returns {@code true} when the
     *                       passed value is a possible result value,
     *                       {@code false} otherwise.
     * @return the first value polled from {@code functionToPoll} that makes
     * {@code isResult(value)} evaluate to {@code true}
     */
    @Timeoutable
    default <T> T poll(Supplier<T> functionToPoll, Predicate<T> isResult) {

        return poll(functionToPoll, isResult, timeout());
    }

    /**
     * Returns the first value polled from {@code functionToPoll} that makes
     * {@code isResult(value)} evaluate to {@code true}, or the last
     * polled value after the timeout occurred.
     *
     * <p><em>Polling</em> refers to the process of waiting for a certain state and
     * periodically checking if the state is reached.</p>
     *
     * @param <T>            the type of the value to be polled
     * @param functionToPoll the function used to poll for the value
     * @param isResult       function that returns {@code true} when the
     *                       passed value is a possible result value,
     *                       {@code false} otherwise.
     * @param timeout        the duration the method will poll the value
     *                       before stop polling and returning the last polled
     *                       value.
     * @return the first value polled from {@code functionToPoll} that makes
     * {@code isResult(value)} evaluate to {@code true}, or the last
     * polled value after the timeout occurred.
     */
    @Timeoutable
    <T> T pollNoFail(Supplier<T> functionToPoll, Predicate<T> isResult, Duration timeout);

    /**
     * Return the first value polled from {@code functionToPoll} that makes
     * {@code isResult(value)} evaluate to {@code true}, or the last
     * polled value after the timeout (defined by {@code timeout()} occurred.
     * <p>
     * <em>Polling</em> refers to the process of waiting for a certain state and
     * periodically checking if the state is reached.</p>
     *
     * @param <T>            the type of the value to be polled
     * @param functionToPoll the function used to poll for the value
     * @param isResult       function that returns {@code true} when the
     *                       passed value is a possible result value,
     *                       {@code false} otherwise.
     * @return the first value polled from {@code functionToPoll} that makes
     * {@code isResult(value)} evaluate to {@code true}, or the last
     * polled value after the timeout occurred.
     */
    @Timeoutable
    default <T> T pollNoFail(Supplier<T> functionToPoll, Predicate<T> isResult) {
        return pollNoFail(functionToPoll, isResult, timeout());
    }
}
