/*
 * MIT License
 *
 * Copyright (c) 2023 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.util;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SetUtilTest {

    private static void assertEqualsIgnoringOrder(String expectedAsText, Set<String> actual) {
        String actualAsTest = actual.size() + "\n"
                + actual.stream().sorted().collect(Collectors.joining("\n"));
        assertEquals(expectedAsText, actualAsTest);
    }

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, SetUtil::new);
    }


    @Test
    void asSet() {
        assertEqualsIgnoringOrder("0\n",
                SetUtil.asSet());

        assertEqualsIgnoringOrder("1\nA",
                SetUtil.asSet("A"));

        assertEqualsIgnoringOrder("3\nA\nB\nC",
                SetUtil.asSet("A", "B", "C"));

        assertEqualsIgnoringOrder("3\nA\nB\nC",
                SetUtil.asSet("A", "B", "C", "B", "A", "A"));
    }
}
