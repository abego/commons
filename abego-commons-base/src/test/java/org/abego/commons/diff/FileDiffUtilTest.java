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

package org.abego.commons.diff;

import org.abego.commons.io.FileUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.MessageFormat;

import static org.abego.commons.diff.FileDiffUtil.DirectoryDifferencesOptions.IGNORE_DOT_DS_STORE_FILES;
import static org.abego.commons.diff.FileDiffUtil.directoryDifferences;
import static org.abego.commons.io.WriterUtil.write;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileDiffUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, FileDiffUtil::new);
    }

    @Test
    void directoryDifferencesOK() {
        File root = FileUtil.tempDirectoryForRun();
        File a = new File(root, "a");
        File b = new File(root, "b");
        File f1a = new File(a, "f1");
        File f1b = new File(b, "f1");
        File f2b = new File(b, "f2");
        File f3a = new File(a, "f3");
        File f3b = new File(b, "f3");
        File dsstore = new File(a, ".DS_Store");
        File dsstoreFile = new File(dsstore, "inDSStore");

        FileUtil.ensureFileExists(f1a);
        FileUtil.ensureFileExists(f1b);
        FileUtil.ensureFileExists(f2b);
        FileUtil.ensureFileExists(dsstoreFile);
        write(f3a, "foo\n");
        write(f3b, "bar\n");

        assertEquals("", directoryDifferences(a, a));

        assertEquals(MessageFormat.format(
                        "Only in {0}/a: .DS_Store\nOnly in {0}/b: f2\n" +
                                "diff -r {0}/a/f3 {0}/b/f3\n" +
                                "1c1\n" +
                                "< foo\n" +
                                "---\n" +
                                "> bar\n", root.getAbsolutePath()),
                directoryDifferences(a, b));

        // with IGNORE_DOT_DS_STORE_FILES
        assertEquals(MessageFormat.format(
                        "Only in {0}/b: f2\n" +
                                "diff -r {0}/a/f3 {0}/b/f3\n" +
                                "1c1\n" +
                                "< foo\n" +
                                "---\n" +
                                "> bar\n", root.getAbsolutePath()),
                directoryDifferences(a, b, IGNORE_DOT_DS_STORE_FILES));
    }
}
