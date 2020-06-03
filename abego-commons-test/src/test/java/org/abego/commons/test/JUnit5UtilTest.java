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

package org.abego.commons.test;

import org.abego.commons.io.FileUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import static org.abego.commons.test.JUnit5Util.assertTextContains;
import static org.abego.commons.test.JUnit5Util.assertTextOfFileEquals;
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
    void assertTextOfFileEquals_ok() throws IOException {
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
    void assertTextOfFileEquals_withCharset() throws IOException {
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
}