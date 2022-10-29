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

package org.abego.commons.timeout;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeoutTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, Timeout::new);
    }


    @Test
    void getTimeoutService() {
        TimeoutService ts = Timeout.getTimeoutService();

        assertNotNull(ts);
        assertEquals(ts, Timeout.getTimeoutService());
    }

    @Test
    void newTimeoutService() {
        TimeoutService ts = Timeout.newTimeoutService();

        assertNotNull(ts);
        assertNotEquals(ts, Timeout.newTimeoutService());
    }

    @Test
    void timeout() {

        Duration duration = Duration.ofMillis(17);
        Timeout.setTimeout(duration);

        assertEquals(duration, Timeout.timeout());
    }

    @Test
    void setTimeoutMillis() {
        int durationInMillis = 11;
        Timeout.setTimeoutMillis(durationInMillis);

        assertEquals(Duration.ofMillis(durationInMillis), Timeout.timeout());
    }

    @Test
    void setInitialTimeout() {
        Duration duration = Duration.ofMillis(13);
        Timeout.setInitialTimeout(duration);

        assertEquals(duration, Timeout.initialTimeout());
    }

    @Test
    void resetTimeout() {
        Duration duration = Duration.ofMillis(13);
        Timeout.setInitialTimeout(duration);
        Timeout.setTimeout(Duration.ofMillis(42));

        Timeout.resetTimeout();

        assertEquals(duration, Timeout.timeout());
    }

    @Test
    void runWithTimeout() {
        Duration duration = Duration.ofMillis(16);
        Timeout.setTimeout(duration);
        Duration newDuration = Duration.ofMillis(9);
        Timeout.runWithTimeout(newDuration,
                () -> assertEquals(newDuration, Timeout.timeout()));
        assertEquals(duration, Timeout.timeout());
    }

}
