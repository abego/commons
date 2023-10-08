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

package org.abego.commons.test;

import org.abego.commons.io.FileUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.range.IntRange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opentest4j.AssertionFailedError;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import static org.abego.commons.range.IntRangeDefault.newIntRange;
import static org.abego.commons.test.JUnit5Util.assertEqualLines;
import static org.abego.commons.test.JUnit5Util.assertIntRangeEquals;
import static org.abego.commons.test.JUnit5Util.assertTextContains;
import static org.abego.commons.test.JUnit5Util.assertTextOfFileEquals;
import static org.abego.commons.test.JUnit5Util.assertThrowsWithMessage;
import static org.abego.commons.test.JUnit5Util.castOrFail;
import static org.abego.commons.test.TestData.SAMPLE_ISO_8859_1_TXT_TEXT;
import static org.abego.commons.test.TestData.SAMPLE_TEXT_2;
import static org.abego.commons.test.TestData.SAMPLE_TXT_RESOURCE_NAME;
import static org.abego.commons.test.TestData.SAMPLE_TXT_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JUnit5UtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, JUnit5Util::new);
    }

    @Test
    void assertTextContains_ok() {
        assertTextContains("", "abc", "desc");
        assertTextContains("a", "abc", "desc");
        assertTextContains("b", "abc", "desc");
        assertTextContains("c", "abc", "desc");
        assertTextContains("abc", "abc", "desc");

        AssertionError e = assertThrows(
                AssertionError.class,
                () -> assertTextContains("d", "abc", "desc")
        );
        assertEquals("desc does not contain \"d\".\nGot:\n\"abc\"",
                e.getMessage());
    }

    @Test
    void assertTextOfFileEquals_ok() {
        File file = FileUtil.tempFileForRunFromResource(
                TestData.class, SAMPLE_TXT_RESOURCE_NAME);

        assertTextOfFileEquals(SAMPLE_TXT_TEXT, file);

        AssertionError e = assertThrows(
                AssertionError.class, () -> assertTextOfFileEquals(SAMPLE_TEXT_2, file));
        String expected = MessageFormat
                .format("Wrong text in file {0} ==> expected: <{1}> but was: <{2}>",
                        file.getAbsolutePath(), SAMPLE_TEXT_2, SAMPLE_TXT_TEXT);
        assertEquals(expected, e.getMessage());
    }

    @Test
    void assertTextOfFileEquals_withCharset() {
        File file = FileUtil.tempFileForRunFromResource(
                TestData.class, TestData.SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME);

        assertTextOfFileEquals(TestData.SAMPLE_ISO_8859_1_TXT_TEXT, file, StandardCharsets.ISO_8859_1);

        AssertionError e = assertThrows(
                AssertionError.class, () -> assertTextOfFileEquals(
                        SAMPLE_TEXT_2, file, StandardCharsets.ISO_8859_1));
        String expected = MessageFormat
                .format("Wrong text in file {0} ==> expected: <{1}> but was: <{2}>",
                        file.getAbsolutePath(), SAMPLE_TEXT_2, SAMPLE_ISO_8859_1_TXT_TEXT);
        assertEquals(expected, e.getMessage());
    }

    @Test
    public void assertThrowsWithMessageOK() {
        // Throws
        assertThrowsWithMessage(
                IllegalArgumentException.class, "foo", () -> {
                    throw new IllegalArgumentException("foo");
                });

        // Throws, but wrong message
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> assertThrowsWithMessage(
                IllegalArgumentException.class, "foo", () -> {
                    throw new IllegalArgumentException("bar");
                }));
        assertEquals("Expected exception message: 'foo', got: 'bar' ==> expected: <foo> but was: <bar>", e.getMessage());

        // Throws, but wrong type
        e = assertThrows(AssertionFailedError.class, () -> assertThrowsWithMessage(
                IllegalArgumentException.class, "foo", () -> {
                    throw new IllegalStateException("foo");
                }));
        assertEquals("Unexpected exception type thrown, expected: <java.lang.IllegalArgumentException> but was: <java.lang.IllegalStateException>", e.getMessage());

        // Does not throw
        e = assertThrows(AssertionFailedError.class, () -> assertThrowsWithMessage(
                IllegalArgumentException.class, "foo", () -> {
                }));
        assertEquals("Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.", e.getMessage());
    }

    @Test
    public void assertIntRangeEqualsOK() {
        IntRange range1 = newIntRange(2, 5);

        // OK
        assertIntRangeEquals(2, 5, range1);

        // wrong start
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> assertIntRangeEquals(3, 5, range1));
        assertEquals("expected: <3..5> but was: <2..5>", e.getMessage());

        // wrong end
        e = assertThrows(AssertionFailedError.class, () -> assertIntRangeEquals(2, 7, range1));
        assertEquals("expected: <2..7> but was: <2..5>", e.getMessage());

        // wrong start and end
        e = assertThrows(AssertionFailedError.class, () -> assertIntRangeEquals(3, 7, range1));
        assertEquals("expected: <3..7> but was: <2..5>", e.getMessage());
    }

    @Test
    public void castOrFailOK() {
        Object o1 = "foo";

        // OK case
        String s = castOrFail(String.class, o1);
        assertEquals("foo", s);

        // Fail case
        AssertionFailedError e = assertThrows(AssertionFailedError.class, () -> castOrFail(Double.class, o1));
        assertEquals("expected java.lang.Double, got java.lang.String", e.getMessage());
    }

    @Test
    void equalLines() {
        assertEqualLines("foo\r\nbar\r\n", "foo\nbar\n");
        assertEqualLines("foo\nbar\n", "foo\r\nbar\r\n");
    }

    @Test
    void assertEqualFiles(@TempDir File tempDir) {
        File dir1 = FileUtil.mkdirs(tempDir, "dir1");
        File dir2 = FileUtil.mkdirs(tempDir, "dir2");
        File file1 = new File(tempDir, "dir1/file");
        File file2 = new File(tempDir, "dir2/file");

        FileUtil.writeText(file1, "foo");
        FileUtil.writeText(file2, "foo");

        JUnit5Util.assertEqualFiles(dir1, dir2);
    }

}
