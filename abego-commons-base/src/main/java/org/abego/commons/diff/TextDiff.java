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

import org.abego.commons.diff.internal.DiffImpl;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.seq.Seq;
import org.abego.commons.seq.SeqUtil;

public final class TextDiff {

    private static final Seq<Difference> NO_DIFFERENCES = SeqUtil.emptySeq();

    TextDiff() {
        throw new MustNotInstantiateException();
    }

    public static Seq<Difference> compareLineWise(String textA, String textB) {
        return DiffImpl.compareLineWise(textA, textB);
    }

    public static Seq<Difference> compareCharacterWise(String textA, String textB) {
        return DiffImpl.compareCharacterWise(textA, textB);
    }

    public static Seq<Difference> getNoDifferences() {
        return NO_DIFFERENCES;
    }
}
