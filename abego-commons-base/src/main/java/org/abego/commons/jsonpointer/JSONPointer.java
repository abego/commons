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

package org.abego.commons.jsonpointer;

import org.abego.commons.lang.ArrayUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static org.abego.commons.lang.ClassUtil.classNameOrNull;

@SuppressWarnings({"HardCodedStringLiteral"})
public final class JSONPointer implements UnaryOperator<@Nullable Object> {
    static final String UNSUPPORTED_FOR_ROOT = "Unsupported for root JSON Pointer";
    static final char TOKEN_PREFIX = '/';
    private final String pointer;
    private final @NonNull String[] tokens;

    private JSONPointer(String pointer) {
        this.pointer = pointer;
        this.tokens = toTokens(pointer);
    }

    /**
     * Return the object given by the {@code pointer} string, relative to the {@code root}.
     *
     * <p>
     * <b>JSON Pointer Examples (taken from RFC 6901)</b>
     * Given the JSON document
     * <pre>
     *    {
     *       "foo": ["bar", "baz"],
     *       "": 0,
     *       "a/b": 1,
     *       "c%d": 2,
     *       "e^f": 3,
     *       "g|h": 4,
     *       "i\\j": 5,
     *       "k\"l": 6,
     *       " ": 7,
     *       "m~n": 8
     *    }
     * </pre>
     * The following JSON Pointers evaluate to the accompanying values:
     * <table summary="JSON Pointer Examples">
     * <tr><th>JSON Pointer</th><th>Value</th></tr>
     * <tr><td>""</td><td>// the whole document</td></tr>
     * <tr><td>"/foo"</td><td>["bar", "baz"]</td></tr>
     * <tr><td>"/foo/0"</td><td>"bar"</td></tr>
     * <tr><td>"/"</td><td>0</td></tr>
     * <tr><td>"/a~1b"</td><td>1</td></tr>
     * <tr><td>"/c%d"</td><td>2</td></tr>
     * <tr><td>"/e^f"</td><td>3</td></tr>
     * <tr><td>"/g|h"</td><td>4</td></tr>
     * <tr><td>"/i\\j"</td><td>5</td></tr>
     * <tr><td>"/k\"l"</td><td>6</td></tr>
     * <tr><td>"/ "</td><td>7</td></tr>
     * <tr><td>"/m~0n"</td><td>8</td></tr>
     * </table>
     *
     * @param jsonPointer a JSON Pointer, as defined in RFC 6901 
     *                    (<a href="https://tools.ietf.org/html/rfc6901">https://tools.ietf.org/html/rfc6901</a>)
     */
    @Nullable
    public static Object referencedValue(Object root, String jsonPointer) {
        return referencedValue(root, toTokens(jsonPointer), jsonPointer);
    }

    private static @Nullable Object referencedValue(@Nullable Object root, @NonNull String[] tokens, String jsonPointer) {
        @Nullable Object result = root;
        for (String token : tokens) {
            result = processToken(result, token, jsonPointer);
        }
        return result;
    }

    private static @NonNull String[] toTokens(String jsonPointer) {
        return jsonPointer.isEmpty()
                ? new String[0]
                : nonEmptyJSONPointerToTokens(jsonPointer);
    }

    private static @NonNull String[] nonEmptyJSONPointerToTokens(String jsonPointer) {
        if (!jsonPointer.startsWith("/"))
            throw newMissingRootSlash(jsonPointer);

        // separate the pointer in the individual navigational tokens
        // (but skip the initial '/')
        @NonNull String[] steps = jsonPointer.substring(1).split("/", -1);
        @NonNull String[] tokens = new @NonNull String[steps.length];
        for (int i = 0; i < steps.length; i++) {
            tokens[i] = unescape(steps[i]);
        }
        return tokens;
    }

    private static String unescape(String escapedToken) {
        return escapedToken.replaceAll("~1", "/").replaceAll("~0", "~");
    }

    @SuppressWarnings("unchecked")
    private static @Nullable Object processToken(@Nullable Object data, String token, String jsonPointer) {
        if (data instanceof Map) {

            Map<@Nullable Object, @Nullable Object> map = (Map<@Nullable Object, @Nullable Object>) data;
            if (!map.containsKey(token)) {
                throw newMissingKeyException(token, jsonPointer);
            }
            return map.get(token);

        } else if (data instanceof Object[]) {

            return getArrayItem((@Nullable Object[]) data, token, jsonPointer);

        } else if (data instanceof List) {
            // List is also an "Iterable" but we handle it specially because
            // we can use `List#get(int)` to access the list's its i-th item.
            // For an iterable we need to iterate and count to find the i-th
            // item. This is typically slower than the `get`.

            return getListItem((List<Object>) data, token, jsonPointer);

        } else if (data instanceof Iterable) {

            return getItemFromIterable((Iterable<@Nullable Object>) data, token, jsonPointer);

        } else {

            throw new IllegalArgumentException(
                    String.format("Expected Map, array or Iterable, got: %s", classNameOrNull(data)));
        }
    }

