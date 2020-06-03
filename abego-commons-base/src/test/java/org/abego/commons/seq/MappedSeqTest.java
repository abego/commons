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

package org.abego.commons.seq;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.abego.commons.seq.SeqUtil.emptySeq;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MappedSeqTest {

    @Test
    void newMappedSeqOrEmpty() {

        // non empty
        Seq<Integer> seq = SeqUtil.newSeq(4, 7, 2);
        Function<Integer, String> mapper = i -> "{" + i + "}";
        Seq<String> result = MappedSeq.newMappedSeqOrEmpty(seq, mapper);

        assertEquals(3, result.size());
        assertEquals("{4}", result.item(0));
        assertEquals("{7}", result.item(1));
        assertEquals("{2}", result.item(2));

        // empty
        result = MappedSeq.newMappedSeqOrEmpty(emptySeq(), mapper);

        assertEquals(0, result.size());

        // null
        result = MappedSeq.newMappedSeqOrEmpty(null, mapper);

        assertEquals(0, result.size());
    }

}