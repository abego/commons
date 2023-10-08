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

/**
 * Typically instances of this class are used in a {@code try}-with-resources
 * statement to ensure some code is executed when the try block is exited.
 */
public final class RunOnClose implements AutoCloseable {

    private final Runnable runnable;

    private RunOnClose(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Return a RunOnClose that will run the {@code runnable} when the returned
     * instance's {@link #close()} method is called.
     */
    public static RunOnClose runOnClose(Runnable runnable) {

        return new RunOnClose(runnable);
    }

    /**
     * Run this object's {@link Runnable}.
     */
    @Override
    public void close() {
        runnable.run();
    }
}
