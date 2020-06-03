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

import org.abego.commons.seq.Seq;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TreeNodeTest {

    static TreeNode<String> newTreeSample() {

        return node("root",
                node("A",
                        node("B",
                                node("C"),
                                node("D")),
                        node("E",
                                node("F"),
                                node("G"))),
                node("X",
                        node("Y"),
                        node("Z")));
    }

    @SafeVarargs
    private static TreeNodeDefault<String> node(String text, @NonNull TreeNodeDefault<String>... children) {
        return TreeNodeDefault.newTreeNodeDefault(text, children);
    }

    @Test
    void getValue() {
        TreeNode<String> root = newTreeSample();

        assertEquals("root", root.getValue());
    }

    @Test
    void getParent() {
        TreeNode<String> root = newTreeSample();
        Seq<TreeNode<String>> children = root.getChildren();
        TreeNode<String> child = children.first();

        assertNull(root.getParent());
        assertEquals(root, child.getParent());

        assertEquals("root", root.getValue());
    }

    @Test
    void getChildren() {
        TreeNode<String> root = newTreeSample();

        Seq<TreeNode<String>> children = root.getChildren();
        assertEquals(2, children.size());

        TreeNode<String> aNode = children.item(0);
        TreeNode<String> xNode = children.item(1);

        assertNull(root.getParent());
        assertEquals("A", aNode.getValue());
        assertEquals("X", xNode.getValue());

        TreeNode<String> bNode = aNode.getChildren().item(0);
        assertEquals("B", bNode.getValue());
        assertEquals(2, bNode.getChildren().size());


    }

}