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

package org.abego.commons.lang;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import static org.abego.commons.lang.ObjectUtil.ignore;
import static org.abego.commons.lang.StringUtil.firstChar;
import static org.abego.commons.lang.StringUtil.hasText;
import static org.abego.commons.lang.StringUtil.isNullOrEmpty;
import static org.abego.commons.lang.StringUtil.lastChar;
import static org.abego.commons.lang.StringUtil.singleQuotedString_noEscapes;
import static org.abego.commons.lang.StringUtil.string;
import static org.abego.commons.lang.StringUtil.stringOrDefault;
import static org.abego.commons.lang.StringUtil.unescapeCharacters;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, StringUtil::new);
    }

    @Test
    void isNullOrEmpty_ok() {
        //noinspection ConstantConditions
        assertTrue(isNullOrEmpty(null));
        assertTrue(isNullOrEmpty(""));
        assertFalse(isNullOrEmpty("a"));
    }

    @Test
    void hasText_ok() {
        //noinspection ConstantConditions
        assertFalse(hasText(null));
        assertFalse(hasText(""));
        assertTrue(hasText("a"));
    }

    @Test
    void stringOrDefault_String() {
        assertEquals("b", stringOrDefault(null, "b"));
        assertEquals("b", stringOrDefault("", "b"));
        assertEquals("a", stringOrDefault("a", "b"));
    }

    @Test
    void stringOrDefault_Supplier() {
        assertEquals("b", stringOrDefault(null, () -> "b"));
        assertEquals("b", stringOrDefault("", () -> "b"));
        assertEquals("a", stringOrDefault("a", () -> "b"));
    }

    @Test
    void string_ok() {
        assertEquals("", string(null));
        assertEquals("", string(""));
        assertEquals("a", string("a"));
    }

    @Test
    void firstChar_ok() {
        assertEquals('a', firstChar("a"));
        assertEquals('a', firstChar("abc"));
        assertThrows(Exception.class, () -> ignore(firstChar("")));
    }

    @Test
    void lastChar_ok() {
        assertEquals('a', lastChar("a"));
        assertEquals('c', lastChar("abc"));
        assertThrows(Exception.class, () -> ignore(lastChar("")));
    }

    @Test
    void singleQuotedString_noEscapes_ok() {
        assertEquals("null", singleQuotedString_noEscapes(null));
        assertEquals("'abc'", singleQuotedString_noEscapes("abc"));
        assertEquals("'a\nb\\nc'", singleQuotedString_noEscapes("a\nb\\nc"));
        assertEquals("''", singleQuotedString_noEscapes(""));
        assertEquals("'3'", singleQuotedString_noEscapes(3));
    }

    @Test
    void unescapeCharacters_ok() {
        assertEquals("abcnd", unescapeCharacters("a\\bc\\nd"));
        assertEquals("a\\.$d", unescapeCharacters("a\\\\\\.\\$d"));
        assertEquals("", unescapeCharacters(""));
    }
}