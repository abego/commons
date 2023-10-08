/*
 * MIT License
 *
 * Copyright (c) 2023 Udo Borkowski, (ub@abego.org)
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

import org.abego.commons.blackboard.Blackboard;
import org.abego.commons.blackboard.BlackboardDefault;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import static org.abego.commons.lang.ObjectUtil.ignore;
import static org.abego.commons.lang.ThreadUtil.runAsync;
import static org.abego.commons.lang.ThreadUtil.runInNewThread;
import static org.abego.commons.lang.ThreadUtil.sleep;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ThreadUtilTest {
    /**
     * The maximum duration of a "non-sleeping" sleep call, in milli seconds
     *
     * <p>(Determined experimentally)</p>
     */
    private static final long MAX_NO_SLEEP_MILLIS = 15L;

    @Test
    void constructorOk() {
        assertThrows(MustNotInstantiateException.class, ThreadUtil::new);
    }

    @Test
    void sleepZeroOk() {
        long t1 = System.currentTimeMillis();
        sleep(0);
        long dt = System.currentTimeMillis() - t1;

        assertTrue(dt <= MAX_NO_SLEEP_MILLIS, () -> "sleep duration in ms: " + dt);
    }

    @Test
    void sleepNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> sleep(-1));
    }

    @Test
    void sleepPositiveOk() {
        int millis = 20;

        long t1 = System.currentTimeMillis();
        sleep(millis);
        long dt = System.currentTimeMillis() - t1;

        assertTrue(dt >= millis, () -> "sleep duration in ms: " + dt);
    }


    @Test
    void sleepInterruptedOk() {
        int millis = 1000;

        Thread sleepingThread = Thread.currentThread();
        new Thread(() -> {
            // wait until the other thread sleeps
            //noinspection LoopConditionNotUpdatedInsideLoop
            while (sleepingThread.getState() != Thread.State.TIMED_WAITING) {
                // do nothing
                ignore(null);
            }
            // then interrupt that thread.
            sleepingThread.interrupt();
        }).start();

        assertFalse(Thread.interrupted());
        long t1 = System.currentTimeMillis();
        sleep(millis);
        long dt = System.currentTimeMillis() - t1;

        // sleep returned before the given duration (because it was interrupted)
        assertTrue(Thread.interrupted());
        assertTrue(dt < millis, () -> "sleep duration in ms: " + dt);
    }

    @Test
    void runAsyncOK() {

        Blackboard<String> bb = BlackboardDefault.newBlackboardDefault();

        Thread thread = runAsync(() -> bb.add("running"));

        // Wait for the output of the thread and the thread terminates
        long startTime = System.currentTimeMillis();
        int timeoutMillis = 1000;
        while (!bb.contains("running") && thread.getState() != Thread.State.TERMINATED) {
            if (System.currentTimeMillis() - startTime > timeoutMillis) {
                fail("Timeout: thread was not running");
            }
            ThreadUtil.sleep(10);
        }

    }

    @Test
    void runInNewThreadOK() {

        Blackboard<String> bb = BlackboardDefault.newBlackboardDefault();

        Thread thread = runInNewThread(() -> bb.add("running"));

        // Wait for the output of the thread and the thread terminates
        long startTime = System.currentTimeMillis();
        int timeoutMillis = 1000;
        while (!bb.contains("running") && thread.getState() != Thread.State.TERMINATED) {
            if (System.currentTimeMillis() - startTime > timeoutMillis) {
                fail("Timeout: thread was not running");
            }
            ThreadUtil.sleep(10);
        }

    }
}
