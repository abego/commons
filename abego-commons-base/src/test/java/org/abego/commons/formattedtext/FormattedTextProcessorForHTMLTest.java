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

import org.junit.jupiter.api.Test;

import static org.abego.commons.formattedtext.FormattedTextProcessorForHTML.newFormattedTextProcessorForHTML;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormattedTextProcessorForHTMLTest {
    @Test
    void toHTML() {
        FormattedText formattedText = FormattedTextTest.getFormattedTextSample();
        String html = FormattedTextProcessorForHTML.toHTML(formattedText);

        assertEquals(
                "foobar<b>baz</b><i>doo</i><font color=\"#ff0000\">colored</font>",
                html);
    }

    @Test
    void isFormatted() {
        FormattedTextProcessorForHTML processor = newFormattedTextProcessorForHTML();

        assertFalse(processor.isFormatted());

        processor.text("foo");

        assertFalse(processor.isFormatted());

        processor.beginBold();
        processor.text("bar");
        processor.end();

        assertTrue(processor.isFormatted());
    }

    @Test
    void unbalancedBlock() {
        FormattedTextProcessorForHTML processor = newFormattedTextProcessorForHTML();

        processor.beginBold();
        processor.text("bar");

        IllegalStateException e =
                assertThrows(IllegalStateException.class, processor::build);
        assertEquals(
                "Missing end. begin and end must be balanced.",
                e.getMessage());
    }

    @Test
    void extraEmd() {
        FormattedTextProcessorForHTML processor = newFormattedTextProcessorForHTML();

        IllegalStateException e =
                assertThrows(IllegalStateException.class, processor::end);
        assertEquals(
                "Extra end. begin and end must be balanced.",
                e.getMessage());
    }

}
