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

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TimeoutServiceImplTest {

    @Test
    void newTimeoutService() {
        TimeoutService ts = TimeoutServiceImpl.newTimeoutService();

        assertNotNull(ts);
    }

    @Test
    void setTimeoutMillis() {
        TimeoutService ts = TimeoutServiceImpl.newTimeoutService();

        int durationInMillis = 11;
        ts.setTimeoutMillis(durationInMillis);

        assertEquals(Duration.ofMillis(durationInMillis), ts.timeout());
    }

    @Test
    void setInitialTimeout() {
        TimeoutService ts = TimeoutServiceImpl.newTimeoutService();

        Duration duration = Duration.ofMillis(13);
        ts.setInitialTimeout(duration);

        assertEquals(duration, ts.initialTimeout());
    }

    @Test
    void setTimeout() {
        TimeoutService ts = TimeoutServiceImpl.newTimeoutService();

        Duration duration = Duration.ofMillis(17);
        ts.setTimeout(duration);

        assertEquals(duration, ts.timeout());
    }

    @Test
    void resetTimeout() {
        TimeoutService ts = TimeoutServiceImpl.newTimeoutService();
        Duration duration = Duration.ofMillis(13);
        ts.setInitialTimeout(duration);
        ts.setTimeout(Duration.ofMillis(42));

        ts.resetTimeout();

        assertEquals(duration, ts.timeout());
    }

    @Test
    void runWithTimeout() {
        TimeoutService ts = TimeoutServiceImpl.newTimeoutService();

        Duration duration = Duration.ofMillis(16);
        ts.setTimeout(duration);
        Duration newDuration = Duration.ofMillis(9);
        ts.runWithTimeout(newDuration,
                () -> assertEquals(newDuration, ts.timeout()));

        assertEquals(duration, ts.timeout());
    }
}
