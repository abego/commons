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

package org.abego.commons.lang;

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.io.PrintStream;
import java.util.function.Predicate;

public final class StackTraceElementUtil {

    StackTraceElementUtil() {
        throw new MustNotInstantiateException();
    }

    public static void printStackTrace(PrintStream out, StackTraceElement[] stackTrace, Predicate<StackTraceElement> includeElement) {
        for (StackTraceElement traceElement : stackTrace) {
            if (includeElement.test(traceElement)) {
                out.println(traceElement);
            }
        }
    }

    public static void printStackTrace(PrintStream out, StackTraceElement[] stackTrace) {
        printStackTrace(out, stackTrace, e -> true);
    }

    /**
     * EXPERIMENTAL: exclude "non-client" code from the stack trace to reduce
     * the noise.
     */
    public static void printStackTraceOnlyClientCode(PrintStream out, StackTraceElement[] stackTrace) {
        printStackTrace(out, stackTrace, e ->
                !e.getClassName().startsWith("java.") &&
                        !e.getClassName().startsWith("javax.") &&
                        !e.getClassName().startsWith("org.abego.commons.") &&
                        !e.getClassName().startsWith("org.abego.event.")
        );
    }
}
