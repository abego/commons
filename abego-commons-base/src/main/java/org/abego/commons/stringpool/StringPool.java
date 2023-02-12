/*
 * MIT License
 *
 * Copyright (c) 2022 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.stringpool;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A StringPool contains a set of {@link String}s that are accessible through
 * an ID, stored in an <code>int</code>.
 *
 * <p>Create a StringPool using a {@link StringPoolBuilder}.</p>
 *
 * <p> The default implementation of a StringPool stores the strings in a
 * compact form only. E.g. a String of ASCII characters and length
 * <code>n</code> &lt;= 127 only requires <code>n + 1</code> bytes. Especially
 * for short strings this is significantly less that used for a native Java
 * {@link String} object. Also equal strings are only stored once in the pool.
 * So a StringPool fits well into an application that has memory constraints
 * and uses a lot of short (and/or duplicated) strings.</p>
 *
 * <p>A side effect of StringPool strings: as the StringPool holds unique
 * strings the equal check of string can be reduced to a an int check of the IDs.
 * </p>
 *
 * <p>A {@code null} String has the ID {@code 0}.</p>
 */
@SuppressWarnings("EmptyMethod")
public interface StringPool {

    /**
     * Return the {@link String} with the given <code>id</code> or
     * <code>null</code> when <code>id == 0</code>.
     */
    @Nullable
    String getStringOrNull(int id);

    /**
     * Return the {@link String} with the given <code>id</code> or
     * throw a {@link NullPointerException} when <code>id == 0</code>, i.e.
     * <code>id</code> identifies the {@code null} String.
     */
    default String getString(int id) {
        @Nullable String obj = getStringOrNull(id);
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }

    /**
     * Return an {@link Iterable} with all strings in the StringPool.
     */
    Iterable<String> allStrings();

    /**
     * Return an {@link Iterable} with all strings in the StringPool, together with the string's ID in this StringPool.
     */
    Iterable<StringAndID> allStringAndIDs();

    default boolean isByteAccessSupported() {
        return false;
    }

    default byte[] getBytes() {
        throw new UnsupportedOperationException();
    }

    /**
     * Holds a {@link String} and its ID in this StringPool.
     */
    interface StringAndID {
        String getString();

        int getID();
    }
}
