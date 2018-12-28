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

package org.abego.commons.util;

import java.util.Collection;

import static org.abego.commons.lang.StringUtil.singleQuotedString_noEscapes;
import static org.abego.commons.lang.exception.MustNotInstantiateException.throwMustNotInstantiate;

public class CollectionUtil {

    CollectionUtil() {
        throwMustNotInstantiate();
    }

    /**
     * Return a comma separated list of single-quoted strings, one string per
     * item in {@code items}.
     *
     * <p>The text between the quotes is not escaped in any way.</p>
     *
     * <p>See also {@link org.abego.commons.lang.StringUtil#singleQuotedString_noEscapes(Object)}.</p>
     */
    public static String singleQuotedStringList_noEscapes(Collection<?> items) {
        StringBuilder sb = new StringBuilder();
        for (Object item : items) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(singleQuotedString_noEscapes(item));
        }
        return sb.toString();
    }
}
