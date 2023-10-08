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

import java.awt.*;
import java.util.Stack;

import static org.abego.commons.lang.StringUtil.escapeForHtml;

/**
 * A FormattedTextProcessor that builds HTML text fragments corresponding to
 * any given {@link FormattedText}.
 * <p>
 * Typically, you will use {@link #toHTML(FormattedText)} to get the HTML text
 * fragment corresponding to a given {@link FormattedText}. However, you may use
 * this class as a 'normal' {@link FormattedTextProcessor}, e.g. to build
 * HTML from multiple {@link FormattedText} instances. In that case the
 * {@link #build()} will return the resulting HTML text.
 */
public final class FormattedTextProcessorForHTML implements FormattedTextProcessor {

    private final StringBuilder text = new StringBuilder();
    private final Stack<Runnable> endAction = new Stack<>();
    private boolean isFormatted = false;
    private int boldLevel = 0;
    private int italicLevel = 0;

    private FormattedTextProcessorForHTML() {
    }

    public static FormattedTextProcessorForHTML newFormattedTextProcessorForHTML() {
        return new FormattedTextProcessorForHTML();
    }

    /**
     * Returns the HTML text corresponding to the given {@code formattedText}.
     */
    public static String toHTML(FormattedText formattedText) {
        FormattedTextProcessorForHTML builder = newFormattedTextProcessorForHTML();
        formattedText.processWith(builder);
        return builder.build();
    }

    @Override
    public FormattedTextProcessorForHTML text(String s) {
        text.append(escapeForHtml(s));
        return this;
    }

    @Override
    public FormattedTextProcessor beginBlock() {
        // unsupported block types just do nothing (at begin and end)
        atEndDoNothing();
        return this;
    }

    @Override
    public FormattedTextProcessorForHTML beginColor(Color color) {
        isFormatted = true;

        text.append(String.format("<font color=\"#%02x%02x%02x\">", //NON-NLS
                color.getRed(), color.getGreen(), color.getBlue()));
        atEndAppendText("</font>"); //NON-NLS
        return this;
    }

    @Override
    public FormattedTextProcessorForHTML beginBold() {
        isFormatted = true;
        if (boldLevel++ == 0) {
            text.append("<b>");
        }
        endAction.push(() -> {
            boldLevel--;
            if (boldLevel == 0) {
                text.append("</b>");
            }
        });
        return this;
    }

    @Override
    public FormattedTextProcessorForHTML beginItalic() {
        isFormatted = true;
        if (italicLevel++ == 0) {
            text.append("<i>");
        }
        endAction.push(() -> {
            italicLevel--;
            if (italicLevel == 0) {
                text.append("</i>");
            }
        });
        return this;
    }

    @Override
    public FormattedTextProcessorForHTML end() {
        if (endAction.empty()) {
            //noinspection DuplicateStringLiteralInspection
            throw new IllegalStateException(
                    "Extra end. begin and end must be balanced."); //NON-NLS
        }
        endAction.pop().run();
        return this;
    }

    public boolean isFormatted() {
        return isFormatted;
    }

    /**
     * Returns the HTML text fragment currently collected.
     * <p>
     * {@code #build()} may be called multiple times. Calls
     * to the {@link FormattedText}-defining methods ({@link #text(String)},
     * {@link #beginBold()}, {@link #end()}, ...) between subsequent builds are
     * additive, i.e. extend the previous HTML text fragment.
     */
    public String build() {
        if (!endAction.empty()) {
            //noinspection DuplicateStringLiteralInspection
            throw new IllegalStateException(
                    "Missing end. begin and end must be balanced."); //NON-NLS
        }
        return text.toString();
    }

    private void atEndDoNothing() {
        endAction.push(() -> {});
    }

    private void atEndAppendText(
            @SuppressWarnings("SameParameterValue") String textToAppend) {
        endAction.push(() -> this.text.append(textToAppend));
    }
}
