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

package org.abego.commons.util.function;

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.util.function.Predicate;

public final class PredicateUtil {

    PredicateUtil() {
        throw new MustNotInstantiateException();
    }

    public static <T> Predicate<T> alwaysTrue() {
        return o -> true;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return o -> false;
    }

    /**
     * Returns a new {@link Predicate} that is the "or" connection of
     * the given {@code predicates}, similar to {@link Predicate#or(Predicate)},
     * but for multiple {@link Predicate}s.
     */
    public static <T> Predicate<T> or(
            Iterable<Predicate<T>> predicates) {
        return valueToTest -> isAnyPredicateTrue(predicates, valueToTest);
    }

    /**
     * Returns a new {@link Predicate} that is the "and" connection of
     * the given {@code predicates}, similar to {@link Predicate#and(Predicate)},
     * but for multiple {@link Predicate}s.
     */
    public static <T> Predicate<T> and(
            Iterable<Predicate<T>> predicates) {
        return valueToTest -> areAllPredicatesTrue(predicates, valueToTest);
    }

    /**
     * Returns a new {@link Predicate} that is the "and" connection of
     * the given {@code predicates}, similar to {@link Predicate#and(Predicate)},
     * but for multiple {@link Predicate}s.
     */
    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<T>... predicates) {
        return valueToTest -> areAllPredicatesTrue(valueToTest, predicates);
    }

    /**
     * Returns {@code true} if any of the {@code predicates} returns
     * {@code true} for the given {@code valueToTest}, {@code false} when all
     * {@link Predicate}s return {@code false}.
     * <p>
     * See also {@link #areAllPredicatesTrue(Iterable, Object)} and
     * {@link #or(Iterable)}.
     */
    public static <T> boolean isAnyPredicateTrue(
            Iterable<Predicate<T>> predicates, T valueToTest) {
        for (Predicate<T> predicate : predicates) {
            if (predicate.test(valueToTest))
                return true;
        }
        return false;
    }

    /**
     * Returns {@code true} if all of the {@code predicates} returns
     * {@code true} for the given {@code valueToTest}, {@code false} when at
     * least one of the {@link Predicate}s return {@code false}.
     * <p>
     * See also {@link #isAnyPredicateTrue(Iterable, Object)} and
     * {@link #and(Iterable)}.
     */
    public static <T> boolean areAllPredicatesTrue(
            Iterable<Predicate<T>> predicates, T valueToTest) {
        for (Predicate<T> predicate : predicates) {
            if (!predicate.test(valueToTest))
                return false;
        }
        return true;
    }

    /**
     * Returns {@code true} if all of the {@code predicates} returns
     * {@code true} for the given {@code valueToTest}, {@code false} when at
     * least one of the {@link Predicate}s return {@code false}.
     */
    @SafeVarargs
    public static <T> boolean areAllPredicatesTrue(
            T valueToTest, Predicate<T>... predicates) {
        for (Predicate<T> predicate : predicates) {
            if (!predicate.test(valueToTest))
                return false;
        }
        return true;
    }
}
