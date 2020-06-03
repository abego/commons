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

import java.util.Locale;

import static org.abego.commons.util.LocaleUtil.DEFAULT_LOCALE;
import static org.abego.commons.util.LocaleUtil.camelCased;
import static org.abego.commons.util.LocaleUtil.dashCased;
import static org.abego.commons.util.LocaleUtil.localeOrDefault;
import static org.abego.commons.util.LocaleUtil.snakeCased;
import static org.abego.commons.util.LocaleUtil.snakeUpperCased;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocaleUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, LocaleUtil::new);
    }

    @Test
    void camelCasedOK() {
        assertEquals("überDenMenschen", camelCased("Über den Menschen", Locale.GERMAN));
    }

    @Test
    void dashCasedOK() {
        assertEquals("über-den-menschen", dashCased("Über den Menschen", Locale.GERMAN));
    }

    @Test
    void snakeCasedOK() {
        assertEquals("über_den_menschen", snakeCased("Über den Menschen", Locale.GERMAN));
    }

    @Test
    void snakeUpperCasedOK() {
        assertEquals("ÜBER_DEN_MENSCHEN", snakeUpperCased("Über den Menschen", Locale.GERMAN));
    }

    @Test
    void localeOrDefaultOK() {
        assertEquals(DEFAULT_LOCALE, localeOrDefault(null));
        assertEquals(Locale.GERMAN, localeOrDefault(Locale.GERMAN));
    }

}