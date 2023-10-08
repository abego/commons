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

package org.abego.commons.lang;

import org.abego.commons.TestData;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.lang.exception.UncheckedException;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.abego.commons.TestData.MISSING_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_TXT_RESOURCE_NAME;
import static org.abego.commons.lang.ClassUtil.classNameOrNull;
import static org.abego.commons.lang.ClassUtil.newInstanceOfClassNamed;
import static org.abego.commons.lang.ClassUtil.packagePath;
import static org.abego.commons.lang.ClassUtil.resource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, ClassUtil::new);
    }

    @Test
    void resourceOk() {

        URL url = resource(TestData.class, SAMPLE_TXT_RESOURCE_NAME);

        assertTrue(url.getPath().endsWith("/org/abego/commons/sample.txt"));
    }

    @Test
    void resourceTestCaseMissingOk() {

        Exception e = assertThrows(Exception.class,
                () -> resource(getClass(), MISSING_RESOURCE_NAME));

        assertEquals(
                "Resource not found: missing.txt (in org.abego.commons.lang.ClassUtilTest)",
                e.getMessage());
    }

    @Test
    void packagePath_Ok() {

        String packagePath = packagePath(TestData.class);

        assertEquals("org/abego/commons", packagePath);
    }

    @Test
    void classNameOrNull_Ok() {

        String className = classNameOrNull("");

        assertEquals("java.lang.String", className);

        assertNull(classNameOrNull(null));

    }

    @Test
    void newInstanceOfClassNamed_OK() {
        String s = newInstanceOfClassNamed("java.lang.String", String.class);
        assertNotNull(s);
    }

    @Test
    void newInstanceOfClassNamed_UnknownClass() {
        UncheckedException e = assertThrows(UncheckedException.class,
                () -> newInstanceOfClassNamed("com.example.UnknownClass", String.class));
        assertEquals("Error when loading 'com.example.UnknownClass'", e.getMessage());
    }

    @Test
    void newInstanceOfClassNamed_UnexpectedType() {
        UncheckedException e = assertThrows(UncheckedException.class,
                () -> newInstanceOfClassNamed("java.lang.Object", String.class));
        assertEquals("java.lang.String expected, got java.lang.Object", e.getMessage());
    }

    @Test
    void requireType() {

        String s = ClassUtil.requireType("foo", String.class);
        assertEquals("foo", s);

        Exception e = assertThrows(Exception.class,
                () -> ClassUtil.requireType("foo", Float.class));
        assertEquals("Expected type java.lang.Float, got java.lang.String", e.getMessage());

    }


}
