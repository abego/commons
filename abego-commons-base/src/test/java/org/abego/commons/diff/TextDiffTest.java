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

import org.abego.commons.seq.Seq;
import org.junit.jupiter.api.Test;

import static org.abego.commons.range.IntRangeDefault.newIntRange;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextDiffTest {

    private static void assertDifferenceEquals(int expectedStartA, int expectedEndA, int expectedStartB, int expectedEndB, Difference diff) {
        assertEquals(newIntRange(expectedStartA, expectedEndA), diff
                .getRangeInA(), "rangeInA");
        assertEquals(newIntRange(expectedStartB, expectedEndB), diff
                .getRangeInB(), "rangeInB");
    }

    @Test
    void compareCharacterWise() {

        String oldText = "ABCDEFGHIJKLM";
        // "CD" deleted, "XY" inserted, "IJ" replaced by "VW"
        String newText = "ABEFXYGHVWKLM";

        Seq<Difference> diffs = TextDiff.compareCharacterWise(oldText, newText);

        assertEquals(3, diffs.size());

        // "CD" deleted
        assertDifferenceEquals(2, 4, 2, 2, diffs.item(0));

        // "XY" inserted
        assertDifferenceEquals(6, 6, 4, 6, diffs.item(1));

        // "IJ" replaced by "VW"
        assertDifferenceEquals(8, 10, 8, 10, diffs.item(2));
    }

    @Test
    void compareLineWise() {

        String oldText = "A\nB\nC\nD\nE\nF\nG\nH\nI\nJ\nK\nL\nM\n";
        // "C\nD\n" deleted, "X\nY\n" inserted, "I\nJ\n" replaced by "V\nW\n"
        String newText = "A\nB\nE\nF\nX\nY\nG\nH\nV\nW\nK\nL\nM\n";

        Seq<Difference> diffs = TextDiff.compareLineWise(oldText, newText);

        assertEquals(3, diffs.size());

        // "C\nD\n" deleted
        assertDifferenceEquals(2, 4, 2, 2, diffs.item(0));

        // "X\nY\n" inserted
        assertDifferenceEquals(6, 6, 4, 6, diffs.item(1));

        // "I\nJ\n" replaced by "V\nW\n"
        assertDifferenceEquals(8, 10, 8, 10, diffs.item(2));
    }

    @Test
    void getNoDifferences() {
        Seq<Difference> diffs = TextDiff.getNoDifferences();

        assertEquals(0, diffs.size());
    }
}