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

package org.abego.commons.lang;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Implement {@link Object#toString()} methods more easily
 */
public class ToStringBuilder {
    public static final String ALREADY_CLOSED_MESSAGE = "ToStringBuilder already closed"; //NON-NLS

    private final StringBuilder result = new StringBuilder();
    private boolean hasMembers;
    private boolean closed;

    private ToStringBuilder(String className) {
        result.append(className);
        result.append("{");
    }

    public static ToStringBuilder newToStringBuilder(String className) {
        return new ToStringBuilder(className);
    }

    public ToStringBuilder field(String name, @Nullable String value) {
        checkState();
        appendSeparator();
        result.append(name);
        result.append("=");
        result.append(StringUtil.singleQuoted(value));
        return this;
    }

    private void appendSeparator() {
        if (hasMembers) {
            result.append(", ");
        } else {
            hasMembers = true;
        }
    }

    public ToStringBuilder field(String name, @Nullable Object value) {
        checkState();
        appendSeparator();
        result.append(name);
        result.append("=");
        result.append(value);
        return this;
    }

    public ToStringBuilder fieldIf(boolean flag, String name, @Nullable Object value) {
        checkState();
        if (flag) {
            field(name, value);
        }
        return this;
    }

    public ToStringBuilder fieldIf(boolean flag, String name, @Nullable String value) {
        checkState();
        if (flag) {
            field(name, value);
        }
        return this;
    }

    public ToStringBuilder fieldOptional(String name, @Nullable String value) {
        checkState();
        if (value != null) {
            field(name, value);
        }
        return this;
    }

    public String build() {
        checkState();
        result.append("}");
        closed = true;
        return toString();
    }

    public String toString() {
        return result.toString();
    }

    private void checkState() {
        if (closed) {
            throw new IllegalStateException(ALREADY_CLOSED_MESSAGE);
        }
    }
}