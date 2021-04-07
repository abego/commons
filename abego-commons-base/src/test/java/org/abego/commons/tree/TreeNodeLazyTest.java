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
import org.abego.commons.seq.SeqUtil;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TreeNodeLazyTest {

    @Test
    void smokeTest() {
        TreeNodeLazy<String> tree = TreeNodeLazy.newTreeNodeLazy("root", null, threeChildrenProvider(2));
        TreeNodeLazy<String> sameTree = TreeNodeLazy.newTreeNodeLazy("root", null, threeChildrenProvider(2));
        TreeNodeLazy<String> otherTree = TreeNodeLazy.newTreeNodeLazy("root", null, threeChildrenProvider(3));

        assertNull(tree.getParent());
        assertEquals("root", tree.toString());
        assertEquals("root", tree.getValue());
        assertEquals(tree, tree);
        assertEquals(tree, sameTree);
        assertEquals(tree.hashCode(), sameTree.hashCode());
        assertNotEquals(tree, otherTree);
        //noinspection AssertBetweenInconvertibleTypes
        assertNotEquals(tree, "foo");

        Seq<TreeNode<String>> children = tree.getChildren();
        assertEquals(3, children.size());
        assertEquals("root.1", children.item(0).getValue());
        assertEquals("root.2", children.item(1).getValue());
        assertEquals("root.3", children.item(2).getValue());
    }

    Function<TreeNode<String>, Iterable<String>> threeChildrenProvider(int maxLevel) {
        return parent -> (parent.getPath().size() > maxLevel)
                ? SeqUtil.emptySeq()
                : rangeClosed(1, 3).mapToObj(i -> parent + "." + i).collect(toList());
    }
}