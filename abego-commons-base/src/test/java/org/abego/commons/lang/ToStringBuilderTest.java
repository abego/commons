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

package org.abego.commons.lang;

import org.junit.jupiter.api.Test;

import static org.abego.commons.lang.ToStringBuilder.ALREADY_CLOSED_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ToStringBuilderTest {

    @Test
    void smokeTest() {

        ToStringBuilder builder = ToStringBuilder.newToStringBuilder("SampleClass");

        String s = builder
                .field("foo", 123)
                .fieldIf(false, "bar", "bar-value")
                .fieldIf(true, "baz", "baz-value")
                .fieldIf(false, "pi", 3.1415)
                .fieldIf(true, "e", 2.7182)
                .fieldOptional("qux", "qux-value")
                .fieldOptional("quxx", null)
                .field("number", 5.67)
                .field("class", Long.class)
                .field("msg", "Some text")
                .build();

        assertEquals("SampleClass{foo=123, baz='baz-value', e=2.7182, qux='qux-value', number=5.67, class=class java.lang.Long, msg='Some text'}", s);

        assertEquals("SampleClass{foo=123, baz='baz-value', e=2.7182, qux='qux-value', number=5.67, class=class java.lang.Long, msg='Some text'}", builder.toString());

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> builder.field("lateField", "val"));
        assertEquals(ALREADY_CLOSED_MESSAGE, e.getMessage());
    }

}