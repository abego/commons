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

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.abego.commons.seq.SeqNonEmptyUtil.newSeqNonEmpty;
import static org.abego.commons.seq.SeqNonEmptyUtil.seqNonEmpty;
import static org.abego.commons.seq.SeqNonEmptyUtil.seqNonEmptyOrElse;
import static org.abego.commons.seq.SeqNonEmptyUtil.seqNonEmptyOrElseGet;
import static org.abego.commons.seq.SeqNonEmptyUtil.seqNonEmptyOrElseThrow;
import static org.abego.commons.seq.SeqUtil.newSeq;
import static org.abego.commons.util.ListUtil.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeqNonEmptyUtilTest {
    Seq<String> noItemSeq() {
        return newSeq();
    }

    Seq<String> singleItemSeq() {
        return newSeq("a");
    }

    Seq<String> helloSeq() {
        return newSeq("h", "e", "l", "l", "o");
    }

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, SeqNonEmptyUtil::new);
    }


    @Test
    void newSeqNonEmptyArrayOk() {
        SeqNonEmpty<String> s = newSeqNonEmpty("h", "e", "l", "l", "o");

        assertEquals(helloSeq(), s);
    }

    @Test
    void newSeqNonEmptyListOk() {
        List<String> list = new ArrayList<>();
        list.add("h");
        list.add("e");
        list.add("l");
        list.add("l");
        list.add("o");

        SeqNonEmpty<String> s = newSeqNonEmpty(list);

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
        SeqNonEmpty<String> alt = seqNonEmpty(newSeq("x"));

        Seq<String> s = singleItemSeq();
        SeqNonEmpty<String> sn = seqNonEmptyOrElse(s, alt);
        assertEquals(s, sn);

        Seq<String> h = helloSeq();
        SeqNonEmpty<String> hn = seqNonEmptyOrElse(h, alt);
        assertEquals(h, hn);

        SeqNonEmpty<String> nn = seqNonEmptyOrElse(noItemSeq(), alt);
        assertEquals(alt, nn);

        // a non-empty Seq that does not implement "SeqNonEmpty"
        Seq<String> ne = new SampleNonEmptySeqNotImplementingSeqNonEmpty();
        SeqNonEmpty<String> nne = seqNonEmptyOrElse(ne, alt);
        assertEquals(ne, nne);

    }

    @Test
    void seqNonEmptyOrElseGetOk() {
        SeqNonEmpty<String> alt = seqNonEmpty(newSeq("x"));

        Seq<String> s = singleItemSeq();
        SeqNonEmpty<String> sn = seqNonEmptyOrElseGet(s, () -> alt);
        assertEquals(s, sn);

        Seq<String> h = helloSeq();
        SeqNonEmpty<String> hn = seqNonEmptyOrElseGet(h, () -> alt);
        assertEquals(h, hn);

        SeqNonEmpty<String> nn = seqNonEmptyOrElseGet(noItemSeq(), () -> alt);
        assertEquals(alt, nn);

        // a non-empty Seq that does not implement "SeqNonEmpty"
        Seq<String> ne = new SampleNonEmptySeqNotImplementingSeqNonEmpty();
        SeqNonEmpty<String> nne = seqNonEmptyOrElseGet(ne, () -> alt);
        assertEquals(ne, nne);

    }

    @Test
    void seqNonEmptyOrElseThrow_nonThrowing() {
        Seq<String> s = singleItemSeq();
        SeqNonEmpty<String> sn = seqNonEmptyOrElseThrow(s, IllegalArgumentException::new);
        assertEquals(s, sn);

        Seq<String> h = helloSeq();
        SeqNonEmpty<String> hn = seqNonEmptyOrElseThrow(h, IllegalArgumentException::new);
        assertEquals(h, hn);

        // a non-empty Seq that does not implement "SeqNonEmpty"
        Seq<String> ne = new SampleNonEmptySeqNotImplementingSeqNonEmpty();
        SeqNonEmpty<String> nne = seqNonEmptyOrElseThrow(ne, IllegalArgumentException::new);
        assertEquals(ne, nne);

    }

    @Test
    void seqNonEmptyOrElseThrow_throwing() {

        assertThrows(IllegalArgumentException.class,
                () -> seqNonEmptyOrElseThrow(noItemSeq(), IllegalArgumentException::new));
    }

    @Test
    void seqToSeqNonEmptyOk() {
        Seq<String> s = helloSeq();

        SeqNonEmpty<String> sne = seqNonEmpty(s);

        assertEquals(s, sne);
    }

    private static class SampleNonEmptySeqNotImplementingSeqNonEmpty extends AbstractSeq<String> {
        private final String item = "foo";

        SampleNonEmptySeqNotImplementingSeqNonEmpty() {
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String item(int index) {
            return item;
        }

        @NonNull
        @Override
        public Iterator<String> iterator() {
            return toList(item).iterator();
        }
    }
}