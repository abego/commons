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

package org.abego.commons.lang;


import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class IterableUtil {
    private static final String SEPARATOR_DEFAULT = "";
    private static final String EMPTY_TEXT_DEFAULT = "";
    private static final String NULL_TEXT_DEFAULT = "null";

    IterableUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Return the texts of the <code>items</code>, stringed together to a single
     * text.
     *
     * <p>Use <code>textOfItem(item)</code> as the text for an individual item.
     * When an item is <code>null</code> use the <code>nullValueText</code>.</p>
     *
     * <p>Use <code>separator</code> to separate subsequent item texts.</p>
     *
     * @param items         the items those texts should be joined. Elements in
     *                      <code>items</code> may be <code>null</code>.
     * @param separator     the text used to separate subsequent item texts.
     *                      [Default: ""]
     * @param emptyText     the text used when items contains no item.
     *                      [Default: ""]
     * @param textOfItem    <code>textOfItem(item)</code> returns the text of
     *                      the given <code>item</code>.
     *                      [Default: Object::toString]
     * @param nullValueText the text to use as an item's text when the item is
     *                      <code>null</code>.
     *                      [Default: "null"]
     * @return the joined texts of the items
     */
    public static <T> String textOf(
            Iterable<T> items,
            CharSequence separator,
            String emptyText,
            Function<T, String> textOfItem,
            CharSequence nullValueText) {
        StringBuilder sb = new StringBuilder();
        boolean addSeparator = false;
        boolean isEmpty = true;
        for (T item : items) {
            isEmpty = false;
            // Add a separator, but not before the first item
            if (addSeparator)
                sb.append(separator);
            else
                addSeparator = true;

            sb.append(item == null ? nullValueText : textOfItem.apply(item));
        }
        return isEmpty ? emptyText : sb.toString();
    }

    /**
     * Return the texts of the <code>items</code>, stringed together to a single
     * text.
     *
     * <p>Convenience overload of {@link #textOf(Iterable, CharSequence, String, Function, CharSequence)},
     * using default values for omitted parameter.</p>
     */
    public static <T> String textOf(Iterable<T> items) {
        return textOf(items, SEPARATOR_DEFAULT, EMPTY_TEXT_DEFAULT, Object::toString, NULL_TEXT_DEFAULT);
    }

    /**
     * Return the texts of the <code>items</code>, stringed together to a single
     * text.
     *
     * <p>Convenience overload of {@link #textOf(Iterable, CharSequence, String, Function, CharSequence)},
     * using default values for omitted parameter.</p>
     */
    public static <T> String textOf(
            Iterable<T> items,
            CharSequence separator) {
        return textOf(items, separator, EMPTY_TEXT_DEFAULT, Object::toString, NULL_TEXT_DEFAULT);
    }

    /**
     * Return the texts of the <code>items</code>, stringed together to a single
     * text.
     *
     * <p>Convenience overload of {@link #textOf(Iterable, CharSequence, String, Function, CharSequence)},
     * using default values for omitted parameter.</p>
     */
    public static <T> String textOf(
            Iterable<T> items,
            CharSequence separator,
            Function<T, String> textOfItem) {
        return textOf(items, separator, EMPTY_TEXT_DEFAULT, textOfItem, NULL_TEXT_DEFAULT);
    }

    public static boolean areEqual(
            Iterable<?> iterable1, Iterable<?> iterable2) {

        if (iterable1 == iterable2) {
            return true;
        }

        Iterator<?> i1 = iterable1.iterator();
        Iterator<?> i2 = iterable2.iterator();

        while (i1.hasNext()) {
            if (!i2.hasNext() || !Objects.equals(i1.next(), i2.next())) {
                return false;
            }
        }

        return !i2.hasNext();
    }

    /**
     * Return the hash code value for the <code>iterable</code>.
     *
     * <p>The hash code is defined to be the result of the
     * following calculation:
     * <pre>
     *         int hashCode = 1;
     *         for (Object e : iterable) {
     *             hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
     *         }
     * </pre>
     */
    public static int hashCodeForIterable(Iterable<?> iterable) {
        int hashCode = 1;
        for (Object e : iterable) {
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }
}
