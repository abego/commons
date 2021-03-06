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

package org.abego.commons.net;

import org.abego.commons.io.InputStreamUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.abego.commons.lang.exception.UncheckedException.newUncheckedException;

public final class URLUtil {
    URLUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Returns an {@code URL} object from the {@code String} representation
     * given in {@code spec}.
     *
     * @param spec an Uniform Resource Identifier specification, as define in
     *             RFC2396
     * @return the URL as specified by spec
     */
    public static URL toURL(String spec) {
        try {
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(String.format("Invalid URL specification: %s", spec), e); //NON-NLS
        }
    }

    public static File toFile(URL url) {
        try {
            return Paths.get(url.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw newUncheckedException(e);
        }
    }

    public static String textOf(URL url) {
        return textOf(url, StandardCharsets.UTF_8);
    }

    public static String textOf(URL url, String charsetName) {
        try (InputStream inputStream = url.openStream()) {
            return InputStreamUtil.textOf(inputStream, charsetName);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String textOf(URL url, Charset charset) {
        return textOf(url, charset.name());
    }

    public static String urlDecode(String string) {
        try {
            return URLDecoder.decode(string, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isURL(String text) {
        try {
            new URL(text);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean isURI(String text) {
        try {
            new URI(text);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
