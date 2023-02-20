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

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.LongSupplier;

public interface ProgressWithRange {
    Duration PAUSE_BETWEEN_UPDATE_EVENTS_DEFAULT = Duration.ofSeconds(1);
    LongSupplier CURRENT_TIME_MILLIS_SUPPLIER_DEFAULT = System::currentTimeMillis;
    Options OPTIONS_DEFAULT = new Options() {
    };

    String getTopic();

    int getValue();

    int getMinValue();

    int getMaxValue();

    String getText();

    void update(int value, String text);

    void close();

    boolean isClosed();

    enum EventKind {
        BEGIN, UPDATE, END
    }

    interface Options {
        default Duration getPauseBetweenUpdateEvents() {
            return PAUSE_BETWEEN_UPDATE_EVENTS_DEFAULT;
        }

        default LongSupplier getCurrentMillisSupplier() {
            return CURRENT_TIME_MILLIS_SUPPLIER_DEFAULT;
        }
    }

    interface Event {
        EventKind getKind();

        default boolean isFirstEvent() {
            return getKind() == EventKind.BEGIN;
        }

        default boolean isLastEvent() {
            return getKind() == EventKind.END;
        }

        int getValue();

        String getText();

        int getMinValue();

        int getMaxValue();

        default int getRangeSize() {
            return getMaxValue() - getMinValue();
        }

        default int getOffsetInRange() {
            return getValue() - getMinValue();
        }

        String getTopic();

        int getPercentageDone();

        default int getPercentageRemaining() {
            return 100 - getPercentageDone();
        }

        Duration getElapsedTime();

        Optional<Duration> getRemainingTime();

        ProgressWithRange getSource();
    }

    interface Listener extends Consumer<Event> {
    }
}
