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

import org.abego.commons.io.FileUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassLoaderUtilTest {
    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, ClassLoaderUtil::new);
    }

    @Test
    void smoketest() throws FileNotFoundException, ClassNotFoundException {
        URL url = getClass().getResource("/org/abego/commons/hello.jar");
        if (url == null) {
            throw new FileNotFoundException();
        }
        File[] jarFiles = new File[]{FileUtil.toFile(url)};

        ClassLoader cl = ClassLoaderUtil.classLoaderUsingJars(jarFiles);

        assertNotNull(cl);
        Class<?> c = cl.loadClass("org.abego.commons.Main");
        assertEquals("Main", c.getSimpleName());
    }

    @Test
    void missingFile() {
        File[] jarFiles = new File[]{new File("no-such-jar")};

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> ClassLoaderUtil.classLoaderUsingJars(jarFiles));
        assertTrue(e.getMessage().startsWith("Missing file:"));
    }


    @Test
    void myClassLoader() {
        File[] noJarFiles = new File[0];

        ClassLoader cl = ClassLoaderUtil.classLoaderUsingJars(noJarFiles);

        assertNotNull(cl);
    }
}
