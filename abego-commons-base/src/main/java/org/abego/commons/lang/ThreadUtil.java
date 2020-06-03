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

public final class ThreadUtil {

    ThreadUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Pause the execution of the currently executing thread for
     * <code>millis</code> milliseconds.
     *
     * <p>This is wrapper around {@link Thread#sleep(long)} that catches
     * {@link InterruptedException}s, setting the thread's interrupted flag in
     * those cases.</p>
     *
     * @throws IllegalArgumentException if the value of {@code millis} is negative
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Run the {@code runnable} in a new {@link Thread} and return
     * that new Thread.
     *
     * @deprecated use {@link #runAsync(Runnable)} instead
     */
    @Deprecated
    public static Thread runInNewThread(Runnable runnable) {
        return runAsync(runnable);
    }

    /**
     * Run the {@code runnable} asynchronously, in a new {@link Thread} and return
     * that new Thread.
     */
    public static Thread runAsync(Runnable runnable) {

        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

}
