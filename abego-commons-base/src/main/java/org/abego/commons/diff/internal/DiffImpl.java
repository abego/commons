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
import org.abego.commons.seq.Seq;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.abego.commons.lang.StringUtil.characters;
import static org.abego.commons.lang.StringUtil.escapedOrNull;
import static org.abego.commons.lang.StringUtil.lines;
import static org.abego.commons.lang.StringUtil.stringOrNull;
import static org.abego.commons.seq.SeqUtil.newSeq;

public final class DiffImpl {

    public static Seq<Difference> compareLineWise(String textA, String textB) {
        Seq<String> linesA = lines(textA);
        Seq<String> linesB = lines(textB);
        return compare(linesA, linesB);
    }

    public static Seq<Difference> compareCharacterWise(String textA, String textB) {
        Seq<Character> charactersA = characters(textA);
        Seq<Character> charactersB = characters(textB);
        return compare(charactersA, charactersB);
    }

    static <T> Seq<Difference> compare(Seq<T> sequenceA, Seq<T> sequenceB) {
        Seq<AlignedItemPair<T>> alignedItems = alignItems(sequenceA, sequenceB);
        final List<DifferenceBuilder> diffList = new ArrayList<>();
        @Nullable DifferenceBuilder actDifference = null;
        int firstLineIndex = 0;
        int secondLineIndex = 0;

        ChangeKind mode = ChangeKind.MODE_UNCHANGED;

        for (AlignedItemPair<T> alignedItemPair : alignedItems) {
            @Nullable T line1 = alignedItemPair.first();
            @Nullable T line2 = alignedItemPair.second();
            if (line1 != null) {
                // first line != null
                if (line2 != null) {
                    // second line != null

                    if (!alignedItemPair.hasEqualItems()) {
                        // Replaced Text
                        if (mode != ChangeKind.MODE_REPLACED) {
                            mode = ChangeKind.MODE_REPLACED;
                            actDifference = new DifferenceBuilder(
                                    firstLineIndex,
                                    secondLineIndex);
                            diffList.add(actDifference);
                        }
                    } else {
                        // Unchanged Text
                        // No item added to the result
                        if (mode != ChangeKind.MODE_UNCHANGED) {
                            mode = ChangeKind.MODE_UNCHANGED;
                            actDifference = null;
                        }
                    }
                } else {
                    // second line == null
                    // Deleted Text

                    if (mode != ChangeKind.MODE_DELETED) {
                        mode = ChangeKind.MODE_DELETED;
                        actDifference = new DifferenceBuilder(
                                firstLineIndex,
                                secondLineIndex);
                        diffList.add(actDifference);
                    }
                }
            } else {
                // Added Text
                if (mode != ChangeKind.MODE_ADDED) {
                    mode = ChangeKind.MODE_ADDED;
                    actDifference = new DifferenceBuilder(
                            firstLineIndex,
                            secondLineIndex);
                    diffList.add(actDifference);
                }
            }

            if (line1 != null) {
                firstLineIndex++;
            }
            if (line2 != null) {
                secondLineIndex++;
            }
            if (actDifference != null) {
                actDifference.getRangeInABuilder().withEnd(firstLineIndex);
                actDifference.getRangeInBBuilder().withEnd(secondLineIndex);
            }
        }

        return toDifferences(diffList);
    }

    private static <T> Seq<AlignedItemPair<T>> alignItems(
            Seq<T> linesA,
            Seq<T> linesB) {
        SequenceDiffDefault<T> algorithm = new SequenceDiffDefault<>();
        return algorithm.alignItems(linesA, linesB);
    }

    private static Seq<Difference> toDifferences(List<DifferenceBuilder> diffList) {
        List<Difference> result = new ArrayList<>();
        for (DifferenceBuilder i : diffList) {
            result.add(i.toDifference());
        }
        return newSeq(result);
    }

    static <T> String asUnifiedDiffChangeHunk(Seq<AlignedItemPair<T>> alignedItems) {
        StringBuilder sb = new StringBuilder();
        addLine(sb, "", "@@ -1 +1 @@");
        for (AlignedItemPair<T> alignedItemPair : alignedItems) {
            @Nullable String line1 = escapedOrNull(stringOrNull(alignedItemPair.first()));
            @Nullable String line2 = escapedOrNull(stringOrNull(alignedItemPair
                    .second()));

            if (line1 == null) {
                // line1 and line2 cannot be null at the same time in an alignedItemPair.
                // Therefore `requireNonNull(line2)` will never fail.
                addLine(sb, "+", requireNonNull(line2));
            } else if (line2 == null) {
                addLine(sb, "-", line1);
            } else if (alignedItemPair.hasEqualItems()) {
                addLine(sb, " ", line1);
            } else {
                addLine(sb, "-", line1);
                addLine(sb, "+", line2);
            }
        }
        return sb.toString();
    }

    private static void addLine(
            StringBuilder sb,
            String prefix,
            String line) {
        sb.append(prefix);
        sb.append(line);
        sb.append("\n");
    }


    private enum ChangeKind {
        MODE_UNCHANGED, MODE_ADDED, MODE_DELETED, MODE_REPLACED
    }

}
