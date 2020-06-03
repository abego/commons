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

package org.abego.commons.tree;

import org.abego.commons.lang.exception.MustNotInstantiateException;

import static org.abego.commons.lang.CharacterUtil.NEWLINE_CHAR;

public final class TreeNodeUtil {

    TreeNodeUtil() {
        throw new MustNotInstantiateException();
    }

    public static <T> String toDebugString(TreeNode<T> root) {
        StringBuilder result = new StringBuilder();
        appendTree(result, root, "");
        return result.toString();
    }

    private static <T> void appendTree(StringBuilder result, TreeNode<T> root, String prefix) {
        result.append(prefix);
        result.append(root.getValue());
        result.append(NEWLINE_CHAR);
        for (TreeNode<T> child : root.getChildren()) {
            //noinspection StringConcatenation
            appendTree(result, child, prefix + "  ");
        }
    }


}
