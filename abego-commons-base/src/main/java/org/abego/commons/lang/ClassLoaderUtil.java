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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static org.abego.commons.net.URLUtil.asJarProtocolURL;

public final class ClassLoaderUtil {
    ClassLoaderUtil() {
        throw new MustNotInstantiateException();
    }

    public static ClassLoader classLoaderUsingJars(File[] jarFiles) {
        if (jarFiles.length == 0) {
            return getMyClassLoader();
        }

        List<URL> urls = new ArrayList<>();
        for (File file : jarFiles) {
            String path = file.getAbsolutePath();
            if (!file.isFile()) {
                throw new IllegalArgumentException("Missing file: " + path);
            }
            urls.add(asJarProtocolURL(FileUtil.toURL(file)));
        }

        return URLClassLoader.newInstance(
                urls.toArray(new URL[0]),
                ClassLoaderUtil.class.getClassLoader());
    }

    private static ClassLoader getMyClassLoader() {
        return ClassLoaderUtil.class.getClassLoader();
    }
}
