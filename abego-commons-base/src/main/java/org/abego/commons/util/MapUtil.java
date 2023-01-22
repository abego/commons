/*
 * MIT License
 *
 * Copyright (c) 2022 Udo Borkowski, (ub@abego.org)
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
import org.eclipse.jdt.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static org.abego.commons.lang.ClassUtil.classNameOrNull;

public final class MapUtil {

    MapUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Returns a new Map created for the given list of (name, value) pairs.
     * <p>
     * The name must be a String, the value can be any value. Names must not
     * be redefined.
     */
    public static Map<String, @Nullable Object> getStringObjectMap(
            String name,
            @Nullable Object value,
            @Nullable Object... moreNamesAndValues) {
        Map<String, @Nullable Object> map = new HashMap<>();

        map.put(name, value);

        int n = moreNamesAndValues.length;
        if (n % 2 == 1) {
            //noinspection DuplicateStringLiteralInspection // Use in test
            throw new IllegalArgumentException(
                    "namesAndValues must contain an even number of objects, as we expect (name,value) pairs"); // NON-NLS
        }

        for (int i = 0; i < n; i += 2) {
            @Nullable Object key = moreNamesAndValues[i];
            @Nullable Object val = moreNamesAndValues[i + 1];
            if (!(key instanceof String)) {
                throw new IllegalArgumentException(
                        String.format("Expected name to be a String, got %s", //NON-NLS
                                classNameOrNull(key)));
            }

            if (map.containsKey(key)) {
                throw new IllegalArgumentException(
                        String.format("Name '%s' already defined", key)); //NON-NLS

            }
            map.put((String) key, val);
        }
        return map;
    }

}
