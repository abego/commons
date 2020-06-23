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

package org.abego.commons.util.function;

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.util.function.BooleanSupplier;

@SuppressWarnings("WeakerAccess")
final public class BooleanSupplierUtil {

    public static final int DEFAULT_WAIT_MILLIS = 10;

    BooleanSupplierUtil() {
        throw new MustNotInstantiateException();
    }

    public static BooleanSupplier not(BooleanSupplier test) {
        return () -> !test.getAsBoolean();
    }

    public static void waitUntil(BooleanSupplier test) {
        waitUntil(test, DEFAULT_WAIT_MILLIS);
    }

    public static void waitUntil(BooleanSupplier test,
                                 int checkIntervalMillis) {
        while (!test.getAsBoolean()) {
            try {
                //noinspection BusyWait
                Thread.sleep(checkIntervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
