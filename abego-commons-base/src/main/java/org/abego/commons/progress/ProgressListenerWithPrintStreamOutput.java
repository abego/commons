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

import java.io.PrintStream;

final class ProgressListenerWithPrintStreamOutput
        implements ProgressWithRangeImpl.Listener {

    private final PrintStream printStream;

    private ProgressListenerWithPrintStreamOutput(PrintStream printStream) {
        this.printStream = printStream;
    }

    public static ProgressListenerWithPrintStreamOutput createProgressListener(PrintStream printStream) {
        return new ProgressListenerWithPrintStreamOutput(printStream);
    }

    @Override
    public void accept(ProgressWithRangeImpl.Event event) {
        String decoratedText = !event.getText().isEmpty()
                ? String.format(" - %s", event.getText())  //NON-NLS
                : "";
        if (event.getMaxValue() == Integer.MAX_VALUE) {
            // Open range end: -> don't display remaining time etc.) 
            printStream.printf("[%d s] %s (%d)%s%n", //NON-NLS
                    event.getElapsedTime().getSeconds(),
                    event.getTopic(),
                    event.getOffsetInRange(),
                    decoratedText);
        } else {
            String remainingTimeText = event.getRemainingTime()
                    .map(t -> String.format("%d s", t.getSeconds())).orElse("?"); //NON-NLS
            printStream.printf("[%d s] %s (%d of %d, %d %%, remaining time: %s)%s%n", //NON-NLS
                    event.getElapsedTime().getSeconds(),
                    event.getTopic(),
                    event.getOffsetInRange(),
                    event.getRangeSize(),
                    event.getPercentageDone(),
                    remainingTimeText,
                    decoratedText);
        }
    }
}
