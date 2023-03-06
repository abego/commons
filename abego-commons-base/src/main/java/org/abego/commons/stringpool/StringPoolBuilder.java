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
 * Provides a way to create a {@link StringPool}.
 *
 * <p>Use {@link #add(String)} to add all {@link String}s to be included in the StringPool. Then call {@link #build()}
 * to create the actual StringPool.</p>
 *
 * @deprecated use https://github.com/abego/abego-stringpool instead
 */
@Deprecated
public interface StringPoolBuilder {

    /**
     * Add the <code>string</code> to the builder and return the string's ID.
     *
     * <p>Later you may use the ID to retrieve the String from the {@link StringPool}.</p>
     *
     * @param string the {@link String} to be added to builder, or <code>null</code>.
     */
    int add(@Nullable String string);

    /**
     * Joining the <code>stringParts</code> to one string, add that string
     * to the builder and return the ID of the just added String.
     * <p>
     * Later you may use the ID to retrieve the String from the {@link StringPool}.
     * <p>
     * When the joined string already exists in the builder that former
     * string's ID is returned. No new joined string is added, even if the
     * individual parts of the current call differ from the ones of the former
     * string, or even when the former string was added with {@link #add(String)}.
     * <p>
     * {@code null} or the empty String are allowed as part values.
     * <p>{@code null} values don't contribute to the resulting String, however
     * passing all {@code null} values or no value at all is the same as calling
     * {@link #add(String)} with a {@code null} argument.
     * <p>
     * You may use this method instead of {@link #add(String)} when certain
     * substrings of the strings you are adding to the builder are repeating.
     * An implementation of the  may store such strings
     * in a more compact form, thus reducing the memory requirements for the
     * resulting {@link StringPool}.
     * <p>
     * When using this method the individual parts <em>may</em> also appear as
     * separate Strings when calling {@link StringPool#allStrings()} or
     * {@link StringPool#allStringAndIDs()}. But you must not rely on this as
     * an implementation of StringPoolBuilder may choose not to make these
     * parts visible or not to store the parts individually at all, e.g. by
     * using this method's default implementation.
     *
     * @param stringParts the parts to be joined to construct the {@link String}
     *                    to be added to builder.
     */
    default int addJoined(@Nullable String... stringParts) {
        return add(joinedString(stringParts));
    }

    @Nullable
    default String joinedString(@Nullable String... stringParts) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean hasNonNullPart = false;
        for (String s : stringParts) {
            if (s != null) {
                hasNonNullPart = true;
                stringBuilder.append(s);
            }
        }
        return hasNonNullPart ? stringBuilder.toString() : null;
    }

    /**
     * Returns {@code true} when the builder already contains the
     * <code>string</code>, {@code false} otherwise.
     * <p>
     * A {@code string == null} will return {@code true}.
     *
     * @param string the {@link String} to check, or <code>null</code>.
     */
    boolean contains(@Nullable String string);

    /**
     * Return a new {@link StringPool} containing all strings added so far.
     */
    StringPool build();
}