    private static @Nullable Object getItemFromIterable(Iterable<@Nullable Object> iterable, String indexString, String jsonPointer) {

        int index = parseIndex(indexString, jsonPointer);
        if (index < 0)
            throw newNegativeIndexException(index, jsonPointer);

        Iterator<@Nullable Object> iterator = iterable.iterator();
        int i = 0;
        while (true) {
            if (!iterator.hasNext()) {
                // We have no more items in the iterator but not yet reached the
                // desired index, so we are "out of bounds". Call the common
                // method for range checking. This will fail as index >= limit (i).
                checkIndexRange(index, i, jsonPointer);
            }
            @Nullable Object object = iterator.next();
            if (i == index)
                return object;
            i++;
        }
    }

    private static Object getListItem(List<Object> list, String indexString, String jsonPointer) {
        int index = parseAndCheckIndex(indexString, list.size(), jsonPointer);

        return list.get(index);
    }

    private static @Nullable Object getArrayItem(@Nullable Object[] array, String indexString, String jsonPointer) {
        int index = parseAndCheckIndex(indexString, array.length, jsonPointer);

        return array[index];
    }

    private static int parseAndCheckIndex(String token, int limit, String jsonPointer) {
        int index = parseIndex(token, jsonPointer);

        checkIndexRange(index, limit, jsonPointer);

        return index;
    }

    private static void checkIndexRange(int index, int limit, String jsonPointer) {
        if (index < 0 || index >= limit) {
            throw newOutOfRangeException(index, limit, jsonPointer);
        }
    }

    private static int parseIndex(String token, String jsonPointer) {
        int index;
        try {
            index = Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw newIndexExpectedException(token, jsonPointer);
        }
        return index;
    }

    private static IllegalArgumentException newMissingRootSlash(String jsonPointer) {
        return new IllegalArgumentException(
                String.format("Error in '%s': must start with '/' or be empty", jsonPointer));
    }

    private static IllegalArgumentException newMissingKeyException(String token, String jsonPointer) {
        return new IllegalArgumentException(
                String.format("Error in '%s': Map is missing key '%s'", jsonPointer, token));
    }

    private static IllegalArgumentException newOutOfRangeException(int index, int limit, String jsonPointer) {
        return new IllegalArgumentException(
                String.format("Error in '%s': expected 0 < index < %d, got index with: %d",
                        jsonPointer, limit, index));
    }

    private static IllegalArgumentException newNegativeIndexException(int index, String jsonPointer) {
        return new IllegalArgumentException(
                String.format("Error in '%s': index must not be negative, got: %d",
                        jsonPointer, index));
    }

    private static IllegalArgumentException newIndexExpectedException(String text, String jsonPointer) {
        return new IllegalArgumentException(
                String.format("Error in '%s': expected index, got: '%s'",
                        jsonPointer, text));
    }

    public static JSONPointer newJSONPointer(String jsonPointer) {
        return new JSONPointer(jsonPointer);
    }

    public static JSONPointer of(String jsonPointer) {
        return newJSONPointer(jsonPointer);
    }

    public boolean isRoot() {
        return pointer.isEmpty();
    }

    public JSONPointer getParent() {
        checkNotRoot();

        return of(pointer.substring(0, pointer.lastIndexOf(TOKEN_PREFIX)));
    }

    public String getLastToken() {
        checkNotRoot();

        return ArrayUtil.lastItem(tokens);
    }

    private void checkNotRoot() {
        if (isRoot()) {
            throw new IllegalStateException(UNSUPPORTED_FOR_ROOT);
        }
    }

    @Override
    public @Nullable Object apply(@Nullable Object root) {
        return referencedValue(root, tokens, pointer);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof JSONPointer)) return false;
        JSONPointer that = (JSONPointer) o;
        //noinspection CallToSuspiciousStringMethod
        return pointer.equals(that.pointer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointer);
    }

    @Override
    public String toString() {
        return pointer;
    }
}
