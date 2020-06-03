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

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.util.OptionalInt;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Utility methods related to the primitive type {@code int}
 */

public final class IntUtil {
    IntUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Return an array of {@code n} {@code int} values for {@code 0} to {@code n-1}.
     */
    public static int[] intSubRangeArray(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    String.format("n > 0 expected. {n: %d}", n)); //NON-NLS
        }

        return intSubRangeArray(0, n - 1);
    }


    /**
     * Return an array of {@code int} values for {@code start} to {@code end}.
     */
    public static int[] intSubRangeArray(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException(
                    String.format("start <= end expected. {start: %d, end: %d}", //NON-NLS
                            start, end));
        }

        int[] result = new int[end - start + 1];
        for (int i = start; i <= end; i++) {
            result[i - start] = i;
        }
        return result;
    }

    /**
     * Throw an {@link IndexOutOfBoundsException} when {@code i < lowerBound} or {@code i >= upperBoundExclusive}
     */
    public static void checkBoundsEndOpen(int i, int lowerBound, int upperBoundExclusive) {
        if (i < lowerBound || i >= upperBoundExclusive) {
            throw new IndexOutOfBoundsException(
                    String.format("Expected %d <= i < %d, got %d", lowerBound, upperBoundExclusive, i)); //NON-NLS
        }
    }

    /**
     * Return {@code i}, or {@code lowerBound} when {@code i < lowerBound}, or {@code upperBound} when {@code i > upperBound}.
     * <p>
     * Throw an {@link IllegalArgumentException} when {@code lowerBound > upperBound}.
     */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    // String checked in tests
    public static int limit(int i, int lowerBound, int upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("lowerBound must not be larger than upperBound"); //NON-NLS
        }

        return max(lowerBound, min(i, upperBound));
    }

    /**
     * Return {@code i}, or {@code 0} when {@code i < 0}, or {@code upperBound} when {@code i > upperBound}.
     */
    public static int limit(int i, int upperBound) {
        return limit(i, 0, upperBound);
    }

    public static OptionalInt parseInt(String s) {
        try {
            return OptionalInt.of(Integer.parseInt(s));
        } catch (Exception e) {
            return OptionalInt.empty();
        }
    }
}
