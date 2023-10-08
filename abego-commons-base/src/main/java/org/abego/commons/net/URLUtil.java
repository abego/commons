/*
 * MIT License
 *
 * Copyright (c) 2022 Udo Borkowski, (ub@abego.org)
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

    /**
     * Deprecated. Use {@link org.abego.commons.io.FileUtil#toFile(URL)} instead
     */
    @Deprecated
    public static File toFile(URL url) {
        try {
            return URIUtil.toFile(url.toURI());
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

    /**
     * Decodes a application/x-www-form-urlencoded string
     * using a specific encoding scheme.
     * <p>
     * The supplied encoding is used to determine what characters
     * are represented by any consecutive sequences of the form "%xy".
     */
    public static String urlDecode(String string, String encoding) {
        try {
            return URLDecoder.decode(string, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Decodes a application/x-www-form-urlencoded string using UTF-8.
     * <p>
     * UTF-8 is used to determine what characters
     * are represented by any consecutive sequences of the form "%xy".
     */
    public static String urlDecode(String string) {
        return urlDecode(string, StandardCharsets.UTF_8.name());
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

    /**
     * Assuming the {@code url} refers to a JAR resource/file the method
     * returns the "JAR:" protocol URL to refer to entries within that JAR.
     *
     * @param url   the {@link URL} to the JAR resource/file
     * @param entry the path to the entry within the JAR.
     *              (Default: "", i.e. the root directory in the JAR)
     * @see <a href="https://docs.oracle.com/cd/E19253-01/819-0913/author/jar.html#jarprotocol">The JAR Protocol</a>
     */
    public static URL asJarProtocolURL(URL url, String entry) {
        return URLUtil.toURL(String.format("jar:%s!/%s", url, entry)); //NON-NLS
    }

    /**
     * See {@link #asJarProtocolURL(URL, String)}.
     */
    public static URL asJarProtocolURL(URL url) {
        return asJarProtocolURL(url, "");
    }

}
