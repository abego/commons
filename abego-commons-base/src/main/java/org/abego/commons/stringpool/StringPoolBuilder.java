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

package org.abego.commons.stringpool;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Provides a way to create a {@link StringPool}.
 *
 * <p>Use {@link #add(String)} to add all {@link String}s to be included in the StringPool. Then call {@link #build()}
 * to create the actual StringPool.</p>
 */
public interface StringPoolBuilder {

    /**
     * Add the <code>string</code> to the builder and return the string's ID.
     *
     * <p>Later you may the ID to retrieve the String from the {@link StringPool}.</p>
     *
     * @param string the {@link String} to be added to builder, or <code>null</code>.
     */
    int add(@Nullable String string);

    /**
     * Return a new {@link StringPool} containing all strings added so far.
     */
    StringPool build();
}
