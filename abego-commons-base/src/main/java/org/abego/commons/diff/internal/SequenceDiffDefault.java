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

import org.abego.commons.seq.Seq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.abego.commons.diff.internal.AlignedItemPair.newAlignedItemPair;
import static org.abego.commons.lang.CharacterUtil.NEWLINE_CHAR;
import static org.abego.commons.lang.CharacterUtil.TAB_CHAR;
import static org.abego.commons.seq.SeqUtil.newSeq;

/**
 * Provides a implementation for the SequenceDiff interface using a dynamic
 * programming algorithm to compute edit distances.
 * <p>
 * The algorithm has a time and space complexity of O(n*m).
 * <p>
 * For details see:
 * <blockquote>
 * Dan Gusfield - Algorithms on Strings, Trees and Sequences; <br>
 * Cambridge University Press, 1999; pp. 215
 * </blockquote>
 */
final class SequenceDiffDefault<T> implements SequenceDiff<T> {

    private static final int MOVE_UP_MASK = 0x01;
    private static final int MOVE_LEFT_MASK = 0x02;
    private static final int MOVE_DIAGONAL_MASK = 0x04;

    // package private for tests
    static <T> String tableToString(
            Object[][] table,
            Seq<T> sequence1,
            Seq<T> sequence2) {
        char colSeparator = TAB_CHAR;

        StringBuilder sb = new StringBuilder();
        sb.append(colSeparator);
        for (T o : sequence1) {
            sb.append(colSeparator);
            sb.append(o);
        }
        for (int i2 = 0; i2 <= sequence2.size(); i2++) {
            sb.append(NEWLINE_CHAR);
            if (i2 > 0) {
                sb.append(sequence2.item(i2 - 1));
            }
            for (int i1 = 0; i1 <= sequence1.size(); i1++) {
                sb.append(colSeparator);
                sb.append(table[i1][i2]);
            }
        }
        return sb.toString();
    }

    public Seq<AlignedItemPair<T>> alignItems(
            Seq<T> sequenceA,
            Seq<T> sequenceB) {

        List<AlignedItemPair<T>> result = new ArrayList<>();

        Item[][] table = calcEditDistanceTable(sequenceA, sequenceB);

        int i1 = sequenceA.size();

        int i2 = sequenceB.size();
        while (i1 > 0 || i2 > 0) {
            // Determine the move to make.
            // First check if a preferred move is defined. If not use the
            // "normal" one
            int move = table[i1][i2].getPreferredMove();
            if (move == 0) {
                move = table[i1][i2].getMoveMask();
            }

            // Create the proper result pair, based on the move.
            if ((move & MOVE_DIAGONAL_MASK) != 0) {
                result.add(0, newAlignedItemPair(
                        sequenceA.item(i1 - 1),
                        sequenceB.item(i2 - 1)));
                i1--;
                i2--;

            } else if ((move & MOVE_LEFT_MASK) != 0) {
                result.add(0, newAlignedItemPair(sequenceA.item(i1 - 1), null));
                i1--;

            } else {
                result.add(0, newAlignedItemPair(null, sequenceB.item(i2 - 1)));
                i2--;
            }
        }
        return newSeq(result);
    }

    Item[][] calcEditDistanceTable(
            Seq<T> sequence1,
            Seq<T> sequence2) {
        int sequence1Len = sequence1.size();
        int sequence2Len = sequence2.size();

        Item[][] table = new Item[sequence1Len + 1][sequence2Len + 1];

        // Fill the first column/row
        table[0][0] = new Item(0, 0, 0, 0);
        for (int i1 = 1; i1 <= sequence1Len; i1++) {
            table[i1][0] = new Item(
                    i1,
                    MOVE_LEFT_MASK,
                    0,
                    MOVE_LEFT_MASK);
        }
        for (int i2 = 0; i2 <= sequence2Len; i2++) {
            table[0][i2] = new Item(
                    i2,
                    MOVE_UP_MASK,
                    0,
                    MOVE_UP_MASK);
        }

        for (int i1 = 1; i1 <= sequence1Len; i1++) {
            for (int i2 = 1; i2 <= sequence2Len; i2++) {
                boolean matched = Objects.equals(
                        sequence1.item(i1 - 1),
                        sequence2.item(i2 - 1));
                int effortLeft = table[i1 - 1][i2].getEffort();
                int effortUp = table[i1][i2 - 1].getEffort();
                int effortDiag = table[i1 - 1][i2 - 1].getEffort();
                int t = matched ? 0 : 1;

                int newEffort = Math.min(effortDiag + t, Math.min(
                        effortLeft + 1,
                        effortUp + 1));

                int moveMask = 0;
                int matchCount = 0;
                int preferredMove = 0;
                if (newEffort == effortUp + 1) {
                    moveMask |= MOVE_UP_MASK;
                    int aMatchCount = table[i1][i2 - 1]
                            .getMatchCount();
                    if (matchCount < aMatchCount) {
                        matchCount = aMatchCount;
                        preferredMove = MOVE_UP_MASK;
                    }
                }
                if (newEffort == effortLeft + 1) {
                    moveMask |= MOVE_LEFT_MASK;
                    int aMatchCount = table[i1 - 1][i2]
                            .getMatchCount();
                    if (matchCount < aMatchCount) {
                        matchCount = aMatchCount;
                        preferredMove = MOVE_LEFT_MASK;
                    }
                }
                if (newEffort == effortDiag + t) {
                    moveMask |= MOVE_DIAGONAL_MASK;
                    int aMatchCount = table[i1 - 1][i2 - 1]
                            .getMatchCount();
                    if (matchCount < aMatchCount) {
                        matchCount = aMatchCount;
                        preferredMove = MOVE_DIAGONAL_MASK;
                    }
                }

                if (matched) {
                    matchCount++;
                }
                table[i1][i2] = new Item(
                        newEffort,
                        moveMask,
                        matchCount,
                        preferredMove);
            }
        }
        return table;
    }


    static class Item {
        private final int effort;
        private final int moveMask;
        private final int matchCount;
        private final int preferredMove;

        public Item(
                int effort,
                int moveMask,
                int matchCount,
                int preferredMove) {
            this.effort = effort;
            this.moveMask = moveMask;
            this.matchCount = matchCount;
            this.preferredMove = preferredMove;
        }

        @SuppressWarnings("MagicCharacter")
        private static String getMoveMaskString(int mask) {
            String result = ""; //$NON-NLS-1$
            if ((mask & MOVE_LEFT_MASK) != 0) {
                result += '-';
            }
            if ((mask & MOVE_DIAGONAL_MASK) != 0) {
                result += '\\';
            }
            if ((mask & MOVE_UP_MASK) != 0) {
                result += '|';
            }
            return result;
        }

        public int getEffort() {
            return effort;
        }

        public int getMatchCount() {
            return matchCount;
        }

        public int getMoveMask() {
            return moveMask;
        }

        public String toString() {
            return String.format("%s[%d;%d%s]", //NON-NLS
                    getMoveMaskString(moveMask),
                    effort,
                    matchCount,
                    getMoveMaskString(preferredMove));
        }

        public int getPreferredMove() {
            return preferredMove;
        }
    }
}