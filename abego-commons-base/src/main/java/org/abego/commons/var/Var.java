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

package org.abego.commons.var;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Var<T> extends Supplier<@NonNull T> {

    /**
     * Return the value of the Var, or throw an {@link IllegalStateException} when the Var has no value.
     */
    @NonNull T get();

    void set(@NonNull T value);

    default boolean hasValue() {
        return true;
    }

    default @NonNull T getOrElse(@NonNull T defaultValue) {
        return hasValue() ? get() : defaultValue;
    }

    default @NonNull T getOrElse(Supplier<@NonNull T> defaultValueSupplier) {
        return hasValue() ? get() : defaultValueSupplier.get();
    }

    default @Nullable T getOrNull() {
        return hasValue() ? get() : null;
    }

    default <R> @NonNull R mapOrElse(
            Function<? super @NonNull T, ? extends @NonNull R> mapper,
            Supplier<@NonNull R> defaultValueSupplier) {
        return hasValue() ? mapper.apply(get()) : defaultValueSupplier.get();
    }

    default <R> @NonNull R mapOrElse(
            Function<? super @NonNull T, ? extends @NonNull R> mapper,
            R defaultValue) {
        return hasValue() ? mapper.apply(get()) : defaultValue;
    }

    default <R> @Nullable R mapOrNull(
            Function<? super @NonNull T, ? extends @NonNull R> mapper) {
        return hasValue() ? mapper.apply(get()) : null;
    }

}
