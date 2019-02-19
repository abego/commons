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
package org.abego.commons.javalang;

import java.io.File;

import static org.abego.commons.io.FileUtil.file;

/**
 * A fully qualified name for a class.
 */
public final class ClassName extends FullyQualifiedName {

    private ClassName(String fullyQualifiedClassName) {
        super(fullyQualifiedClassName);
    }

    public static ClassName className(String fullyQualifiedClassName) {
        return new ClassName(fullyQualifiedClassName);
    }

    public static File javaFileInSourceRoot(
            String fullyQualifiedClassName, File sourceRoot) {

        ClassName className = ClassName.className(fullyQualifiedClassName);
        return className.javaFileInSourceRoot(sourceRoot);
    }

    public File javaFileInSourceRoot(File sourceRoot) {

        String pathname = String.format("%s%s%s.java", // NON-NLS
                packageDirectory(sourceRoot), File.separator, simpleName());
        return file(pathname);
    }

    public String packagePath() {
        return parentPath();
    }

    public File packageDirectory(File rootDirectory) {
        return parentDirectory(rootDirectory);
    }

    @Override
    public String toString() {
        return name();
    }
}
