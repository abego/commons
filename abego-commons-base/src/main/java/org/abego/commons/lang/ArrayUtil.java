/*
 * MIT License
 *
 * Copyright (c) 2021 Udo Borkowski, (ub@abego.org)
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

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static org.abego.commons.lang.IntUtil.checkBoundsEndOpen;

public final class ArrayUtil {

    ArrayUtil() {
        throw new MustNotInstantiateException();
    }

    // --- Factories ---

    public static <T> Iterator<T> iterator(T[] array) {
        return new Iterator<T>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return array[i++];
            }
        };
    }

    // --- Queries ---

    /**
     * @return &lt; 0 if not found, otherwise the index of the first occurrence of item
     */
    @SuppressWarnings("WeakerAccess")
    public static <T> int indexOf(@Nullable T[] array, @Nullable T item) {
        for (int i = 0; i < array.length; i++) {
            T t = array[i];
            if (t == null) {
                if (item == null) {
                    return i;
                }
            } else {
                if (t.equals(item)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static <T> boolean contains(T[] array, T item) {
        return indexOf(array, item) >= 0;
    }

    /**
     * Return the {@code index}-ed item of the {@code array} when the array is not {@code null} and
     * the index is in {@code 0..{@code array.length-1}}, otherwise the {@code defaultValue}.
     */
    public static <T> T itemOrDefault(T @Nullable [] array, int index, T defaultValue) {
        return array != null && index >= 0 && index < array.length ? array[index] : defaultValue;
    }

    /**
     * Check if {@code i} is a valid index for an array of size {@code arraySize} and throw an
     * {@link IndexOutOfBoundsException} when it isn't.
     */
    public static void checkArrayIndex(int i, int arraySize) {
        checkBoundsEndOpen(i, 0, arraySize);
    }

    public static <T> T lastItem(@NonNull T[] array) {
        if (array.length == 0) {
            //noinspection DuplicateStringLiteralInspection
            throw new IllegalArgumentException("Empty array has no last item"); //NON-NLS
        }
        return array[array.length - 1];
    }

    @Nullable
    public static <T> T firstOrNull(T[] array, Predicate<T> predicate) {
        for (T i : array) {
            if (predicate.test(i)) {
                return i;
            }
        }
        return null;
    }
}
