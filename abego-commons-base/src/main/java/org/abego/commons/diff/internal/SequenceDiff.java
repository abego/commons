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

interface SequenceDiff<T> {

    /**
     * Returns the differences between the items in sequenceA and sequenceB.
     * <p>
     * The differences are returned as a sequence of "{@link AlignedItemPair}s.
     * For every pair of the result sequence the following holds:
     * <p>
     * <table border="1">
     * <tr>
     * <td>first</td>
     * <td>second</td>
     * <td>Comment</td>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>X</td>
     * <td>The sequenceB has an extra item X at the given position. <br>
     * I.e. X was "added".</td>
     * </tr>
     * <tr>
     * <td>X</td>
     * <td>null</td>
     * <td>The sequenceA has an extra item X at the given position. <br>
     * I.e. X was "deleted".</td>
     * </tr>
     * <tr>
     * <td>X</td>
     * <td>Y</td>
     * <td>Depending on whether X and Y are 'equal' the items of sequenceA and
     * sequenceB at the given position are either unchanged
     * ('match') or differ. <br>
     * If X and Y are not 'equal' X has been replaces by Y (see {@link org.abego.commons.diff.internal.AlignedItemPair#hasEqualItems()}.<br>
     * </td>
     * </tr>
     * </table>
     */
    Seq<AlignedItemPair<T>> alignItems(
            Seq<T> sequenceA,
            Seq<T> sequenceB);
}