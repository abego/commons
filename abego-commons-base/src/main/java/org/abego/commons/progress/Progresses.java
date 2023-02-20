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

package org.abego.commons.progress;

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.io.PrintStream;

public final class Progresses {
    Progresses() {
        throw new MustNotInstantiateException();
    }

    public static ProgressWithRange createProgressWithRange(
            String topic,
            int minValue, int maxValue,
            ProgressWithRange.Listener listener,
            ProgressWithRange.Options options) {

        return ProgressWithRangeImpl.createProgressWithRange(
                topic, minValue, maxValue, listener, options);
    }

    public static ProgressWithRange createProgressWithRange(
            String topic,
            int minValue, int maxValue,
            ProgressWithRange.Listener listener) {

        return createProgressWithRange(
                topic, minValue, maxValue, listener,
                ProgressWithRange.OPTIONS_DEFAULT);
    }

    public static ProgressWithRange createProgressWithRange(
            String topic,
            int count,
            ProgressWithRange.Listener listener) {

        return createProgressWithRange(
                topic, 0, count, listener,
                ProgressWithRange.OPTIONS_DEFAULT);
    }

    public static ProgressWithRange.Listener getEmptyProgressListener() {
        return EmptyProgressListener.getInstance();
    }

    public static ProgressWithRange.Listener createProgressListenerWithOutputTo(
            PrintStream printStream) {
        return ProgressListenerWithPrintStreamOutput.createProgressListener(printStream);
    }
}
