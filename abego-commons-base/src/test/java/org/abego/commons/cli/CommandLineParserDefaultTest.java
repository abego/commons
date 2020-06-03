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

package org.abego.commons.cli;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.abego.commons.cli.CommandLineParserDefault.newCommandLineParserDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CommandLineParserDefaultTest {

    @Test
    void newCommandLineDefault() {
        CommandLineParserDefault cmd = newCommandLineParserDefault();

        Assertions.assertNotNull(cmd);
    }

    @Test
    void hasNextItem_emptyArgs() {
        CommandLineParserDefault cmd = newCommandLineParserDefault();
        Assertions.assertFalse(cmd.hasNextItem());
    }

    @Test
    void hasNextItem_withArgs() {
        CommandLineParser cmd = newCommandLineParserDefault("foo", "bar");

        Assertions.assertTrue(cmd.hasNextItem());

        cmd.nextArgumentOrNull();

        Assertions.assertTrue(cmd.hasNextItem());
        cmd.nextArgumentOrNull();

        Assertions.assertFalse(cmd.hasNextItem());
    }

    @Test
    void nextOptionWithArgumentOrNull_Null() {
        CommandLineParser cmd = newCommandLineParserDefault("-foo", "bar");

        @Nullable String arg = cmd.nextOptionWithArgumentOrNull("-bar");

        Assertions.assertNull(arg);
    }

    @Test
    void nextOptionWithArgumentOrNull_Arg() {
        CommandLineParser cmd = newCommandLineParserDefault("-foo", "bar");

        @Nullable String arg = cmd.nextOptionWithArgumentOrNull("-foo");

        assertEquals("bar", arg);
    }

    @Test
    void nextOptionWithArgumentOrNull_MissingArg() {
        CommandLineParser cmd = newCommandLineParserDefault("-foo");

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> cmd.nextOptionWithArgumentOrNull("-foo"));

        assertEquals("Missing argument for option -foo", e.getMessage());
    }

    @Test
    void nextOptionOrNull_Null() {
        CommandLineParser cmd = newCommandLineParserDefault("-foo", "bar");

        @Nullable String arg = cmd.nextOptionOrNull("-bar");

        Assertions.assertNull(arg);
    }

    @Test
    void nextOptionOrNull_Option() {
        CommandLineParser cmd = newCommandLineParserDefault("-foo", "bar");

        @Nullable String arg = cmd.nextOptionOrNull("-foo");

        assertEquals("-foo", arg);
    }

    @Test
    void nextOptionOrNull_MultipleOptions() {
        CommandLineParser cmd = newCommandLineParserDefault("-foo", "bar");

        @Nullable String arg = cmd.nextOptionOrNull("-bar", "-foo");

        assertEquals("-foo", arg);
    }

    @Test
    void nextArgument() {
        CommandLineParser cmd = newCommandLineParserDefault("foo", "bar");

        @Nullable String arg = cmd.nextArgumentOrNull();

        assertEquals("foo", arg);

        arg = cmd.nextArgumentOrNull();

        assertEquals("bar", arg);

        arg = cmd.nextArgumentOrNull();

        Assertions.assertNull(arg);
    }

    @Test
    void nextArgumentOrDefault() {
        CommandLineParser cmd = newCommandLineParserDefault("foo");

        String arg = cmd.nextArgumentOrDefault("bar");

        assertEquals("foo", arg);

        arg = cmd.nextArgumentOrDefault("bar");

        assertEquals("bar", arg);

        arg = cmd.nextArgumentOrDefault("bar");

        assertEquals("bar", arg);
    }

    @Test
    void nextArgumentOrNull() {
        CommandLineParser cmd = newCommandLineParserDefault("foo");

        @Nullable String arg = cmd.nextArgumentOrNull();

        assertEquals("foo", arg);

        arg = cmd.nextArgumentOrNull();

        assertNull(arg);

        arg = cmd.nextArgumentOrNull();

        assertNull(arg);
    }

    @Test
    void nextArgumentOrFail() {
        CommandLineParser cmd = newCommandLineParserDefault("foo");

        String arg = cmd.nextArgumentOrFail("missing arg1");

        assertEquals("foo", arg);

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> cmd.nextArgumentOrFail("missing arg2"));

        assertEquals("missing arg2", e.getMessage());
    }

    @Test
    void isArgumentNext_true() {
        CommandLineParser cmd = newCommandLineParserDefault("foo");

        Assertions.assertTrue(cmd.isArgumentNext());
    }

    @Test
    void isArgumentNext_false() {
        CommandLineParser cmd = newCommandLineParserDefault("-foo");

        Assertions.assertFalse(cmd.isArgumentNext());
    }

    @Test
    void isArgumentNext_false_empty() {
        CommandLineParser cmd = newCommandLineParserDefault();

        Assertions.assertFalse(cmd.isArgumentNext());
    }

    @Test
    void isOptionNext_true() {
        CommandLineParser cmd = newCommandLineParserDefault("foo");

        Assertions.assertFalse(cmd.isOptionNext("-foo"));
    }

    @Test
    void isOptionNext_false() {
        CommandLineParser cmd = newCommandLineParserDefault("-foo");

        Assertions.assertTrue(cmd.isOptionNext("-foo"));
    }

    @Test
    void isOptionNext_false_empty() {
        CommandLineParser cmd = newCommandLineParserDefault();

        Assertions.assertFalse(cmd.isOptionNext());
    }

    @Test
    void toStringOK() {
        CommandLineParser cmd = newCommandLineParserDefault("-foo", "bar");

        assertEquals("ArgumentsDefault{args=[-foo, bar], i=0}", cmd.toString());

        cmd.nextOptionOrNull("-foo");

        assertEquals("ArgumentsDefault{args=[-foo, bar], i=1}", cmd.toString());
    }
}