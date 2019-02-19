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
import java.text.MessageFormat;
import java.util.Objects;

import static org.abego.commons.lang.exception.UncheckedException.uncheckedException;

/**
 * A Fully Qualified Name, as defined in chapter 6.7 of "The Java® Language Specification
 * Java SE 11 Edition".
 */
public class FullyQualifiedName {
    /**
     * The character used to separate the (identifier) parts of a qualified name
     */
    public static final char QUALIFIED_NAME_PART_SEPARATOR_CHAR = '.'; // NON-NLS
    public static final String QUALIFIED_NAME_PART_SEPARATOR_REGEX = "\\."; // NON-NLS

    private final String fullName;
    private final int endIndexOfPackagePath;

    FullyQualifiedName(String fullyQualifiedName) {

        this.fullName = fullyQualifiedNameString(fullyQualifiedName);
        endIndexOfPackagePath = fullyQualifiedName.lastIndexOf(QUALIFIED_NAME_PART_SEPARATOR_CHAR);
    }

    public static FullyQualifiedName fullyQualifiedName(
            String fullyQualifiedName) {

        return new FullyQualifiedName(fullyQualifiedName);
    }

    public static String fullyQualifiedNameString(String text) {

        if (!isFullyQualifiedName(text)) {
            throw uncheckedException(MessageFormat.format(
                    "Fully qualified name expected. Got {0}", text)); // NON-NLS
        }
        return text;
    }

    public static boolean isFullyQualifiedName(String text) {

        String[] names = text.split("\\.", -1); // NON-NLS
        for (String name : names) {
            if (!JavaLangUtil.isJavaIdentifier(name)) {
                return false;
            }
        }
        return true;
    }

    public String name() {
        return fullName;
    }

    public String simpleName() {
        return fullName.substring(endIndexOfPackagePath + 1);
    }

    public String parentPath() {
        return endIndexOfPackagePath < 1 ? "" :
                fullName.substring(0, endIndexOfPackagePath);
    }

    public File parentDirectory(File rootDirectory) {

        String path = parentPath().replaceAll(
                QUALIFIED_NAME_PART_SEPARATOR_REGEX, File.separator);
        return new File(rootDirectory, path);
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FullyQualifiedName)) {
            return false;
        }
        FullyQualifiedName that = (FullyQualifiedName) o;
        return fullName.equals(that.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }
}
