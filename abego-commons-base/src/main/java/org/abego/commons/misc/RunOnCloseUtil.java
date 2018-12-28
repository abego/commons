/*
 * MIT License
 *
 * Copyright (c) 2018 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.misc;

import java.util.function.Consumer;

import static org.abego.commons.lang.exception.MustNotInstantiateException.throwMustNotInstantiate;

public class RunOnCloseUtil {

    RunOnCloseUtil() {
        throwMustNotInstantiate();
    }

    /**
     * Set the value associated with the {@code setter} to
     * {@code initialValue} and return a {@link RunOnClose} that
     * {@link RunOnClose#close()} method will set the value to
     * {@code onCloseValue}.
     *
     * <p>Typically this method is used in a {@code try}-with-resources
     * statement to ensure a changed value is reset to an "original" value
     * when the try block is exited.</p>
     */
    public static <T> RunOnClose resetOnClose(
            Consumer<T> setter, T initialValue, T onCloseValue) {
        setter.accept(initialValue);
        Runnable runnable = () -> setter.accept(onCloseValue);
        return RunOnClose.runOnClose(runnable);
    }
}
