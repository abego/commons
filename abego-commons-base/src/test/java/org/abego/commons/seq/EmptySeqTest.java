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

package org.abego.commons.seq;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.abego.commons.seq.SeqFactories.emptySeq;
import static org.abego.commons.seq.SeqUtil.newSeq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmptySeqTest {

    @Test
    void equalsOk() {

        Seq<String> e = emptySeq();

        Seq<String> e2 = emptySeq();
        Seq<String> s = newSeq("a", "b", "c");

        assertEquals(e, e2);
        assertNotEquals(e, s);
    }

    @Test
    void sizeOk() {
        Seq<String> e = emptySeq();

        assertEquals(0, e.size());
    }

    @Test
    void isEmptyOk() {
        Seq<String> e = emptySeq();

        assertTrue(e.isEmpty());
    }

    @Test
    void itemOk() {
        Seq<String> e = emptySeq();

        assertThrows(NoSuchElementException.class, () -> e.item(0));
    }

    @Test
    void iteratorOk() {
        Seq<String> e = emptySeq();

        assertFalse(e.iterator().hasNext());
    }

    @Test
    void streamOk() {
        Seq<String> e = emptySeq();

        assertEquals(0, e.stream().count());
    }


}
