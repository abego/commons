/*
 * MIT License
 *
 * Copyright (c) 2023 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.lineprocessing;


import org.abego.commons.lineprocessing.LineProcessing.ScriptBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.abego.commons.io.InputStreamUtil.newInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LineProcessingTest {

    @Test
    void smoketest() {
        ScriptBuilder<MyState> builder = LineProcessing.newScriptBuilder(MyState::new);
        StringBuilder out = new StringBuilder();
        builder.onMatch((c, s) -> true, (c, s) -> {
            out.append(String.format("line %d: %s\n", c.lineNumber(), c.line()));
            s.charCount += c.line().length();
            c.more();
        });
        builder.onMatch("foo", (c, s) ->
                out.append("- foo found\n"));
        builder.onMatch("bar", (c, s) ->
                out.append("- bar found\n"));
        builder.onMatch("(\\w)\\w\\w\\w", (c, s) ->
                out.append(String.format("- 4-letter word, first char: %s\n",
                        c.m().group(1))));
        builder.onDefault((c, s) ->
                out.append(String.format("- unknown: %s\n", c.m().group())));
        builder.onEndOfText((c, s) ->
                out.append(String.format("\ntotal chars: %d\n", s.charCount)));

        LineProcessing.Script script = builder.build();

        script.process("bar\nfoo\nbaz\nbooz\nfoo\n");

        assertEquals("line 1: bar\n" +
                "- bar found\n" +
                "line 2: foo\n" +
                "- foo found\n" +
                "line 3: baz\n" +
                "line 4: booz\n" +
                "- 4-letter word, first char: b\n" +
                "line 5: foo\n" +
                "- foo found\n" +
                "\n" +
                "total chars: 16\n", out.toString());
    }

    @Test
    void conditionPredicateAccess() {
        ScriptBuilder<MyState> builder = LineProcessing.newScriptBuilder(MyState::new);
        StringBuilder out = new StringBuilder();
        builder.onMatch((c, s) -> {
            out.append(String.format("line %d: %s - pattern? %s\n",
                    c.lineNumber(), c.line(), c.isPatternMatchingRule()));
            return false;
        }, (c, s) -> {});

        LineProcessing.Script script = builder.build();

        script.process("bar");

        assertEquals("line 1: bar - pattern? false\n", out.toString());
    }

    @Test
    void moreCalledInConditionPredicate() {
        ScriptBuilder<MyState> builder = LineProcessing.newScriptBuilder(MyState::new);
        builder.onMatch((c, s) -> {
            c.more();
            return false;
        }, (c, s) -> {});

        LineProcessing.Script script = builder.build();

        Exception e = assertThrows(UnsupportedOperationException.class,
                () -> script.process("bar"));
        assertEquals("Must not call `more()`", e.getMessage());
    }

    @Test
    void matcherCalledInConditionPredicate() {
        ScriptBuilder<MyState> builder = LineProcessing.newScriptBuilder(MyState::new);
        builder.onMatch((c, s) -> {
            c.m();
            return false;
        }, (c, s) -> {});

        LineProcessing.Script script = builder.build();

        Exception e = assertThrows(Exception.class,
                () -> script.process("bar"));
        assertEquals("No matcher defined", e.getMessage());
    }

    @Test
    void processInputStream() {
        ScriptBuilder<MyState> builder = LineProcessing.newScriptBuilder(MyState::new);
        StringBuilder out = new StringBuilder();
        builder.onMatch((c, s) -> {
            out.append(String.format("line %d: %s - pattern? %s\n",
                    c.lineNumber(), c.line(), c.isPatternMatchingRule()));
            return false;
        }, (c, s) -> {});

        LineProcessing.Script script = builder.build();

        InputStream inputStream = newInputStream("bar");

        script.process(inputStream);

        assertEquals("line 1: bar - pattern? false\n", out.toString());
    }

    static class MyState {
        int charCount = 0;
    }

}
