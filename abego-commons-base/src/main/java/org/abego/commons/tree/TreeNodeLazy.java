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
import org.eclipse.jdt.annotation.Nullable;

import java.util.Objects;
import java.util.function.Function;

import static org.abego.commons.seq.SeqUtil.newSeq;

public final class TreeNodeLazy<T> implements TreeNode<T> {
    private final T value;
    private final Function<TreeNode<T>, Iterable<T>> childrenValueProvider;
    @Nullable
    private final TreeNode<T> parent;
    @Nullable
    private transient Seq<TreeNode<T>> children;

    private TreeNodeLazy(T value,
                         @Nullable TreeNode<T> parent,
                         Function<TreeNode<T>, Iterable<T>> childrenValueProvider) {
        this.value = value;
        this.parent = parent;
        this.childrenValueProvider = childrenValueProvider;
    }

    public static <T> TreeNodeLazy<T> newTreeNodeLazy(T value,
                                                      @Nullable TreeNode<T> parent,
                                                      Function<TreeNode<T>, Iterable<T>> childrenValueProvider) {
        return new TreeNodeLazy<>(value, parent, childrenValueProvider);
    }

    public String toString() {
        return Objects.toString(value);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public @Nullable TreeNode<T> getParent() {
        return parent;
    }

    @Override
    public Seq<TreeNode<T>> getChildren() {
        @Nullable Seq<TreeNode<T>> cs = children;
        if (cs == null) {
            cs = newSeq(childrenValueProvider.apply(this))
                    .map(c -> new TreeNodeLazy<>(
                            c, this, childrenValueProvider));
            children = cs;
        }
        return cs;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TreeNodeLazy)) {
            return false;
        }
        TreeNodeLazy<?> that = (TreeNodeLazy<?>) o;
        return Objects.equals(value, that.value) &&
                getChildren().equals(that.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, getChildren());
    }
}
