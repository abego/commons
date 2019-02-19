/*
 * MIT License
 *
 * Copyright (c) 2018 Udo Borkowski, (ub@abego.org)
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

import static org.abego.commons.seq.SeqForList.LIST_MUST_NOT_BE_EMPTY_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeqForListTest extends AbstractSeqTest {


    @Override
    Seq<String> singleItemSeq() {
        List<String> list = new ArrayList<>();
        list.add("a");
        return Seq.newSeq(list);
    }

    @Override
    Seq<String> helloSeq() {
        List<String> list = new ArrayList<>();
        list.add("h");
        list.add("e");
        list.add("l");
        list.add("l");
        list.add("o");
        return Seq.newSeq(list);
    }

    @Test
    void noItemsFails() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> SeqForList.seqForList(new ArrayList<String>()));
        assertEquals(LIST_MUST_NOT_BE_EMPTY_MESSAGE, e.getMessage());
    }
}
