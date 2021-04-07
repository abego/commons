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

package org.abego.commons.formattedtext;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormattedTextProcessorTest {

    @Test
    void smokeText() {
        FormattedText text = FormattedTextTest.getFormattedTextSample();
        FormattedTextProcessorSample processor = new FormattedTextProcessorSample();

        text.processWith(processor);

        assertEquals("foo{begin}bar{bold}baz{end}{italic}doo{end}{color java.awt.Color[r=255,g=0,b=0]}colored{end}{end}",
                processor.getText());
    }

    @Test
    void smokeTextUsingDefaults() {
        FormattedText text = FormattedTextTest.getFormattedTextSample();
        FormattedTextProcessorSampleUsingDefaults processor = new FormattedTextProcessorSampleUsingDefaults();

        text.processWith(processor);

        assertEquals("foo{begin}bar{begin}baz{end}{begin}doo{end}{begin}colored{end}{end}", processor.getText());
    }

    /**
     * A sample FormattedTextProcessor that does only override the required methods, not the default methods
     */
    static class FormattedTextProcessorSampleUsingDefaults implements FormattedTextProcessor {
        private final StringBuilder result = new StringBuilder();

        @Override
        public FormattedTextProcessor text(String text) {
            result.append(text);
            return this;
        }

        @Override
        public FormattedTextProcessor beginBlock() {
            return text("{begin}");
        }

        @Override
        public FormattedTextProcessor end() {
            return text("{end}");
        }

        public String getText() {
            return result.toString();
        }
    }

    /**
     * A sample FormattedTextProcessor that overrides all methods, including the default methods.
     */
    static class FormattedTextProcessorSample extends FormattedTextProcessorSampleUsingDefaults {

        @Override
        public FormattedTextProcessor beginColor(Color color) {
            return text("{color " + color + "}");
        }

        @Override
        public FormattedTextProcessor beginBold() {
            return text("{bold}");
        }

        @Override
        public FormattedTextProcessor beginItalic() {
            return text("{italic}");
        }
    }
}