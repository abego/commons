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

package org.abego.commons.lang;

import org.abego.commons.io.FileUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.abego.commons.lang.ObjectUtil.ignore;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuntimeUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, RuntimeUtil::new);
    }

    @Test
    void execAndReturnOutAndErr_withOutputToStdOut() {
        String s = RuntimeUtil.execAndReturnOutAndErr("echo", "foo");
        assertEquals("foo\n", s);
    }

    @Test
    void execAndReturnOutAndErr_withOutputToStdErr() {

        File file = new File("writeToErr.sh");
        FileUtil.appendText(file, "echo bar 1>&2");
        try {
            String s = RuntimeUtil.execAndReturnOutAndErr("sh", file.getAbsolutePath());
            assertEquals("bar\n", s);
        } finally {
            ignore(file.delete());
        }
    }
}