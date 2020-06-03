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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassNameTest {

    private static final String CLASS_NAME_SAMPLE_PACKAGE = "org.example"; //NON-NLS
    private static final String CLASS_NAME_SAMPLE_SIMPLE_NAME = "Foo"; //NON-NLS
    private static final String CLASS_NAME_SAMPLE_NAME = "org.example.Foo"; //NON-NLS
    private static final File SRC_DIR_SAMPLE = new File("src/main"); //NON-NLS
    private static final String CLASS_NAME_SAMPLE_JAVA_FILE_PATH =
            "src/main/org/example/Foo.java"; //NON-NLS

    private static final ClassName CLASS_NAME_SAMPLE =
            ClassName.className(CLASS_NAME_SAMPLE_NAME);

    @Test
    void className_ok() {
        ClassName cn = ClassName.className(CLASS_NAME_SAMPLE_NAME);

        Assertions.assertNotNull(cn);
    }

    @Test
    void name_ok() {
        assertEquals(CLASS_NAME_SAMPLE_NAME, CLASS_NAME_SAMPLE.name());
    }

    @Test
    void simpleName_ok() {
        assertEquals(CLASS_NAME_SAMPLE_SIMPLE_NAME, CLASS_NAME_SAMPLE.simpleName());
    }

    @Test
    void packagePath_ok() {
        assertEquals(CLASS_NAME_SAMPLE_PACKAGE, CLASS_NAME_SAMPLE.packagePath());
    }

    @Test
    void javaFileInSourceRoot_static_ok() {
        File file = ClassName.javaFileInSourceRoot(
                CLASS_NAME_SAMPLE_NAME, SRC_DIR_SAMPLE);

        assertEquals(CLASS_NAME_SAMPLE_JAVA_FILE_PATH, file.getPath());
    }

    @Test
    void javaFileInSourceRoot_ok() {
        File file = CLASS_NAME_SAMPLE.javaFileInSourceRoot(SRC_DIR_SAMPLE);

        assertEquals(CLASS_NAME_SAMPLE_JAVA_FILE_PATH, file.getPath());
    }

    @Test
    void toString_ok() {
        assertEquals(CLASS_NAME_SAMPLE_NAME, CLASS_NAME_SAMPLE.toString());
    }
}