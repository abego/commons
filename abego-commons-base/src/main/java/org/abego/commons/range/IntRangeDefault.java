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

public final class IntRangeDefault implements IntRange {
    private final int start;
    private final int end;

    private IntRangeDefault(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException(String.format("start must be <= end, got %d and %d", start, end)); //NON-NLS
        }
        this.start = start;
        this.end = end;
    }

    public static IntRange newIntRange(int start, int end) {
        return new IntRangeDefault(start, end);
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return String.format("%d..%d", start, end); //NON-NLS
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof IntRange)) return false;
        IntRange that = (IntRange) o;
        return start == that.getStart() && end == that.getEnd();
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
