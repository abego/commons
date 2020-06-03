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

package org.abego.commons.io;

import org.abego.commons.TestData;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.abego.commons.TestData.EMPTY_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.EMPTY_TXT_TEXT;
import static org.abego.commons.TestData.SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_ISO_8859_1_TXT_TEXT;
import static org.abego.commons.TestData.SAMPLE_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_TXT_TEXT;
import static org.abego.commons.io.InputStreamUtil.textOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputStreamUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, InputStreamUtil::new);
    }

    @Test
    void textOf_ok() {
        InputStream inputStream = TestData.class
                .getResourceAsStream(SAMPLE_TXT_RESOURCE_NAME);

        assertEquals(SAMPLE_TXT_TEXT, textOf(inputStream));
    }

    @Test
    void textOf_withEmptyStream() {
        InputStream inputStream = TestData.class
                .getResourceAsStream(EMPTY_TXT_RESOURCE_NAME);

        assertEquals(EMPTY_TXT_TEXT, textOf(inputStream));
    }

    @Test
    void textOf_withCharset() {
        InputStream inputStream = TestData.class
                .getResourceAsStream(SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME);

        assertEquals(SAMPLE_ISO_8859_1_TXT_TEXT,
                textOf(inputStream, StandardCharsets.ISO_8859_1));
    }

    @Test
    void textOf_withCharsetName() {
        InputStream inputStream = TestData.class
                .getResourceAsStream(SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME);

        assertEquals(SAMPLE_ISO_8859_1_TXT_TEXT,
                textOf(inputStream, StandardCharsets.ISO_8859_1.name()));
    }
}