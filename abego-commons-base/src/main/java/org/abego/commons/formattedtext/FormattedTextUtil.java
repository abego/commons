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

import org.abego.commons.formattedtext.FormattedText.FontStyle;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.range.IntRange;
import org.eclipse.jdt.annotation.Nullable;

import java.awt.*;

public final class FormattedTextUtil {

    FormattedTextUtil() {
        throw new MustNotInstantiateException();
    }

    public static FormattedText toFormattedText(String text) {
        return p -> p.text(text);
    }

    private static boolean allRangesAreEmpty(Iterable<IntRange> ranges) {
        for (IntRange range : ranges) {
            if (!range.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a {@link FormattedText} version of the given {@code text}, with
     * the {@code ranges} of the text formatted as defined by the {@code color}
     * and the {@code styles}
     **/
    public static FormattedText withStyledRanges(
            String text, Iterable<IntRange> ranges, @Nullable Color color, FontStyle... styles) {
        return p -> {
            int i = 0;
            boolean bold = false;
            boolean italic = false;
            for (FontStyle style : styles) {
                if (style == FontStyle.BOLD) {
                    bold = true;
                } else if (style == FontStyle.ITALIC) {
                    italic = true;
                }
            }
            for (IntRange range : ranges) {
                int s = range.getStart();
                int e = range.getEnd();
                if (s < e) {
                    if (i < s) {
                        p.text(text.substring(i, s));
                    }
                    if (bold) {
                        p.beginBold();
                    }
                    if (italic) {
                        p.beginItalic();
                    }
                    if (color != null) {
                        p.beginColor(color);
                    }
                    p.text(text.substring(s, e));
                    if (color != null) {
                        p.end();
                    }
                    if (italic) {
                        p.end();
                    }
                    if (bold) {
                        p.end();
                    }
                    i = e;
                }
            }
            if (i < text.length()) {
                p.text(text.substring(i));
            }
        };
    }


    /**
     * As {@link #withStyledRanges(String, Iterable, Color, FontStyle...)}, but
     * returns the unchanged {@code text} (as a {@link String}) when no range
     * needs to formatted, i.e. all text is plain text.
     *
     * @return {@link String} or {@link FormattedText}
     */
    public static Object withStyledRangesIfRequired(
            String text, Iterable<IntRange> ranges, @Nullable Color color, FontStyle... styles) {
        return allRangesAreEmpty(ranges)
                ? text
                : withStyledRanges(text, ranges, color, styles);
    }

}
