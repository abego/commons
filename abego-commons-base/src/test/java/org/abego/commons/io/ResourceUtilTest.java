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

import java.nio.charset.StandardCharsets;

import static org.abego.commons.TestData.EMPTY_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.MISSING_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_ISO_8859_1_TXT_TEXT;
import static org.abego.commons.TestData.SAMPLE_TXT_RESOURCE_FULL_NAME;
import static org.abego.commons.TestData.SAMPLE_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_TXT_TEXT;
import static org.abego.commons.io.ResourceUtil.hasResource;
import static org.abego.commons.io.ResourceUtil.textOfResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, ResourceUtil::new);
    }

    @Test
    void textOfResource_ok() {

        String text = textOfResource(TestData.class, SAMPLE_TXT_RESOURCE_NAME);

        assertEquals(SAMPLE_TXT_TEXT, text);

    }

    @Test
    void textOfResource_emptyResource() {

        String text = textOfResource(TestData.class, EMPTY_TXT_RESOURCE_NAME);

        assertEquals("", text);
    }

    @Test
    void textOfResource_resourceMissing() {

        Exception e = assertThrows(Exception.class,
                () -> textOfResource(getClass(), MISSING_RESOURCE_NAME));

        assertEquals(
                "Error when accessing resource `missing.txt` of class `org.abego.commons.io.ResourceUtilTest`.",  // NON-NLS
                e.getMessage());
    }

    @Test
    void textOfResource_Charset_ok() {
        String text = textOfResource(
                TestData.class, SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME, StandardCharsets.ISO_8859_1);

        assertEquals(SAMPLE_ISO_8859_1_TXT_TEXT, text);
    }

    @Test
    void hasResource_ok() {
        assertTrue(hasResource(SAMPLE_TXT_RESOURCE_FULL_NAME));
        assertFalse(hasResource("missingResource"));
    }


}