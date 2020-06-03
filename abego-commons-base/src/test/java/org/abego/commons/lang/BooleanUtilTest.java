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
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.abego.commons.util.ListUtil.list;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BooleanUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, BooleanUtil::new);
    }

    @Test
    void isFalsyOK() {
        assertTrue(BooleanUtil.isFalsy(null));
        assertTrue(BooleanUtil.isFalsy(false));
        assertTrue(BooleanUtil.isFalsy(""));
        assertTrue(BooleanUtil.isFalsy(new String[0]));
        assertTrue(BooleanUtil.isFalsy(new ArrayList<>()));
        assertTrue(BooleanUtil.isFalsy(0));
        assertTrue(BooleanUtil.isFalsy(0.0f));
        assertTrue(BooleanUtil.isFalsy(0.0));

        assertFalse(BooleanUtil.isFalsy(new Object()));
        assertFalse(BooleanUtil.isFalsy(true));
        assertFalse(BooleanUtil.isFalsy("hi"));
        assertFalse(BooleanUtil.isFalsy(new String[1]));
        assertFalse(BooleanUtil.isFalsy(list("hi")));
        assertFalse(BooleanUtil.isFalsy(1));
        assertFalse(BooleanUtil.isFalsy(1.1f));
        assertFalse(BooleanUtil.isFalsy(1.2));
    }

    @Test
    void isTruthyOK() {
        assertTrue(BooleanUtil.isTruthy(new Object()));
        assertTrue(BooleanUtil.isTruthy(true));
        assertTrue(BooleanUtil.isTruthy("hi"));
        assertTrue(BooleanUtil.isTruthy(new String[1]));
        assertTrue(BooleanUtil.isTruthy(list("hi")));
        assertTrue(BooleanUtil.isTruthy(1));
        assertTrue(BooleanUtil.isTruthy(1.1f));
        assertTrue(BooleanUtil.isTruthy(1.2));

        assertFalse(BooleanUtil.isTruthy(null));
        assertFalse(BooleanUtil.isTruthy(false));
        assertFalse(BooleanUtil.isTruthy(""));
        assertFalse(BooleanUtil.isTruthy(new String[0]));
        assertFalse(BooleanUtil.isTruthy(new ArrayList<>()));
        assertFalse(BooleanUtil.isTruthy(0));
        assertFalse(BooleanUtil.isTruthy(0.0f));
        assertFalse(BooleanUtil.isTruthy(0.0));


    }
}