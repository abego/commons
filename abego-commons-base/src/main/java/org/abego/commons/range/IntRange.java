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

package org.abego.commons.range;

/**
 * Represents the range of int numbers from {@code start} to {@code end} (with {@code start <= end}).
 *
 * <p>When applied to a sequence a range {@code start..end} refers to all
 * items of the sequence with {@code start <= index < end}</p>
 */
public interface IntRange {

    int getStart();

    int getEnd();

    /**
     * The length of the range, i.e. {@code getEnd() - getStart()}.
     */
    default int getLength() {
        return getEnd() - getStart();
    }

    default boolean isEmpty() {
        return getLength() == 0;
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    default boolean contains(IntRange otherRange) {
        return getStart() <= otherRange.getStart() &&
                otherRange.getEnd() <= getEnd();
    }
}
