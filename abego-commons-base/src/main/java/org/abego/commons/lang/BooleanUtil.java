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

package org.abego.commons.lang;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
final public class BooleanUtil {

    BooleanUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Return <code>true</code> when the <code>object</code> is <em>falsy</em>,
     * <code>false</code> otherwise.
     *
     * <p>An object is <em>falsy</em> when it is
     * <ul>
     * <li><code>null</code>, or</li>
     * <li>a {@link Boolean} and <code>false</code>, or</li>
     * <li>an empty String, or</li>
     * <li>an empty array, or</li>
     * <li>an {@link Iterable} with no items, or</li>
     * <li>a Number with a value <code>0</code> or <code>NaN</code>.</li>
     * </ul>
     */
    public static boolean isFalsy(@Nullable Object object) {

        if (object == null) {
            return true;
        }

        if (object instanceof Boolean) {
            return !(Boolean) object;
        }

        if (object.getClass().isArray()) {
            return ((Object[]) object).length == 0;
        }

        if (object instanceof String) {
            return ((String) object).isEmpty();
        }

        if (object instanceof Iterable) {
            return !((Iterable<?>) object).iterator().hasNext();
        }

        if (object instanceof Number) {
            double n = ((Number) object).doubleValue();
            return n == 0.0 || Double.isNaN(n);
        }

        return false;
    }

    /**
     * Return <code>true</code> when the <code>object</code> is <em>truthy</em>,
     * <code>false</code> otherwise.
     *
     * <p>An object is <em>truthy</em> when it is not <em>falsy</em>
     * ({@link #isFalsy(Object)}).
     */
    public static boolean isTruthy(@Nullable Object object) {
        return !isFalsy(object);
    }

}
