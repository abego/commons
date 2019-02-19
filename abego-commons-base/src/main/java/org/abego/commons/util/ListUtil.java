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

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.abego.commons.lang.StringUtil.stringOrNull;


public class ListUtil {

    ListUtil() {
        throw new MustNotInstantiateException();
    }

    // --- Factories ---

    @SafeVarargs
    public static <C> List<C> list(C... array) {
        ArrayList<C> collection = new ArrayList<>();
        Collections.addAll(collection, array);
        return collection;
    }

    // --- Queries ---

    public static String nthItemAsStringOrNull(List<?> list, int i) {
        Object item = i >= 0 && i < list.size() ? list.get(i) : null;
        return stringOrNull(item);
    }
}
