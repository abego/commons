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
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.abego.commons.util.ListUtil.toList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PredicateUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, PredicateUtil::new);
    }


    @Test
    void alwaysTrue() {
        assertTrue(PredicateUtil.alwaysTrue().test(true));
        assertTrue(PredicateUtil.alwaysTrue().test(false));
        assertTrue(PredicateUtil.alwaysTrue().test(0));
        assertTrue(PredicateUtil.alwaysTrue().test(123));
        assertTrue(PredicateUtil.alwaysTrue().test(""));
        assertTrue(PredicateUtil.alwaysTrue().test("foo"));
        assertTrue(PredicateUtil.alwaysTrue().test(null));
    }

    @Test
    void alwaysFalse() {
        assertFalse(PredicateUtil.alwaysFalse().test(true));
        assertFalse(PredicateUtil.alwaysFalse().test(false));
        assertFalse(PredicateUtil.alwaysFalse().test(0));
        assertFalse(PredicateUtil.alwaysFalse().test(123));
        assertFalse(PredicateUtil.alwaysFalse().test(""));
        assertFalse(PredicateUtil.alwaysFalse().test("foo"));
        assertFalse(PredicateUtil.alwaysFalse().test(null));
    }

    @Test
    void and() {
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isDividableByThree = n -> n % 3 == 0;
        Iterable<Predicate<Integer>> predicates =
                toList(isEven, isDividableByThree);

        Predicate<Integer> isDividableBySix = PredicateUtil.and(predicates);

        assertTrue(isDividableBySix.test(0));
        assertFalse(isDividableBySix.test(1));
        assertFalse(isDividableBySix.test(2));
        assertFalse(isDividableBySix.test(3));
        assertFalse(isDividableBySix.test(4));
        assertFalse(isDividableBySix.test(5));
        assertTrue(isDividableBySix.test(6));
        assertFalse(isDividableBySix.test(7));
    }

    @Test
    void or() {
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isDividableByThree = n -> n % 3 == 0;
        Iterable<Predicate<Integer>> predicates =
                toList(isEven, isDividableByThree);

        Predicate<Integer> isEvenOrDividableByThree =
                PredicateUtil.or(predicates);

        assertTrue(isEvenOrDividableByThree.test(0));
        assertFalse(isEvenOrDividableByThree.test(1));
        assertTrue(isEvenOrDividableByThree.test(2));
        assertTrue(isEvenOrDividableByThree.test(3));
        assertTrue(isEvenOrDividableByThree.test(4));
        assertFalse(isEvenOrDividableByThree.test(5));
        assertTrue(isEvenOrDividableByThree.test(6));
        assertFalse(isEvenOrDividableByThree.test(7));
    }
}
