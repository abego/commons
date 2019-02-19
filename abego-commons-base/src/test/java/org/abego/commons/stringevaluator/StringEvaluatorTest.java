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

package org.abego.commons.stringevaluator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.abego.commons.stringevaluator.StringEvaluator.stringEvaluator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringEvaluatorTest {

    @Test
    void constants() {
        assertEquals('\\', StringEvaluator.ESCAPE_CHARACTER);
        assertEquals('{', StringEvaluator.LEFT_TERM_DELIMITER_DEFAULT);
        assertEquals('}', StringEvaluator.RIGHT_TERM_DELIMITER_DEFAULT);
    }

    @Test
    void addTermValueAndValueOf() {
        StringEvaluator se = stringEvaluator();

        se.setTermValue("name1", "One");

        assertEquals("One", se.valueOf("{name1}"));
    }

    @Test
    void addTermValueAndValueOf_nested() {
        StringEvaluator se = stringEvaluator();

        se.setTermValue("name1", "One");
        se.setTermValue("outer", "out-{name1}-out");

        assertEquals("out-One-out", se.valueOf("{outer}"));
    }


    @Test
    void addTermValueAndValueOf_nested2() {
        StringEvaluator se = stringEvaluator();

        se.setTermValue("name1", "One");
        se.setTermValue("outer", "out-{inner}-out");
        se.setTermValue("inner", "in-{name1}-in");

        assertEquals("out-in-One-in-out", se.valueOf("{outer}"));
    }

    @Test
    void addTermValueAndValueOf_endlessRecursion() {
        StringEvaluator se = stringEvaluator();

        se.setTermValue("outer", "out-{inner}-out");
        se.setTermValue("inner", "in-{outer}-in");

        assertThrows(StackOverflowError.class, () -> se.valueOf("{outer}"));
    }

    @Test
    void valueOf_edgeCases() {
        StringEvaluator se = stringEvaluator();

        se.setTermValue("name1", "One");

        assertEquals(">>One<<", se.valueOf(">>{name1}<<"));
        assertEquals(">>One", se.valueOf(">>{name1}"));
        assertEquals("One<<", se.valueOf("{name1}<<"));
        assertEquals("One", se.valueOf("{name1}"));
        assertEquals("OneOne", se.valueOf("{name1}{name1}"));
        assertEquals("One-One", se.valueOf("{name1}-{name1}"));
        assertEquals("---", se.valueOf("---"));
    }

    @Test
    void escapedChar() {
        StringEvaluator se = stringEvaluator();

        assertEquals(">>{<<", se.valueOf(">>\\{<<"));
    }

    @Test
    void escapedChar_andReference() {
        StringEvaluator se = stringEvaluator();

        se.setTermValue("name1", "One");

        assertEquals(">>One{name1}<<", se.valueOf(
                ">>{name1}\\{name1\\}<<"));
    }

    @Test
    void undefinedTerm() {
        StringEvaluator se = stringEvaluator();

        assertEquals(">>{name1}<<", se.valueOf(">>{name1}<<"));
    }

    @Test
    void addTermEvaluator() {
        StringEvaluator se = stringEvaluator();

        se.addTermEvaluator(s -> s.equals("name1") ? "foo" : null);

        se.setTermValue("name1", "One");
        se.setTermValue("outer", "out-{name1}-out");

        assertEquals("out-foo-out-foo", se.valueOf("{outer}-{name1}"));
    }

    @Test
    void addTermEvaluatorTwice() {
        StringEvaluator se = stringEvaluator();

        se.addTermEvaluator(s -> s.equals("name1") ? "foo" :
                s.equals("inner") ? ">-{name1}-<" : null);
        se.addTermEvaluator(s -> s.equals("name1") ? "bar" : null);

        se.setTermValue("name1", "One");
        se.setTermValue("outer", "out-{inner}-out");
        se.setTermValue("inner", "in-{name1}-in");

        assertEquals("out->-bar-<-out", se.valueOf("{outer}"));
    }

    @Test
    void addTermEvaluatorTwice_OrderMatters() {
        StringEvaluator se = stringEvaluator();

        se.addTermEvaluator(s -> s.equals("name1") ? "bar" : null);
        se.addTermEvaluator(s -> s.equals("name1") ? "foo" :
                s.equals("inner") ? ">-{name1}-<" : null);

        se.setTermValue("name1", "One");
        se.setTermValue("outer", "out-{inner}-out");
        se.setTermValue("inner", "in-{name1}-in");

        assertEquals("out->-foo-<-out", se.valueOf("{outer}"));
    }

    @Test
    void delimiters() {
        StringEvaluator se = stringEvaluator('(', ')');

        se.setTermValue("name1", "One");
        se.setTermValue("outer", "out-(name1)-out");

        assertEquals("out-One-out", se.valueOf("(outer)"));
        assertEquals("{outer}", se.valueOf("{outer}"));
    }

    @Test
    void delimiters_SameTwice() {
        StringEvaluator se = stringEvaluator('$', '$');

        se.setTermValue("name1", "One");
        se.setTermValue("outer", "out-$name1$-out");

        assertEquals("out-One-out", se.valueOf("$outer$"));
        assertEquals("{outer}", se.valueOf("{outer}"));
    }

    @Test
    void delimiters_Escape_fails() {
        Exception e = Assertions
                .assertThrows(StringEvaluator.StringEvaluatorException.class,
                        () -> stringEvaluator('\\', '$'));

        assertEquals("'\\' must not be used as a term delimiter",
                e.getMessage());
    }
}
