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

package org.abego.commons.test;

import org.abego.commons.timeout.Timeout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AssertRetryingTest {
    @Test
    void assertEqualsRetrying() {
        AssertRetrying.assertEqualsRetrying("foo", () -> "foo");
    }

    @Test
    void assertEqualsRetryingTimeout() {
        Timeout.setTimeoutMillis(1);
        Throwable e = Assertions.assertThrows(Throwable.class, () ->
                AssertRetrying.assertEqualsRetrying("foo", () -> "bar"));
        Assertions.assertEquals("[Timeout]  ==> expected: <foo> but was: <bar>", e.getMessage());
    }

    @Test
    void assertEqualsRetryingTimeoutWithMessage() {
        Timeout.setTimeoutMillis(1);
        Throwable e = Assertions.assertThrows(Throwable.class, () ->
                AssertRetrying.assertEqualsRetrying("foo", () -> "bar", "msg"));
        Assertions.assertEquals("[Timeout] msg ==> expected: <foo> but was: <bar>", e.getMessage());
    }

    @Test
    void assertTrueRetryingTimeout() {
        Timeout.setTimeoutMillis(1);
        Throwable e = Assertions.assertThrows(Throwable.class, () ->
                AssertRetrying.assertTrueRetrying(() -> false));
        Assertions.assertEquals("[Timeout]  ==> expected: <true> but was: <false>", e.getMessage());
    }

    @Test
    void assertTrueRetryingTimeoutWithMessage() {
        Timeout.setTimeoutMillis(1);
        Throwable e = Assertions.assertThrows(Throwable.class, () ->
                AssertRetrying.assertTrueRetrying(() -> false, "msg"));
        Assertions.assertEquals("[Timeout] msg ==> expected: <true> but was: <false>", e.getMessage());
    }

    @Test
    void assertSuccessRetrying() {
        Timeout.setTimeoutMillis(1);
        Throwable e = Assertions.assertThrows(Throwable.class, () ->
                AssertRetrying.assertSuccessRetrying(() -> {
                    throw new IllegalStateException();
                }));
        Assertions.assertEquals("[Timeout]  ==> expected: <Success> but was: <java.lang.IllegalStateException>", e.getMessage());
    }
}
