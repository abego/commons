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

package org.abego.commons.test;

import org.abego.commons.timeout.Timeout;
import org.abego.commons.timeout.TimeoutSupplier;
import org.abego.commons.timeout.Timeoutable;
import org.eclipse.jdt.annotation.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Factory for {@link AssertRetryingService} and access to default instance.
 */
public final class AssertRetrying {
    private static final AssertRetryingService INSTANCE =
            newAssertRetryingService(Timeout.getTimeoutService());

    /**
     * Returns the default AssertRetryingService instance.
     * <p>
     * The AssertRetryingService uses the default
     * {@link org.abego.commons.timeout.TimeoutService} instance,
     * as provided by
     * {@link Timeout#getTimeoutService()}.
     */
    public static AssertRetryingService getAssertRetryingService() {
        return INSTANCE;
    }

    /**
     * Returns the newly created AssertRetryingService instance.
     */
    public static AssertRetryingService newAssertRetryingService(TimeoutSupplier timeoutProvider) {
        return AssertRetryingServiceImpl.newAssertRetryingService(timeoutProvider);
    }

    @Timeoutable
    public static <T> void assertEqualsRetrying(T expected,
                                                Supplier<T> actualSupplier,
                                                @Nullable String message) {
        getAssertRetryingService().assertEqualsRetrying(expected, actualSupplier, message);
    }

    @Timeoutable
    public static <T> void assertEqualsRetrying(T expected, Supplier<T> actualSupplier) {
        getAssertRetryingService().assertEqualsRetrying(expected, actualSupplier);
    }

    @Timeoutable
    public static void assertTrueRetrying(BooleanSupplier actualSupplier,
                                          @Nullable String message) {
        getAssertRetryingService().assertTrueRetrying(actualSupplier, message);
    }

    @Timeoutable
    public static void assertTrueRetrying(BooleanSupplier actualSupplier) {
        getAssertRetryingService().assertTrueRetrying(actualSupplier);
    }

    @Timeoutable
    public static void assertSuccessRetrying(Runnable runnable) {
        getAssertRetryingService().assertSuccessRetrying(runnable);
    }

}
