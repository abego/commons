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

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.MILLISECOND;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilTest {
    @Test
    void smoketest() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.OCTOBER, 14, 13, 3, 42);
        cal.set(MILLISECOND, 123);
        Date date = cal.getTime();

        assertEquals("2023-10-14T13:03:42.123", DateUtil.isoDateTimeMillis(date));
        assertEquals("2023-10-14T13:03:42", DateUtil.isoDateTime(date));
        assertEquals("2023-10-14", DateUtil.isoDate(date));
        assertEquals("13:03:42", DateUtil.isoTime(date));
        assertEquals("13:03:42.123", DateUtil.isoTimeMillis(date));
    }
}
