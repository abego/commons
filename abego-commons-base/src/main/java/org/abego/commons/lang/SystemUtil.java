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

import java.io.PrintStream;

import static java.util.Objects.requireNonNull;
import static org.abego.commons.lang.RunOnCloseUtil.resetOnClose;

public final class SystemUtil {

    SystemUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Set <code>System.out</code> to <code>newStream</code> and return a
     * {@link RunOnClose} that resets <code>System.out</code> to its
     * original stream.
     *
     * <p>The method is typically used in a try-with-resources statement like
     * this:</p>
     *
     * <pre>
     *  try (RunOnClose r = systemOutRedirect(out)) {
     *       // ... some code that outputs to System.out,
     *  }
     *
     *  // here System.out is using its original stream again
     * </pre>
     */
    public static RunOnClose systemOutRedirect(PrintStream newStream) {
        return resetOnClose(System::setOut, newStream,
                requireNonNull(System.out, "System.out must not be null")); //NON-NLS
    }

    /**
     * Set <code>System.err</code> to <code>newStream</code> and return a
     * {@link RunOnClose} that resets <code>System.err</code> to its
     * original stream.
     *
     * <p>The method is typically used in a try-with-resources statement like
     * this:</p>
     *
     * <pre>
     *  try (RunOnClose r = systemErrRedirect(err)) {
     *       // ... some code that outputs to System.err
     *  }
     *
     *  // here System.err is using its original stream again
     * </pre>
     */
    public static RunOnClose systemErrRedirect(PrintStream newStream) {
        return resetOnClose(System::setErr, newStream,
                requireNonNull(System.err, "System.err must not be null")); //NON-NLS
    }


    /**
     * Return the line separator of the current system, as provided by {@code  System.getProperty("line.separator")}.
     *
     * <p>The value may differ from {@link System#lineSeparator()} that always returns the same String per platform.</p>
     */
    public static String getLineSeparator() {
        return requireNonNull(System.getProperty("line.separator"), "System property 'line.separator' not defined"); //NON-NLS
    }
}
