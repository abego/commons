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
import static org.abego.commons.lang.StringUtil.escaped;
import static org.abego.commons.lang.StringUtil.firstChar;
import static org.abego.commons.lang.StringUtil.hasText;
import static org.abego.commons.lang.StringUtil.isNullOrEmpty;
import static org.abego.commons.lang.StringUtil.join;
import static org.abego.commons.lang.StringUtil.joinWithEmptyStringForNull;
import static org.abego.commons.lang.StringUtil.lastChar;
import static org.abego.commons.lang.StringUtil.quoted;
import static org.abego.commons.lang.StringUtil.singleQuoted;
import static org.abego.commons.lang.StringUtil.singleQuotedStringWithoutEscapes;
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
        assertEquals("null", singleQuotedStringWithoutEscapes(null));
        assertEquals("'abc'", singleQuotedStringWithoutEscapes("abc"));
        assertEquals("'a\nb\\nc'", singleQuotedStringWithoutEscapes("a\nb\\nc"));
        assertEquals("''", singleQuotedStringWithoutEscapes(""));
        assertEquals("'3'", singleQuotedStringWithoutEscapes(3));
    }

    @Test
    void unescapeCharacters_ok() {
        assertEquals("abcnd", unescapeCharacters("a\\bc\\nd"));
        assertEquals("a\\.$d", unescapeCharacters("a\\\\\\.\\$d"));
        assertEquals("", unescapeCharacters(""));
    }

    @Test
    void join_javaDocSample() {
        String message = join("-", "Java", 3.14, null, true, '!');

        assertEquals("Java-3.14-null-true-!", message);
    }

    @Test
    void join_emptyNull_javaDocSample() {
        String message = joinWithEmptyStringForNull("-", "Java", 3.14, null, true, '!');

        assertEquals("Java-3.14--true-!", message);
    }


    @Test
    void quotedOk() {
        // null text
        assertEquals("Nil", quoted(null, "Nil", true));
        assertEquals("Nil", quoted(null, "Nil", false));
        assertEquals("Nil", quoted(null, "Nil"));
        assertEquals("null", quoted(null));

        // simple text
        assertEquals("'abc'", quoted("abc", "Nil", true));
        assertEquals("\"abc\"", quoted("abc", "Nil", false));
        assertEquals("\"abc\"", quoted("abc", "Nil"));
        assertEquals("\"abc\"", quoted("abc"));

        // text to escape
        String t = "a\b\f\n\r\t\\\'\"\u0080\u0099 Z";
        assertEquals("'a\\b\\f\\n\\r\\t\\\\\\'\"\\u0080\\u0099 Z'", quoted(t, "Nil", true));
        assertEquals("\"a\\b\\f\\n\\r\\t\\\\'\\\"\\u0080\\u0099 Z\"", quoted(t, "Nil", false));
        assertEquals("\"a\\b\\f\\n\\r\\t\\\\'\\\"\\u0080\\u0099 Z\"", quoted(t, "Nil"));
        assertEquals("\"a\\b\\f\\n\\r\\t\\\\'\\\"\\u0080\\u0099 Z\"", quoted(t));
    }

    @Test
    void singleQuotedOk() {
        // null text
        assertEquals("Nil", singleQuoted(null, "Nil"));
        assertEquals("null", singleQuoted(null));

        // simple text
        assertEquals("'abc'", singleQuoted("abc", "Nil"));
        assertEquals("'abc'", singleQuoted("abc"));

        // text to escape
        String t = "a\b\f\n\r\t\\\'\"\u0099Z";
        assertEquals("'a\\b\\f\\n\\r\\t\\\\\\'\"\\u0099Z'", singleQuoted(t, "Nil"));
        assertEquals("'a\\b\\f\\n\\r\\t\\\\\\'\"\\u0099Z'", singleQuoted(t));
    }

    @Test
    void escapedOk() {
        // null text
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> escaped(null));

        // simple text
        assertEquals("abc", escaped("abc"));

        // text to escape
        String t = "a\b\f\n\r\t\\\'\"\u0099Z";
        assertEquals("a\\b\\f\\n\\r\\t\\\\'\\\"\\u0099Z", escaped(t));
    }

    @Test
    void escapedCharOk() {
        assertEquals("a", escaped('a'));
        assertEquals("\\b", escaped('\b'));
        assertEquals("\\f", escaped('\f'));
        assertEquals("\\n", escaped('\n'));
        assertEquals("\\r", escaped('\r'));
        assertEquals("\\t", escaped('\t'));
        assertEquals("\\\\", escaped('\\'));
        assertEquals("\'", escaped('\''));
        assertEquals("\\\"", escaped('"'));
        assertEquals("\\u0099", escaped('\u0099'));
        assertEquals("Z", escaped('Z'));
    }


}