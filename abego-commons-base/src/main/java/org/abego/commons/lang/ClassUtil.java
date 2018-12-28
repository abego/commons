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


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.abego.commons.io.InputStreamUtil.textOf;
import static org.abego.commons.lang.exception.MustNotInstantiateException.throwMustNotInstantiate;
import static org.abego.commons.lang.exception.UncheckedException.uncheckedException;

public class ClassUtil {

    ClassUtil() {
        throwMustNotInstantiate();
    }

    /**
     * Return the text of the resource <code>resourceName</code> of
     * <code>class_</code>, assuming the text is encoded with the {@link Charset}
     * <code>charset</code>.
     */
    public static String textOfResource(
            Class<?> class_, String resourceName, Charset charset) {
        try {
            return textOf(class_.getResourceAsStream(resourceName), charset);
        } catch (Exception e) {
            throw uncheckedException(String.format(
                    "Error when accessing resource `%s` of class `%s`.", // NON-NLS
                    resourceName, class_.getName()), e);
        }
    }

    /**
     * Return the text of the resource <code>resourceName</code> of
     * <code>class_</code>, assuming the text is UTF-8 encoded.
     */
    public static String textOfResource(Class<?> class_, String resourceName) {
        return textOfResource(class_, resourceName, StandardCharsets.UTF_8);
    }

}
