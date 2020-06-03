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

package org.abego.commons.javalang;

import org.abego.commons.io.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.abego.commons.io.FileUtil.file;
import static org.abego.commons.javalang.FullyQualifiedName.fullyQualifiedName;
import static org.abego.commons.javalang.FullyQualifiedName.fullyQualifiedNameString;
import static org.abego.commons.javalang.FullyQualifiedName.isFullyQualifiedName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FullyQualifiedNameTest {

    private static final String Q_NAME_SAMPLE_NAME = "com.example.bar";

    private static final FullyQualifiedName Q_NAME_SAMPLE =
            fullyQualifiedName(Q_NAME_SAMPLE_NAME);

    @Test
    void parentPath_ok() {
        assertEquals("com.example", Q_NAME_SAMPLE.parentPath());
    }

    @Test
    void parentPath_singleStepParent() {
        assertEquals("foo", fullyQualifiedName("foo.bar").parentPath());
    }

    @Test
    void parentPath_oneLetterParent() {
        // this test was introduced to catch a "changed conditional boundary"
        // mutation by pitest, mutating a "<" in parentPath to "<=".
        assertEquals("a", fullyQualifiedName("a.bar").parentPath());
    }

    @Test
    void parentPath_emptyParent() {
        assertEquals("", fullyQualifiedName("foo").parentPath());
    }

    @Test
    void toString_ok() {
        assertEquals(Q_NAME_SAMPLE_NAME, Q_NAME_SAMPLE.toString());
    }

    @Test
    void simpleName_ok() {
        FullyQualifiedName name = fullyQualifiedName("foo");

        assertEquals("foo", name.simpleName());
        assertEquals("foo", name.name());
        assertEquals("", name.parentPath());
    }

    @Test
    void parentDirectory_simpleName() {
        File root = FileUtil.tempDirectoryForRun();
        FullyQualifiedName name = fullyQualifiedName("foo");

        assertEquals(root, name.parentDirectory(root));
    }

    @Test
    void parentDirectory_nameWithPackage() {
        File root = FileUtil.tempDirectoryForRun();
        FullyQualifiedName name = fullyQualifiedName("foo.bar");

        assertEquals(file(root, "foo"), name.parentDirectory(root));
    }


    @Test
    void isFullyQualifiedName_ok() {
        assertTrue(isFullyQualifiedName("abc"));
        assertTrue(isFullyQualifiedName("abc.bcd"));
        assertTrue(isFullyQualifiedName("abc.bc.Def"));

        assertFalse(isFullyQualifiedName(""));
        assertFalse(isFullyQualifiedName("."));
        assertFalse(isFullyQualifiedName("abc."));
        assertFalse(isFullyQualifiedName(".def"));
        assertFalse(isFullyQualifiedName("abc.def."));
        assertFalse(isFullyQualifiedName("abc..def"));
        assertFalse(isFullyQualifiedName("1"));
        assertFalse(isFullyQualifiedName("?"));
    }

    @Test
    void fullyQualifiedNameString_ok() {
        assertEquals("abc", fullyQualifiedNameString("abc"));
        assertEquals("abc.bcd", fullyQualifiedNameString("abc.bcd"));
        assertEquals("abc.bc.Def", fullyQualifiedNameString("abc.bc.Def"));

        assertThrows(Exception.class, () -> fullyQualifiedNameString(""));
        assertThrows(Exception.class, () -> fullyQualifiedNameString("1"));
        assertThrows(Exception.class, () -> fullyQualifiedNameString("?"));
    }

    @Test
    void equals_ok() {
        FullyQualifiedName a = fullyQualifiedName("p.a");
        FullyQualifiedName a2 = fullyQualifiedName("p.a");
        FullyQualifiedName b = fullyQualifiedName("p.b");

        assertEquals(a, a);
        assertEquals(a, a2);
        assertNotEquals(a, b);
        assertNotEquals(a, "p.a");
    }

    @Test
    void hashCode_ok() {
        FullyQualifiedName a = fullyQualifiedName("p.a");
        FullyQualifiedName a2 = fullyQualifiedName("p.a");
        FullyQualifiedName b = fullyQualifiedName("p.b");

        assertEquals(a.hashCode(), a.hashCode());
        assertEquals(a.hashCode(), a2.hashCode());
        assertNotEquals(a.hashCode(), b.hashCode());
    }
}