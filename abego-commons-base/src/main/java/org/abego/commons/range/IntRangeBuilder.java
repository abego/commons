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

import org.eclipse.jdt.annotation.Nullable;

import java.util.Objects;

import static java.lang.String.format;
import static org.abego.commons.range.IntRangeDefault.newIntRange;

public final class IntRangeBuilder {
    private int start;
    private int end;

    private IntRangeBuilder(int start, int end) {
        checkIntRangeParameters(start, end);

        this.start = start;
        this.end = end;
    }

    public static IntRangeBuilder newIntRangeBuilder(int start, int end) {
        return new IntRangeBuilder(start, end);
    }

    public static IntRangeBuilder of(int start, int end) {
        return new IntRangeBuilder(start, end);
    }

    private static void checkIntRangeParameters(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException(
                    format("start must be <= end. Got start = %d, end = %d", start, end)); //NON-NLS
        }
    }

    public IntRangeBuilder withStart(int start) {
        checkIntRangeParameters(start, end);

        this.start = start;
        return this;
    }

    public IntRangeBuilder withEnd(int end) {
        checkIntRangeParameters(start, end);

        this.end = end;
        return this;
    }

    public IntRange toIntRange() {
        return newIntRange(start, end);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof IntRangeBuilder)) return false;
        IntRangeBuilder that = (IntRangeBuilder) o;
        return start == that.start && end == that.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return format("IntRangeBuilder{start=%d, end=%d}", start, end); //NON-NLS
    }
}