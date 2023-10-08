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

package org.abego.commons.formattedtext;

import org.abego.commons.formattedtext.FormattedTextProcessorTest.FormattedTextProcessorSampleUsingDefaults;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.range.IntRange;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.abego.commons.range.IntRangeDefault.newIntRange;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormattedTextUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, FormattedTextUtil::new);
    }


    @Test
    void toFormattedTextOK() {
        FormattedText ft = FormattedTextUtil.toFormattedText("foo");

        FormattedTextProcessorSampleUsingDefaults processor =
                new FormattedTextProcessorSampleUsingDefaults();
        ft.processWith(processor);

        assertEquals("foo", processor.getText());
    }

    @Test
    void withStyledRanges() {
        String text = "foobar";
        List<IntRange> ranges = new ArrayList<>();
        ranges.add(newIntRange(3, 6));
        Color color = Color.yellow;

        FormattedText ft = FormattedTextUtil.withStyledRanges(
                text, ranges, color, FormattedText.FontStyle.BOLD);

        assertEquals("foo<b><font color=\"#ffff00\">bar</font></b>",
                FormattedTextProcessorForHTML.toHTML(ft));
    }

    @Test
    void withStyledRangesIfRequired() {
        String text = "foobar";
        List<IntRange> ranges = new ArrayList<>();
        ranges.add(newIntRange(1, 3));

        Object result = FormattedTextUtil.withStyledRangesIfRequired(
                text, ranges, null, FormattedText.FontStyle.ITALIC);

        assertTrue(result instanceof FormattedText);
        assertEquals("f<i>oo</i>bar",
                FormattedTextProcessorForHTML.toHTML((FormattedText) result));
    }

    @Test
    void withStyledRangesIfRequired_notFormatted() {
        String text = "foobar";
        List<IntRange> ranges = new ArrayList<>();

        Object result = FormattedTextUtil.withStyledRangesIfRequired(
                text, ranges, null, FormattedText.FontStyle.ITALIC);

        assertTrue(result instanceof String);
        assertEquals("foobar", result);
    }

}
