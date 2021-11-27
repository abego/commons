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

package org.abego.commons.timeout;

import java.time.Duration;

/**
 * Factory for {@link TimeoutSupport} and access to default instance.
 */
public class Timeouts {
    private static final TimeoutSupport INSTANCE = newTimeoutSupport();

    /**
     * Returns the default TimeoutSupport instance.
     */
    public static TimeoutSupport getTimeoutSupport() {
        return INSTANCE;
    }

    /**
     * Returns the newly created TimeoutSupport instance.
     */
    public static TimeoutSupport newTimeoutSupport() {
        return TimeoutSupportImpl.newTimeoutSupport();
    }

    /**
     * Return the duration to use as timeout when calling a
     * "{@link Timeoutable}" method with no explicitly specified timeout.
     * <p>
     * Works on the default TimeoutSupport instance as returned by
     * {@link #getTimeoutSupport()}.
     */
    public static Duration timeout() {
        return getTimeoutSupport().timeout();
    }

    /**
     * Sets the {@code timeout} property, as returned by
     * {@link TimeoutSupplier#timeout()}, to the given {@code duration}.
     * <p>
     * Works on the default TimeoutSupport instance as returned by
     * {@link #getTimeoutSupport()}.
     *
     * @param duration the timeout duration
     */
    public static void setTimeout(Duration duration) {
        getTimeoutSupport().setTimeout(duration);
    }

    /**
     * Sets the {@code timeout} property, as returned by {@link TimeoutSupplier#timeout()}, to
     * the given {@code durationInMillis}.
     * <p>
     * Works on the default TimeoutSupport instance as returned by
     * {@link #getTimeoutSupport()}.
     *
     * @param durationInMillis the duration of the timeout, in milliseconds
     */
    public static void setTimeoutMillis(long durationInMillis) {
        getTimeoutSupport().setTimeoutMillis(durationInMillis);
    }

    /**
     * Returns the duration of the initial timeout, i.e. the value
     * {@link TimeoutSupplier#timeout()} returns initially or after a "reset".
     *
     * <p>See {@link TimeoutSupplier#timeout()}</p>
     * <p>
     * Works on the default TimeoutSupport instance as returned by
     * {@link #getTimeoutSupport()}.
     *
     * @return the duration of the initial timeout, i.e. the value
     * {@link TimeoutSupplier#timeout()} returns initially or after a "reset"
     */
    public static Duration initialTimeout() {
        return getTimeoutSupport().initialTimeout();
    }

    /**
     * Sets the {@code initialTimeout} property, as returned by
     * {@link #initialTimeout()}.
     * <p>
     * Works on the default TimeoutSupport instance as returned by
     * {@link #getTimeoutSupport()}.
     *
     * @param duration the new duration
     */
    public static void setInitialTimeout(Duration duration) {
        getTimeoutSupport().setInitialTimeout(duration);
    }

    /**
     * Runs the {@code runnable}, with the timeout temporarily set
     * to the {@code timeoutDuration}.
     * <p>
     * Works on the default TimeoutSupport instance as returned by
     * {@link #getTimeoutSupport()}.
     *
     * @param timeoutDuration the duration of the timeout
     * @param runnable        the runnable to run
     */
    public static void runWithTimeout(Duration timeoutDuration, Runnable runnable) {
        getTimeoutSupport().runWithTimeout(timeoutDuration, runnable);
    }
}
