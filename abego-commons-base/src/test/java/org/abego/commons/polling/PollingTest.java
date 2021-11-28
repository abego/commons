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

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.timeout.TimeoutUncheckedException;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.abego.commons.polling.Polling.poll;
import static org.abego.commons.polling.Polling.pollNoFail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PollingTest {
    @Test
    void constructorOk() {
        assertThrows(MustNotInstantiateException.class, Polling::new);
    }

    @Test
    void pollOk() {
        Duration timeout = Duration.ofMillis(20);
        long waitTimeMillis = 5;
        long endTime = System.currentTimeMillis() + waitTimeMillis;

        Long t = poll(System::currentTimeMillis, i -> i >= endTime, timeout);

        assertTrue(t >= endTime);
    }

    @Test
    void pollNoFailOk() {
        Duration timeout = Duration.ofMillis(100);
        long waitTimeMillis = 5;
        long endTime = System.currentTimeMillis() + waitTimeMillis;

        Long t = pollNoFail(System::currentTimeMillis, i -> i >= endTime, timeout);

        assertTrue(t >= endTime, () -> "Expected t >= endTime, got t=" + t + ", endTime=" + endTime);
    }

    @Test
    void pollTimeoutOk() {
        Duration timeout = Duration.ofMillis(20);
        long waitTimeMillis = 100;
        long endTime = System.currentTimeMillis() + waitTimeMillis;

        assertThrows(TimeoutUncheckedException.class,
                () -> poll(System::currentTimeMillis, i -> i >= endTime, timeout));

    }

    /**
     * Interrupting the Thread running poll will behave as if there was
     * a timeout.
     */
    @Test
    void pollInterruptedOk() {

        // use a long timeout so it won't kick in
        Duration timeout = Duration.ofMillis(200);
        long waitTimeMillis = 100;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + waitTimeMillis;

        Thread myThread = Thread.currentThread();

        // calling interrupt triggers the TimeoutUncheckedException
        assertThrows(TimeoutUncheckedException.class,
                () -> poll(System::currentTimeMillis, i -> {
                    myThread.interrupt(); // force the timeout early
                    return i >= endTime;
                }, timeout));

        long t = System.currentTimeMillis();
        assertTrue(t < endTime);
    }

    @Test
    void pollNoFailTimeoutOk() {
        Duration timeout = Duration.ofMillis(20);
        long waitTimeMillis = 100;
        long endTime = System.currentTimeMillis() + waitTimeMillis;

        Long t = pollNoFail(System::currentTimeMillis, i -> i >= endTime, timeout);

        assertTrue(t < endTime);

    }
}