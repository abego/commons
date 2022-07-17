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

package org.abego.commons.diff.internal;

import org.abego.commons.seq.Seq;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;

import static org.abego.commons.diff.internal.DiffImpl.characters;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SequenceDiffDefaultTest {

    private static final Seq<Character> SAMPLE_OLD_TEXT = characters("ABCDEFGHIJKLM");
    // "CD" deleted, "XY" inserted, "IJ" replaced by "VW"
    private static final Seq<Character> SAMPLE_NEW_TEXT = characters("ABEFXYGHVWKLM");

    public static Seq<AlignedItemPair<Character>> getAlignedItemsSample() {
        SequenceDiff<Character> algorithm = new SequenceDiffDefault<>();

        return algorithm.alignItems(SAMPLE_OLD_TEXT, SAMPLE_NEW_TEXT);
    }

    @Test
    void alignItems() {
        Seq<AlignedItemPair<Character>> alignedItems = getAlignedItemsSample();

        assertEquals(15, alignedItems.size());
        assertAlignedItemEquals('A', 'A', alignedItems.item(0));
        assertAlignedItemEquals('B', 'B', alignedItems.item(1));
        assertAlignedItemEquals('C', null, alignedItems.item(2));
        assertAlignedItemEquals('D', null, alignedItems.item(3));
        assertAlignedItemEquals('E', 'E', alignedItems.item(4));
        assertAlignedItemEquals('F', 'F', alignedItems.item(5));
        assertAlignedItemEquals(null, 'X', alignedItems.item(6));
        assertAlignedItemEquals(null, 'Y', alignedItems.item(7));
        assertAlignedItemEquals('G', 'G', alignedItems.item(8));
        assertAlignedItemEquals('H', 'H', alignedItems.item(9));
        assertAlignedItemEquals('I', 'V', alignedItems.item(10));
        assertAlignedItemEquals('J', 'W', alignedItems.item(11));
        assertAlignedItemEquals('K', 'K', alignedItems.item(12));
        assertAlignedItemEquals('L', 'L', alignedItems.item(13));
        assertAlignedItemEquals('M', 'M', alignedItems.item(14));
    }

    private void assertAlignedItemEquals(@Nullable Character expectedFirstItem, @Nullable Character expectedSecondItem, AlignedItemPair<Character> actualAlignedItemPair) {
        assertEquals(expectedFirstItem, actualAlignedItemPair.first());
        assertEquals(expectedSecondItem, actualAlignedItemPair.second());
    }

    @Test
    void calcEditDistanceTable() {
        SequenceDiffDefault<Character> algorithm = new SequenceDiffDefault<>();
        SequenceDiffDefault.Item[][] table = algorithm
                .calcEditDistanceTable(SAMPLE_OLD_TEXT, SAMPLE_NEW_TEXT);

        assertEquals("\t\tA\tB\tC\tD\tE\tF\tG\tH\tI\tJ\tK\tL\tM\n" +
                        "\t|[0;0|]\t-[1;0-]\t-[2;0-]\t-[3;0-]\t-[4;0-]\t-[5;0-]\t-[6;0-]\t-[7;0-]\t-[8;0-]\t-[9;0-]\t-[10;0-]\t-[11;0-]\t-[12;0-]\t-[13;0-]\n" +
                        "A\t|[1;0|]\t\\[0;1]\t-[1;1-]\t-[2;1-]\t-[3;1-]\t-[4;1-]\t-[5;1-]\t-[6;1-]\t-[7;1-]\t-[8;1-]\t-[9;1-]\t-[10;1-]\t-[11;1-]\t-[12;1-]\n" +
                        "B\t|[2;0|]\t|[1;1|]\t\\[0;2\\]\t-[1;2-]\t-[2;2-]\t-[3;2-]\t-[4;2-]\t-[5;2-]\t-[6;2-]\t-[7;2-]\t-[8;2-]\t-[9;2-]\t-[10;2-]\t-[11;2-]\n" +
                        "E\t|[3;0|]\t|[2;1|]\t|[1;2|]\t\\[1;2\\]\t-\\[2;2-]\t\\[2;3\\]\t-[3;3-]\t-[4;3-]\t-[5;3-]\t-[6;3-]\t-[7;3-]\t-[8;3-]\t-[9;3-]\t-[10;3-]\n" +
                        "F\t|[4;0|]\t|[3;1|]\t|[2;2|]\t\\|[2;2|]\t\\[2;2\\]\t-\\|[3;3|]\t\\[2;4\\]\t-[3;4-]\t-[4;4-]\t-[5;4-]\t-[6;4-]\t-[7;4-]\t-[8;4-]\t-[9;4-]\n" +
                        "X\t|[5;0|]\t|[4;1|]\t|[3;2|]\t\\|[3;2|]\t\\|[3;2|]\t\\[3;2\\]\t|[3;4|]\t\\[3;4\\]\t-\\[4;4-]\t-\\[5;4-]\t-\\[6;4-]\t-\\[7;4-]\t-\\[8;4-]\t-\\[9;4-]\n" +
                        "Y\t|[6;0|]\t|[5;1|]\t|[4;2|]\t\\|[4;2|]\t\\|[4;2|]\t\\|[4;2|]\t\\|[4;4|]\t\\|[4;4|]\t\\[4;4\\]\t-\\[5;4-]\t-\\[6;4-]\t-\\[7;4-]\t-\\[8;4-]\t-\\[9;4-]\n" +
                        "G\t|[7;0|]\t|[6;1|]\t|[5;2|]\t\\|[5;2|]\t\\|[5;2|]\t\\|[5;2|]\t\\|[5;4|]\t\\[4;5\\]\t-\\|[5;5-]\t\\[5;4\\]\t-\\[6;4-]\t-\\[7;4-]\t-\\[8;4-]\t-\\[9;4-]\n" +
                        "H\t|[8;0|]\t|[7;1|]\t|[6;2|]\t\\|[6;2|]\t\\|[6;2|]\t\\|[6;2|]\t\\|[6;4|]\t|[5;5|]\t\\[4;6\\]\t-[5;6-]\t-\\[6;6-]\t-\\[7;6-]\t-\\[8;6-]\t-\\[9;6-]\n" +
                        "V\t|[9;0|]\t|[8;1|]\t|[7;2|]\t\\|[7;2|]\t\\|[7;2|]\t\\|[7;2|]\t\\|[7;4|]\t|[6;5|]\t|[5;6|]\t\\[5;6\\]\t-\\[6;6-]\t-\\[7;6-]\t-\\[8;6-]\t-\\[9;6-]\n" +
                        "W\t|[10;0|]\t|[9;1|]\t|[8;2|]\t\\|[8;2|]\t\\|[8;2|]\t\\|[8;2|]\t\\|[8;4|]\t|[7;5|]\t|[6;6|]\t\\|[6;6|]\t\\[6;6\\]\t-\\[7;6-]\t-\\[8;6-]\t-\\[9;6-]\n" +
                        "K\t|[11;0|]\t|[10;1|]\t|[9;2|]\t\\|[9;2|]\t\\|[9;2|]\t\\|[9;2|]\t\\|[9;4|]\t|[8;5|]\t|[7;6|]\t\\|[7;6|]\t\\|[7;6|]\t\\[6;7\\]\t-[7;7-]\t-[8;7-]\n" +
                        "L\t|[12;0|]\t|[11;1|]\t|[10;2|]\t\\|[10;2|]\t\\|[10;2|]\t\\|[10;2|]\t\\|[10;4|]\t|[9;5|]\t|[8;6|]\t\\|[8;6|]\t\\|[8;6|]\t|[7;7|]\t\\[6;8\\]\t-[7;8-]\n" +
                        "M\t|[13;0|]\t|[12;1|]\t|[11;2|]\t\\|[11;2|]\t\\|[11;2|]\t\\|[11;2|]\t\\|[11;4|]\t|[10;5|]\t|[9;6|]\t\\|[9;6|]\t\\|[9;6|]\t|[8;7|]\t|[7;8|]\t\\[6;9\\]",
                SequenceDiffDefault
                        .tableToString(table, SAMPLE_OLD_TEXT, SAMPLE_NEW_TEXT));
    }

}
