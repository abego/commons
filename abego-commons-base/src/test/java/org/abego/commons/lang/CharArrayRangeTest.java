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

import org.junit.jupiter.api.Test;

import static org.abego.commons.TestData.SAMPLE_TEXT_CHAR_0;
import static org.abego.commons.TestData.SAMPLE_TEXT_CHAR_1;
import static org.abego.commons.TestData.SAMPLE_TEXT_CHAR_2;
import static org.abego.commons.TestData.SAMPLE_TEXT_SUBSTRING_1_2;
import static org.abego.commons.lang.CharArrayRange.charArrayRange;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharArrayRangeTest {

    @Test
    void case_happyPath() {
        char[] chars = new char[]{SAMPLE_TEXT_CHAR_0, SAMPLE_TEXT_CHAR_1, SAMPLE_TEXT_CHAR_2};

        CharArrayRange ca = charArrayRange(chars, 1, 2);

        assertEquals(chars, ca.charArray());
        assertEquals(1, ca.startOffset());
        assertEquals(2, ca.length());
        assertEquals(SAMPLE_TEXT_SUBSTRING_1_2, ca.text());
        assertEquals(SAMPLE_TEXT_SUBSTRING_1_2, ca.toString());
    }

    @Test
    void case_emptyRange() {
        char[] chars = new char[]{};

        CharArrayRange ca = charArrayRange(chars, 0, 0);

        assertEquals("", ca.text());
    }

    @Test
    void charArrayRange_startOffsetNegative() {
        char[] chars = new char[]{};

        assertThrows(IllegalArgumentException.class, () ->
                charArrayRange(chars, -1, 0));
    }

    @Test
    void charArrayRange_lengthNegative() {
        char[] chars = new char[]{};

        assertThrows(IllegalArgumentException.class, () ->
                charArrayRange(chars, 0, -1));
    }
}