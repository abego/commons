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

package org.abego.commons.diff.internal;

import org.eclipse.jdt.annotation.Nullable;

import java.util.Objects;

class AlignedItemPair<T> {
    private final @Nullable T first;
    private final @Nullable T second;

    private AlignedItemPair(@Nullable T first, @Nullable T second) {
        this.first = first;
        this.second = second;
    }

    public static <T> AlignedItemPair<T> newAlignedItemPair(@Nullable T first, @Nullable T second) {
        return new AlignedItemPair<>(first, second);
    }

    public @Nullable T first() {
        return first;
    }

    public @Nullable T second() {
        return second;
    }

    public boolean hasEqualItems() {
        return Objects.equals(first(), second());
    }


}
