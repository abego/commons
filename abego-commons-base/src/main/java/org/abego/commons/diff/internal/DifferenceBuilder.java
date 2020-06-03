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
package org.abego.commons.diff.internal;

import org.abego.commons.diff.Difference;
import org.abego.commons.range.IntRangeBuilder;

import static org.abego.commons.range.IntRangeBuilder.newIntRangeBuilder;

final class DifferenceBuilder {
    private final IntRangeBuilder rangeInA;
    private final IntRangeBuilder rangeInB;

    public DifferenceBuilder(int startInA, int endInA, int startInB, int endInB) {
        rangeInA = newIntRangeBuilder(startInA, endInA);
        rangeInB = newIntRangeBuilder(startInB, endInB);
    }

    public DifferenceBuilder(int indexInA, int indexInB) {
        this(indexInA, indexInA, indexInB, indexInB);
    }

    public IntRangeBuilder getRangeInABuilder() {
        return rangeInA;
    }

    public IntRangeBuilder getRangeInBBuilder() {
        return rangeInB;
    }

    public Difference toDifference() {
        return DifferenceDefault.newDifference(
                getRangeInABuilder().toIntRange(),
                getRangeInBBuilder().toIntRange());
    }

}