/*
 * MIT License
 *
 * Copyright (c) 2018 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.javalang;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import static org.abego.commons.javalang.JavaLangUtil.isJavaIdentifier;
import static org.abego.commons.javalang.JavaLangUtil.isValidJavaIdentifierCharButNotAtStart;
import static org.abego.commons.javalang.JavaLangUtil.toJavaIdentifier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaLangUtilTest {

    private static final String IDENTIFIER_SAMPLE = "SomeName"; // NON-NLS

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, JavaLangUtil::new);
    }

    @Test
    void toJavaIdentifier_ok() {
        assertEquals(IDENTIFIER_SAMPLE, toJavaIdentifier(IDENTIFIER_SAMPLE));
    }

    @Test
    void toJavaIdentifier_withSpecialCharacters() {
        assertEquals("a_b_c_", toJavaIdentifier("a.b-c*")); // NON-NLS
    }

    @Test
    void toJavaIdentifier_nonIDStart() {
        assertEquals("_dot", toJavaIdentifier(".dot")); // NON-NLS
    }

    @Test
    void toJavaIdentifier_startWithDigit() {
        assertEquals("_123", toJavaIdentifier("123")); // NON-NLS
    }

    @Test
    void toJavaIdentifier_emptyString_() {
        assertEquals("_", toJavaIdentifier("")); // NON-NLS
    }

    @Test
    void isValidJavaIdentifierCharButNotAtStart_ok() {
        assertTrue(isValidJavaIdentifierCharButNotAtStart('9')); // NON-NLS
        assertFalse(isValidJavaIdentifierCharButNotAtStart('?')); // NON-NLS
        assertFalse(isValidJavaIdentifierCharButNotAtStart('a')); // NON-NLS
    }

    @Test
    void isJavaIdentifier_ok() {
        assertTrue(isJavaIdentifier("abc")); // NON-NLS
        assertTrue(isJavaIdentifier("a2")); // NON-NLS
        assertTrue(isJavaIdentifier("_a")); // NON-NLS

        assertFalse(isJavaIdentifier("")); // NON-NLS
        assertFalse(isJavaIdentifier("1")); // NON-NLS
        assertFalse(isJavaIdentifier("?")); // NON-NLS
        assertFalse(isJavaIdentifier("ab?")); // NON-NLS
    }
}
