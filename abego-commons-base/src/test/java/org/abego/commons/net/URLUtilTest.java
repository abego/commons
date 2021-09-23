/*
 * MIT License
 *
 * Copyright (c) 2021 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.net;

import org.abego.commons.TestData;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.abego.commons.TestData.EMPTY_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.EMPTY_TXT_TEXT;
import static org.abego.commons.TestData.SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_ISO_8859_1_TXT_TEXT;
import static org.abego.commons.TestData.SAMPLE_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_TXT_TEXT;
import static org.abego.commons.net.URLUtil.isURI;
import static org.abego.commons.net.URLUtil.isURL;
import static org.abego.commons.net.URLUtil.textOf;
import static org.abego.commons.net.URLUtil.toFile;
import static org.abego.commons.net.URLUtil.urlDecode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class URLUtilTest {

    private static URL getTestDataResource(String resourceName) {
        @Nullable URL url = TestData.class.getResource(resourceName);
        if (url == null) {
            throw new IllegalStateException("Missing TestData resource");
        }
        return url;
    }

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, URLUtil::new);
    }

    @Test
    void toURL_Valid() {
        URL url = URLUtil.toURL("file:/foo/bar");

        assertEquals("file:/foo/bar", url.toString());
    }

    @Test
    void toURL_Invalid() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> URLUtil.toURL("Unknown:/foo/bar"));

        assertEquals("Invalid URL specification: Unknown:/foo/bar", e.getMessage());
    }

    @Test
    void textOf_ok() {
        URL url = getTestDataResource(SAMPLE_TXT_RESOURCE_NAME);

        assertEquals(SAMPLE_TXT_TEXT, textOf(url));
    }

    @Test
    void textOf_invalidURL() {
        URL url = URLUtil.toURL("file:/missing/file");

        UncheckedIOException e = assertThrows(
                UncheckedIOException.class, () -> textOf(url));
        assertEquals(
                "java.io.FileNotFoundException: /missing/file (No such file or directory)",
                e.getMessage());
    }

    @Test
    void textOf_withEmptyStream() {
        URL url = getTestDataResource(EMPTY_TXT_RESOURCE_NAME);

        assertEquals(EMPTY_TXT_TEXT, textOf(url));
    }

    @Test
    void textOf_withCharset() {
        URL url = getTestDataResource(SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME);

        assertEquals(SAMPLE_ISO_8859_1_TXT_TEXT,
                textOf(url, StandardCharsets.ISO_8859_1));
    }

    @Test
    void textOf_withCharsetName() {
        URL url = getTestDataResource(SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME);
        assertEquals(SAMPLE_ISO_8859_1_TXT_TEXT,
                textOf(url, StandardCharsets.ISO_8859_1.name()));
    }

    @Test
    void isURL_OK() {
        assertTrue(isURL("file://a.b"));
        assertTrue(isURL("file://  a.b"));

        assertFalse(isURL("a.b"));
        assertFalse(isURL("/a.b"));
        assertFalse(isURL(""));

    }

    @Test
    void isURI_OK() {
        // valid URL and URI
        assertTrue(isURI("file://a.b"));

        // valid URL, but invalid URI
        assertFalse(isURI("file://  a.b"));

        // Invalid URI, but invalid URI
        assertTrue(isURI("a.b"));
        assertTrue(isURI("/a.b"));
        assertTrue(isURI(""));

    }

    @Test
    void urlDecode_OK() {
        assertEquals("azAZ09..* _A", urlDecode("azAZ09..*+_%41"));
    }

    @Test
    void toFile_OK() throws MalformedURLException {
        File file = toFile(new URL("file:/foo/bar.baz"));

        assertEquals("/foo/bar.baz", file.getPath());
    }

    @Test
    void toFile_URISyntaxException_OK() throws MalformedURLException {
        URL url = new URL("file:/foo/bar|s");

        Exception e = assertThrows(Exception.class, () -> toFile(url));

        assertEquals("Illegal character in path at index 13: file:/foo/bar|s",
                e.getMessage());
    }

}