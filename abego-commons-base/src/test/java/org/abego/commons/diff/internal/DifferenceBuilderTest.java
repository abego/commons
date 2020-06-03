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

package org.abego.commons.diff.internal;

import org.junit.jupiter.api.Test;

import static org.abego.commons.range.IntRangeBuilder.newIntRangeBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DifferenceBuilderTest {

    @Test
    void constructor_2Args() {
        DifferenceBuilder builder = new DifferenceBuilder(1, 5);

        assertEquals(newIntRangeBuilder(1, 1), builder.getRangeInABuilder());
        assertEquals(newIntRangeBuilder(5, 5), builder.getRangeInBBuilder());
    }

    @Test
    void constructor_4Args() {
        DifferenceBuilder builder = new DifferenceBuilder(1, 3, 5, 8);

        assertEquals(newIntRangeBuilder(1, 3), builder.getRangeInABuilder());
        assertEquals(newIntRangeBuilder(5, 8), builder.getRangeInBBuilder());
    }

}