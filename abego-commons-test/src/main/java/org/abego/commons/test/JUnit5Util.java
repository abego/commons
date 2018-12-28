/*
 * MIT License
 *
 * Copyright (c) 2018 Udo Borkowski, (ub@abego.org)
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

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import static org.abego.commons.lang.exception.MustNotInstantiateException.throwMustNotInstantiate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JUnit5Util {

    JUnit5Util() {
        throwMustNotInstantiate();
    }

    public static void assertTextContains(
            String expectedSubstring,
            String text,
            String descriptor) {
        if (!text.contains(expectedSubstring)) {
            fail(MessageFormat.format(
                    "{0} does not contain \"{1}\".\nGot:\n\"{2}\"", // NON-NLS
                    descriptor, expectedSubstring, text));
        }
    }

    public static void assertTextOfFileEquals(
            final String expected,
            final File file,
            final Charset charset) throws FileNotFoundException {
        assertEquals(
                expected,
                FileUtil.textOf(file, charset),
                MessageFormat.format(
                        "Wrong text in file {0}", // NON-NLS
                        file.getAbsolutePath()));
    }

    public static void assertTextOfFileEquals(
            final String expected,
            final File file) throws FileNotFoundException {

        assertTextOfFileEquals(expected, file, StandardCharsets.UTF_8);
    }


}
