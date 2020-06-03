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

package org.abego.commons.util.stream;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.abego.commons.lang.IterableUtil.textOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StreamUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, StreamUtil::new);
    }


    @Test
    void toStream_OK() {
        Stream<String> stream = StreamUtil.toStream("a", "b", "c");

        assertEquals("abc", textOf(StreamUtil.toList(stream)));
    }

    @Test
    void toList_OK() {
        Stream<String> stream = StreamUtil.toStream("a", "b", "c");

        List<String> list = StreamUtil.toList(stream);

        assertEquals("a-b-c", textOf(list, "-"));
    }

    @Test
    void join_OK() {
        Stream<String> stream = StreamUtil.toStream("a", "b", "c");

        String s = StreamUtil.join(stream, ":");

        assertEquals("a:b:c", s);
    }

    @Test
    void join_withDefaultDelimiter() {
        Stream<String> stream = StreamUtil.toStream("a", "b", "c");

        String s = StreamUtil.join(stream);

        assertEquals("a,b,c", s);
    }
}