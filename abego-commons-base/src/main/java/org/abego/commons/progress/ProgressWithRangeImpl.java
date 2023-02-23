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

import org.abego.commons.lang.IntUtil;

import java.time.Duration;
import java.util.Optional;
import java.util.function.LongSupplier;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

final class ProgressWithRangeImpl implements ProgressWithRange {

    private static final Logger LOGGER = getLogger(ProgressWithRangeImpl.class.getName());
    private final String topic;
    private final int minValue;
    private final int maxValue;
    private final Listener listener;
    private final LongSupplier currentMillisSupplier;
    private final long startTimeMillis;
    private final long pauseBetweenUpdateEventsMillis;
    private long lastEventTimeMills = 0;
    private int lastValue;
    private String lastText;
    private boolean hasEmittedEvents = false;
    private boolean closed = false;

    private ProgressWithRangeImpl(String topic,
                                  int minValue,
                                  int maxValue,
                                  Listener listener,
                                  Options options) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException(String.format(
                    "minValue must be <= maxValue. Got minValue: %d, maxValue: %d", //NON-NLS
                    minValue, maxValue));
        }
        this.topic = topic;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.listener = listener;
        this.currentMillisSupplier = options.getCurrentMillisSupplier();
        this.pauseBetweenUpdateEventsMillis =
                options.getPauseBetweenUpdateEvents().toMillis();
        this.startTimeMillis = currentMillisSupplier.getAsLong();
        this.lastValue = minValue;
        this.lastText = "";
    }

    public static ProgressWithRange createProgressWithRange(
            String topic, int minValue, int maxValue, Listener listener,
            Options options) {
        return new ProgressWithRangeImpl(
                topic, minValue, maxValue, listener, options);
    }

    public static ProgressWithRange createProgressWithRange(
            String topic, int minValue, int maxValue, Listener listener) {
        return new ProgressWithRangeImpl(
                topic, minValue, maxValue, listener, OPTIONS_DEFAULT);
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public int getValue() {
        return lastValue;
    }

    @Override
    public int getMinValue() {
        return minValue;
    }

    @Override
    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public String getText() {
        return lastText;
    }

    @Override
    public void update(int value, String text) {
        if (isClosed()) {
            LOGGER.warning("Calling 'update' on closed ProgressWithRange"); //NON-NLS
            return;
        }

        int validValue = IntUtil.limit(value, minValue, maxValue);
        if (value != validValue) {
            LOGGER.warning(String.format(
                    "Value %d is out of range, using %d instead.", value, validValue)); //NON-NLS
        }

        lastValue = validValue;
        lastText = text;

        long nowMillis = getCurrentTimeMillis();
        ensureBeginIsEmitted();
        if (lastEventTimeMills + pauseBetweenUpdateEventsMillis <= nowMillis) {
            emitEvent(EventKind.UPDATE);
        }
    }

    private void ensureBeginIsEmitted() {
        if (!hasEmittedEvents) {
            hasEmittedEvents = true;
            emitEvent(EventKind.BEGIN);
        }
    }

    @Override
    public void close() {
        if (isClosed()) {
            LOGGER.warning("Calling 'close' on closed ProgressWithRange"); //NON-NLS
            return;
        }
        closed = true;

        ensureBeginIsEmitted();
        emitEvent(EventKind.END);
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    private long getCurrentTimeMillis() {
        return currentMillisSupplier.getAsLong();
    }

    private void emitEvent(EventKind eventKind) {
        lastEventTimeMills = getCurrentTimeMillis();
        listener.accept(new EventImpl(
                eventKind, lastValue, lastText, lastEventTimeMills));
    }

    private class EventImpl implements Event {
        private final EventKind kind;
        private final int value;
        private final String text;
        private final long nowMillis;

        private EventImpl(EventKind kind, int value, String text, long nowMillis) {
            this.kind = kind;
            this.value = value;
            this.text = text;
            this.nowMillis = nowMillis;
        }

        @Override
        public EventKind getKind() {
            return kind;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public int getMinValue() {
            return minValue;
        }

        @Override
        public int getMaxValue() {
            return maxValue;
        }

        @Override
        public String getTopic() {
            return topic;
        }

        @Override
        public int getPercentageDone() {
            int rangeSize = getRangeSize();
            return rangeSize != 0
                    ? (getValue() - getMinValue()) * 100 / rangeSize
                    : 100;
        }

        @Override
        public Duration getElapsedTime() {
            return Duration.ofMillis(getElapsedTimeMillis());
        }

        private long getElapsedTimeMillis() {
            return nowMillis - startTimeMillis;
        }

        @Override
        public Optional<Duration> getRemainingTime() {
            int percentageDone = getPercentageDone();
            if (percentageDone > 0) {
                long timePerPercentMillis = getElapsedTimeMillis() / percentageDone;
                long remainingMillis = getPercentageRemaining() * timePerPercentMillis;
                return Optional.of(Duration.ofMillis(remainingMillis));
            } else {
                return Optional.empty();
            }
        }

        @Override
        public ProgressWithRange getSource() {
            return ProgressWithRangeImpl.this;
        }
    }
}
