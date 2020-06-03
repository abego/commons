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

import java.util.ArrayList;
import java.util.List;

import static org.abego.commons.seq.SeqNonEmpty.seqNonEmpty;
import static org.abego.commons.seq.SeqNonEmpty.seqNonEmptyOrElse;
import static org.abego.commons.seq.SeqNonEmpty.seqNonEmptyOrElseGet;
import static org.abego.commons.seq.SeqNonEmpty.seqNonEmptyOrElseThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeqNonEmptyTest {
    Seq<String> noItemSeq() {
        return Seq.newSeq();
    }

    Seq<String> singleItemSeq() {
        return Seq.newSeq("a");
    }

    Seq<String> helloSeq() {
        return Seq.newSeq("h", "e", "l", "l", "o");
    }


    @Test
    void withArrayOk() {
        SeqNonEmpty<String> s = SeqNonEmpty.with("h", "e", "l", "l", "o");

        assertEquals(helloSeq(), s);
    }

    @Test
    void withListOk() {
        List<String> list = new ArrayList<>();
        list.add("h");
        list.add("e");
        list.add("l");
        list.add("l");
        list.add("o");

        SeqNonEmpty<String> s = SeqNonEmpty.with(list);

        assertEquals(helloSeq(), s);
    }


    @Test
    void seqNonEmptyOk() {

        Seq<String> s = singleItemSeq();
        SeqNonEmpty<String> sn = seqNonEmpty(s);
        assertEquals(s, sn);

        Seq<String> h = helloSeq();
        SeqNonEmpty<String> hn = seqNonEmpty(h);
        assertEquals(h, hn);

        assertThrows(IllegalArgumentException.class, () -> seqNonEmpty(noItemSeq()));
    }

    @Test
    void seqNonEmptyOrElseOk() {
        SeqNonEmpty<String> alt = seqNonEmpty(Seq.newSeq("x"));

        Seq<String> s = singleItemSeq();
        SeqNonEmpty<String> sn = seqNonEmptyOrElse(s, alt);
        assertEquals(s, sn);

        Seq<String> h = helloSeq();
        SeqNonEmpty<String> hn = seqNonEmptyOrElse(h, alt);
        assertEquals(h, hn);

        SeqNonEmpty<String> nn = seqNonEmptyOrElse(noItemSeq(), alt);
        assertEquals(alt, nn);
    }

    @Test
    void seqNonEmptyOrElseGetOk() {
        SeqNonEmpty<String> alt = seqNonEmpty(Seq.newSeq("x"));

        Seq<String> s = singleItemSeq();
        SeqNonEmpty<String> sn = seqNonEmptyOrElseGet(s, () -> alt);
        assertEquals(s, sn);

        Seq<String> h = helloSeq();
        SeqNonEmpty<String> hn = seqNonEmptyOrElseGet(h, () -> alt);
        assertEquals(h, hn);

        SeqNonEmpty<String> nn = seqNonEmptyOrElseGet(noItemSeq(), () -> alt);
        assertEquals(alt, nn);
    }

    @Test
    void seqNonEmptyOrElseThrowOk() {
        Seq<String> s = singleItemSeq();
        SeqNonEmpty<String> sn = seqNonEmptyOrElseThrow(s, IllegalArgumentException::new);
        assertEquals(s, sn);

        Seq<String> h = helloSeq();
        SeqNonEmpty<String> hn = seqNonEmptyOrElseThrow(h, IllegalArgumentException::new);
        assertEquals(h, hn);

        assertThrows(IllegalArgumentException.class, () -> seqNonEmptyOrElseThrow(noItemSeq(), IllegalArgumentException::new));
    }

    @Test
    void seqToSeqNonEmptyOk() {
        ABCSeq s = new ABCSeq();

        SeqNonEmpty<String> sne = seqNonEmpty(s);

        // to silence warning: 'assertEquals()' between objects of inconvertible types 'ABCSeq' and 'SeqNonEmpty<String>'
        //noinspection AssertEqualsBetweenInconvertibleTypes
        assertEquals(s, sne);
    }
}