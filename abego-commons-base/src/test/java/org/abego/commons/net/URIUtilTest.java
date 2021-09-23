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

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class URIUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, URIUtil::new);
    }

    @Test
    void toFile_OK() throws URISyntaxException {
        File file = URIUtil.toFile(new URI("file:/foo/bar.baz"));

        assertEquals("/foo/bar.baz", file.getPath());
    }

    @Test
    void toPath_OK() throws URISyntaxException {
        Path path = URIUtil.toPath(new URI("file:/foo/bar.baz"));

        assertEquals("/foo/bar.baz", path.toString());
    }

    @Test
    void toPath_wrong_URI_OK() {

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> URIUtil.toPath(new URI("http:/foo/bar.baz")));

        assertEquals("File URI expected, got 'http:/foo/bar.baz'.",
                e.getMessage());
    }
}