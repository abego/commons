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
package org.abego.commons.diff;

import org.abego.commons.range.IntRange;

/**
 * A difference between a sequence A and a sequence B, represented as aligned ranges in the sequences ("rangeInA" and
 * "rangeInB").
 *
 * <p>
 * Assume that sequence B is an edited version of sequence A the following holds:
 * <ul>
 * <li>When rangeInA is empty, the items of sequence B at rangeInB were added to sequence B.</li>
 * <li>When rangeInB is empty, the items of sequence A at rangeInA were deleted.</li>
 * <li>When rangeInA and rangeInB are both not empty and the items in both sequences differ,
 * the items of rangeInA of sequence A were replaced by the items of rangeInB of sequence B.</li>
 * <li>When rangeInA and rangeInB are both not empty and the items in both sequences are the
 * same, the items of rangeInA of sequence A were moved to a different location in sequence B
 * (defined by rangeInB).</li>
 *
 * </ul>
 */
public interface Difference {

    IntRange getRangeInA();

    IntRange getRangeInB();
}