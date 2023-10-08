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

package org.abego.commons.io;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IOFunctionUtilTest {
    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, IOFunctionUtil::new);
    }

    @Test
    void applyInputStreamOfFileOn(@TempDir File tempDir) {
        File file = new File(tempDir, "file");
        FileUtil.writeText(file, "foo");

        String text = IOFunctionUtil.applyInputStreamOfFileOn(file,
                InputStreamUtil::textOf);

        assertEquals("foo", text);

        UncheckedIOException e = assertThrows(UncheckedIOException.class, () ->
                IOFunctionUtil.applyInputStreamOfFileOn(tempDir,
                        InputStreamUtil::textOf));
        assertTrue(e.getMessage().startsWith("java.io.FileNotFoundException"));
    }

    @Test
    void applyOutputStreamOfFileOn(@TempDir File tempDir) {
        File file = new File(tempDir, "file");

        String text = IOFunctionUtil.applyOutputStreamOfFileOn(file,
                os -> {
                    OutputStreamUtil.write("foo", os, StandardCharsets.UTF_8);
                    return "bar";
                });

        assertEquals("foo", FileUtil.textOf(file));
        assertEquals("bar", text);

        UncheckedIOException e = assertThrows(UncheckedIOException.class, () ->
                IOFunctionUtil.applyOutputStreamOfFileOn(tempDir,
                        os -> {
                            OutputStreamUtil.write("foo", os, StandardCharsets.UTF_8);
                            return "bar";
                        }));
        assertTrue(e.getMessage().startsWith("java.io.FileNotFoundException"));
    }
}
