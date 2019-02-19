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

package org.abego.commons.util;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.abego.commons.util.CollectionUtil.singleQuotedStringListWithoutEscapes;
import static org.abego.commons.util.ListUtil.list;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CollectionUtilTest {

    private static final List<?> LIST_1 = list("a", 1, true, null);
    private static final String EXPECTED_1 = "'a', '1', 'true', null";
    /**
     * List, including a String containing a single quote
     */
    private static final List<String> LIST_2 = list("'", "a\nb\\nc");
    private static final String EXPECTED_2 = "''', 'a\nb\\nc'";

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, CollectionUtil::new);
    }

    @Test
    void singleQuotedStringList_noEscapes_ok() {
        assertEquals("", singleQuotedStringListWithoutEscapes(list()));
        assertEquals(EXPECTED_1, singleQuotedStringListWithoutEscapes(LIST_1));
        assertEquals(EXPECTED_2, singleQuotedStringListWithoutEscapes(LIST_2));
    }
}