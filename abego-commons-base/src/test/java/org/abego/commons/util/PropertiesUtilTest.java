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

package org.abego.commons.util;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import static org.abego.commons.util.PropertiesUtil.toPropertyKey;
import static org.abego.commons.util.PropertiesUtil.toPropertyValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, PropertiesUtil::new);
    }


    @Test
    void asPropertyKeyOK() {
        assertEquals("", toPropertyKey(""));
        assertEquals("foo", toPropertyKey("foo"));
        assertEquals("123", toPropertyKey("123"));
        assertEquals("a\\:b", toPropertyKey("a:b"));
        assertEquals("a\\=b", toPropertyKey("a=b"));
        assertEquals("a\\:\\=b", toPropertyKey("a:=b"));
        assertEquals("a\\:\\=b", toPropertyKey("a:=b"));
        assertEquals("foo\\ ", toPropertyKey("foo "));
        assertEquals("\\ foo\\ ", toPropertyKey(" foo "));
        assertEquals("\\\t\\\tfoo", toPropertyKey("\t\tfoo"));
    }

    @Test
    void asPropertyValueOK() {
        assertEquals("", toPropertyValue(""));
        assertEquals("foo", toPropertyValue("foo"));
        assertEquals("\\ foo", toPropertyValue(" foo"));
        assertEquals("foo ", toPropertyValue("foo "));
        assertEquals("foo\\nbar", toPropertyValue("foo\nbar"));
        assertEquals("foo\\tbar", toPropertyValue("foo\tbar"));
        assertEquals("foo\\\\bar", toPropertyValue("foo\\bar"));
        assertEquals("\\   foo", toPropertyValue("   foo"));
        assertEquals("#foo", toPropertyValue("#foo"));
    }
}