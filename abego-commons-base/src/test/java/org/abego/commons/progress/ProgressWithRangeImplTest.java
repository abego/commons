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

import org.abego.commons.io.PrintStreamToBuffer;
import org.abego.commons.var.Var;
import org.abego.commons.var.VarUtil;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.function.LongSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressWithRangeImplTest {
    @Test
    void smoketest() {
        PrintStreamToBuffer printStream = PrintStreamToBuffer.newPrintStreamToBuffer();
        ProgressWithRange.Listener listener =
                Progresses.createProgressListenerWithOutputTo(printStream);

        // we need to "control the time" for reliable tests
        Var<Long> currentMillis = VarUtil.newVar(0L);
        ProgressWithRange.Options options = new ProgressWithRange.Options() {
            @Override
            public LongSupplier getCurrentMillisSupplier() {
                return currentMillis::get;
            }
        };

        ProgressWithRange progress = ProgressWithRangeImpl.createProgressWithRange(
                "Topic", 0, 10,
                listener,
                options);

        assertEquals("Topic", progress.getTopic());
        assertEquals(0, progress.getMinValue());
        assertEquals(10, progress.getMaxValue());
        assertEquals(0, progress.getValue());
        assertEquals("", progress.getText());
        assertFalse(progress.isClosed());
        // No event is created during ProgressWithRange creation
        assertEquals("", printStream.text());


        progress.update(0, "");
        assertEquals(0, progress.getValue());
        assertEquals("", progress.getText());
        assertFalse(progress.isClosed());
        assertEquals("[0 s] Topic (0 of 10, 0 %, remaining time: ?)\n", printStream.text());

        // update with no time elapsed, no event 
        progress.update(1, "one");
        assertEquals(1, progress.getValue());
        assertEquals("one", progress.getText());
        assertFalse(progress.isClosed());
        assertEquals("[0 s] Topic (0 of 10, 0 %, remaining time: ?)\n", printStream.text());

        // 2 seconds later, event emitted 
        currentMillis.set(2000L);
        progress.update(2, "two");
        assertEquals(2, progress.getValue());
        assertEquals("two", progress.getText());
        assertEquals("" +
                "[0 s] Topic (0 of 10, 0 %, remaining time: ?)\n" +
                "[2 s] Topic (2 of 10, 20 %, remaining time: 8 s) - two\n", printStream.text());

        // 0.5 seconds later (not enough time, no event) 
        currentMillis.set(2500L);
        progress.update(3, "three");
        assertEquals(3, progress.getValue());
        assertEquals("three", progress.getText());
        assertEquals("" +
                "[0 s] Topic (0 of 10, 0 %, remaining time: ?)\n" +
                "[2 s] Topic (2 of 10, 20 %, remaining time: 8 s) - two\n", printStream.text());

        // another 0.5 seconds later, 1 second after last event, event emitted
        currentMillis.set(3000L);
        progress.update(4, "four");
        assertEquals(4, progress.getValue());
        assertEquals("four", progress.getText());
        assertEquals("" +
                "[0 s] Topic (0 of 10, 0 %, remaining time: ?)\n" +
                "[2 s] Topic (2 of 10, 20 %, remaining time: 8 s) - two\n" +
                "[3 s] Topic (4 of 10, 40 %, remaining time: 4 s) - four\n", printStream.text());

        // big jump in time, event emitted
        currentMillis.set(9001L);
        progress.update(9, "nine");
        assertEquals(9, progress.getValue());
        assertEquals("nine", progress.getText());
        assertEquals("" +
                "[0 s] Topic (0 of 10, 0 %, remaining time: ?)\n" +
                "[2 s] Topic (2 of 10, 20 %, remaining time: 8 s) - two\n" +
                "[3 s] Topic (4 of 10, 40 %, remaining time: 4 s) - four\n" +
                "[9 s] Topic (9 of 10, 90 %, remaining time: 1 s) - nine\n", printStream.text());

        // only 999ms since last event, no event emitted
        // (also: values out of range are fixed (here 11 not in 0-10))
        currentMillis.set(10000L);
        progress.update(11, "eleven->ten");
        assertEquals(10, progress.getValue());
        assertEquals("eleven->ten", progress.getText());
        assertFalse(progress.isClosed());
        assertEquals("" +
                "[0 s] Topic (0 of 10, 0 %, remaining time: ?)\n" +
                "[2 s] Topic (2 of 10, 20 %, remaining time: 8 s) - two\n" +
                "[3 s] Topic (4 of 10, 40 %, remaining time: 4 s) - four\n" +
                "[9 s] Topic (9 of 10, 90 %, remaining time: 1 s) - nine\n", printStream.text());

        // closing. (only 999ms since last event, but as this is "close" an
        // event is emitted anyway. The pause only applies to update events.
        progress.close();
        assertEquals(10, progress.getValue());
        assertEquals("eleven->ten", progress.getText());
        assertTrue(progress.isClosed());
        assertEquals("" +
                "[0 s] Topic (0 of 10, 0 %, remaining time: ?)\n" +
                "[2 s] Topic (2 of 10, 20 %, remaining time: 8 s) - two\n" +
                "[3 s] Topic (4 of 10, 40 %, remaining time: 4 s) - four\n" +
                "[9 s] Topic (9 of 10, 90 %, remaining time: 1 s) - nine\n" +
                "[10 s] Topic (10 of 10, 100 %, remaining time: 0 s) - eleven->ten\n", printStream.text());
    }

    @Test
    void eventKind() {
        Var<ProgressWithRange> expectedProgress = VarUtil.newVar();

        // the listener just writes selected event info to "output".
        StringBuilder output = new StringBuilder();
        ProgressWithRange.Listener listener = event -> {
            assertSame(expectedProgress.get(), event.getSource());

            output.append(String.format("kind: %s, isFirstEvent: %b, isLastEvent: %b - %s\n",
                    event.getKind(),
                    event.isFirstEvent(), event.isLastEvent(),
                    event.getText()));
        };

        // no pause between update events, to avoid extra "time mocking".
        ProgressWithRange.Options options = new ProgressWithRange.Options() {
            @Override
            public Duration getPauseBetweenUpdateEvents() {
                return Duration.ofMillis(0);
            }
        };

        ProgressWithRange progress = ProgressWithRangeImpl.createProgressWithRange(
                "Topic", 0, 10,
                listener,
                options);
        expectedProgress.set(progress);

        // No events during ProgressWithRange creation
        assertEquals("", output.toString());

        progress.update(1, "one");
        assertEquals("" +
                "kind: BEGIN, isFirstEvent: true, isLastEvent: false - one\n" +
                "kind: UPDATE, isFirstEvent: false, isLastEvent: false - one\n", output.toString());

        progress.update(2, "two");
        assertEquals("" +
                "kind: BEGIN, isFirstEvent: true, isLastEvent: false - one\n" +
                "kind: UPDATE, isFirstEvent: false, isLastEvent: false - one\n" +
                "kind: UPDATE, isFirstEvent: false, isLastEvent: false - two\n", output.toString());

        progress.close();
        assertEquals("kind: BEGIN, isFirstEvent: true, isLastEvent: false - one\n" +
                "kind: UPDATE, isFirstEvent: false, isLastEvent: false - one\n" +
                "kind: UPDATE, isFirstEvent: false, isLastEvent: false - two\n" +
                "kind: END, isFirstEvent: false, isLastEvent: true - two\n", output.toString());

        // close after close generate no events (but log a warning)
        progress.close();
        assertEquals("kind: BEGIN, isFirstEvent: true, isLastEvent: false - one\n" +
                "kind: UPDATE, isFirstEvent: false, isLastEvent: false - one\n" +
                "kind: UPDATE, isFirstEvent: false, isLastEvent: false - two\n" +
                "kind: END, isFirstEvent: false, isLastEvent: true - two\n", output.toString());

        // updates after close generate no events (but log a warning)
        progress.update(3, "three");
        assertEquals("kind: BEGIN, isFirstEvent: true, isLastEvent: false - one\n" +
                "kind: UPDATE, isFirstEvent: false, isLastEvent: false - one\n" +
                "kind: UPDATE, isFirstEvent: false, isLastEvent: false - two\n" +
                "kind: END, isFirstEvent: false, isLastEvent: true - two\n", output.toString());

    }

    @Test
    void invalidRange() {
        Exception e = assertThrows(Exception.class, () -> ProgressWithRangeImpl.createProgressWithRange(
                "Topic", 10, 5, EmptyProgressListener.getInstance()));
        assertEquals("minValue must be <= maxValue. Got minValue: 10, maxValue: 5", e.getMessage());
    }

    @Test
    void bugZeroSizeRangeAndPercentageDone() {
        StringBuilder output = new StringBuilder();
        ProgressWithRange.Listener listener = event -> output.append(
                String.format("Done: %d %%\n", event.getPercentageDone()));

        ProgressWithRange progress = ProgressWithRangeImpl.createProgressWithRange(
                "Topic", 10, 10,
                listener);
        progress.close();

        assertEquals("Done: 100 %\nDone: 100 %\n", output.toString());
    }
}
