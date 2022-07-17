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

import java.text.MessageFormat;
import java.util.function.Supplier;

import static org.abego.commons.lang.ClassUtil.classNameOrNull;

public final class ObjectUtil {

    ObjectUtil() {
        throw new MustNotInstantiateException();
    }

    public static boolean allAreNotNull(@Nullable Object... objects) {
        for (@Nullable Object o : objects) {
            if (o == null) return false;
        }
        return true;
    }

    /**
     * Do nothing, ignore the argument.
     *
     * <p>The method is typically used in test code to avoid
     * "Result of 'foo()' is ignored" warnings.</p>
     *
     * <b>Usage Example:</b>
     * <pre>
     * assertThrows(Exception.class, () -&gt; ignore(firstChar("")));
     * </pre>
     */
    @SuppressWarnings("EmptyMethod")
    public static void ignore(@Nullable Object ignoredObject) {
        // intentionally empty
    }

    /**
     * Throws an {@link IllegalArgumentException} if the <code>object</code>
     * is not of the <code>expectedType</code>.
     */
    public static <T> T checkType(@Nullable Object object, Class<? extends T> expectedType) {
        if (!(expectedType.isInstance(object))) {
            throw new IllegalArgumentException(
                    MessageFormat.format(
                            "Object is not of type {0} (but {1})", //NON-NLS
                            expectedType.getName(), classNameOrNull(object)));
        }

        return expectedType.cast(object);
    }

    /**
     * Returns the {@code value} when it is not {@code null}, otherwise return the {@code otherValue}.
     *
     * <p>In some programming languages this operation is performed by an "Elvis" operator ("?:").</p>
     */
    public static <T> @NonNull T valueOrElse(@Nullable T value, @NonNull T otherValue) {
        return value != null ? value : otherValue;
    }

    /**
     * Returns the {@code value} when it is not {@code null}, otherwise throw a
     * {@link NullPointerException} with the given message.
     */
    public static <T> @NonNull T valueOrFail(@Nullable T value, @NonNull String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

    /**
     * Returns the {@code value} when it is not {@code null}, otherwise throw a
     * {@link NullPointerException} with the given message.
     */
    public static <T> @NonNull T valueOrFail(@Nullable T value, @NonNull Supplier<String> messageSupplier) {
        if (value == null) {
            throw new NullPointerException(messageSupplier.get());
        }
        return value;
    }

    /**
     * Returns the {@code value} when it is not {@code null}, otherwise throw a
     * {@link NullPointerException}.
     */
    public static <T> @NonNull T valueOrFail(@Nullable T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }


    /**
     * Compares the "toString" representations of o1 and o2,
     * as defined by {@link StringUtil#compareToIgnoreCaseStable}.
     *
     * <p>{@code null} values are assumed to be larger than any String.
     *
     * <p>This method does not take locale into account, and may result in an
     * unsatisfactory ordering for certain locales.</p>
     */
    public static int compareAsTexts(@Nullable Object o1, @Nullable Object o2) {
        if (o1 == null) {
            return o2 == null ? 0 : 1;
        } else if (o2 == null) {
            return -1;
        }
        return StringUtil.compareToIgnoreCaseStable(
                o1.toString(), o2.toString());
    }
}
