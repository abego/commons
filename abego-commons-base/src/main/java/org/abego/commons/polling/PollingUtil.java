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

package org.abego.commons.polling;


import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.timeout.TimeoutUncheckedException;
import org.abego.commons.timeout.Timeoutable;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static java.lang.System.currentTimeMillis;

public final class PollingUtil {

    private static final int MAX_SLEEP_BETWEEN_POLLS_MILLIS = 100;

    PollingUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Return the first value polled from <code>functionToPoll</code> that makes
     * <code>isResult(value)</code> evaluate to <code>true</code>.
     *
     * <p>Throw a TimeoutUncheckedException when <code>condition</code> did not
     * return <code>true</code> within <code>timeout</code>.</p>
     *
     * @param functionToPoll the function used to poll for the value
     * @param isResult       function that returns <code>true</code> when the
     *                       passed value is a possible result value,
     *                       <code>false</code> otherwise.
     * @param timeout        the duration the method will poll the value
     *                       before throwing a {@link TimeoutUncheckedException}.
     */
    @Timeoutable
    public static <T> T poll(
            Supplier<T> functionToPoll,
            Predicate<T> isResult,
            Duration timeout) {
        return poll(functionToPoll, isResult, timeout, v -> {
            throw new TimeoutUncheckedException();
        });
    }

    /**
     * Return the first value polled from <code>functionToPoll</code> that makes
     * <code>isResult(value)</code> evaluate to <code>true</code> or the last
     * polled value in case of a timeout.
     *
     * @param functionToPoll the function used to poll for the value
     * @param isResult       function that returns <code>true</code> when the
     *                       passed value is a possible result value,
     *                       <code>false</code> otherwise.
     * @param timeout        the duration the method will poll the value
     *                       before returning the last polled value.
     */
    @Timeoutable
    public static <T> T pollNoFail(
            Supplier<T> functionToPoll,
            Predicate<T> isResult,
            Duration timeout) {
        return poll(functionToPoll, isResult, timeout, v -> v);
    }

    /**
     * Return the first value polled from <code>functionToPoll</code> that makes
     * <code>isResult(value)</code> evaluate to <code>true</code>.
     *
     * <p>When <code>condition</code> did not return <code>true</code> within
     * <code>timeout</code> call {@code onTimeout} with the last polled value
     * and return the result of the function call.</p>
     *
     * @param functionToPoll the function used to poll for the value
     * @param isResult       function that returns <code>true</code> when the
     *                       passed value is a possible result value,
     *                       <code>false</code> otherwise.
     * @param timeout        the duration the method will poll the value
     *                       before calling {@code onTimeout}
     * @param onTimeout      function to call when "on timeout".
     */
    @Timeoutable
    public static <T> T poll(
            Supplier<T> functionToPoll,
            Predicate<T> isResult,
            Duration timeout,
            UnaryOperator<T> onTimeout) {

        long startTime = currentTimeMillis();
        long endTime = startTime + timeout.toMillis();

        T lastValue;
        do {

            lastValue = functionToPoll.get();
            if (isResult.test(lastValue)) {
                return lastValue;
            }

            // Before polling the next time we sleep a little to give other
            // threads a better chance to do their jobs.
            // The duration of the sleep adapts over the time: first we poll
            // very frequently, i.e. sleep only a little. Then the sleep time
            // increments until it reaches MAX_SLEEP_BETWEEN_POLLS_MILLIS.
            // This way we can react quickly if the functionToPoll "is fast",
            // but don't waste CPU time when the functionToPoll "is slow", i.e.
            // hasn't provided the expected value in the fast phase.
            long timeToSleep = currentTimeMillis() - startTime;

            try {
                //noinspection BusyWait
                Thread.sleep(Math.min(timeToSleep, MAX_SLEEP_BETWEEN_POLLS_MILLIS));
            } catch (InterruptedException e) {
                // When the Thread is interrupted behave as if timeouted.
                Thread.currentThread().interrupt();
                break;
            }

        } while (currentTimeMillis() < endTime);

        return onTimeout.apply(lastValue);
    }

}
