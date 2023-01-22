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

package org.abego.commons.stringgraph;

import org.abego.commons.lang.StringUtil;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Comparator;
import java.util.Objects;

class EdgeDefault implements Edge {
    private static final Comparator<Edge> COMPARATOR = EdgeUtil.getComparator();
    private final String fromNode;
    private final String toNode;
    private final String label;

    private EdgeDefault(String fromNode, String toNode, String label) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.label = label;
    }

    static Edge createEdge(String fromNode, String toNode, String label) {
        return new EdgeDefault(fromNode, toNode, label);
    }

    @Override
    public String getFromNode() {
        return fromNode;
    }

    @Override
    public String getToNode() {
        return toNode;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getText() {
        return EdgeUtil.calcEdgeText(this);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeDefault edge = (EdgeDefault) o;
        //noinspection CallToSuspiciousStringMethod
        return getFromNode().equals(edge.getFromNode())
                && getToNode().equals(edge.getToNode())
                && getLabel().equals(edge.getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromNode(), getToNode(), getLabel());
    }

    @Override
    public String toString() {
        //noinspection HardCodedStringLiteral,StringConcatenation,MagicCharacter
        return "EdgeDefault{" +
                "fromNode=" + StringUtil.quoted2(fromNode) +
                ", label=" + StringUtil.quoted2(label) +
                ", toNode=" + StringUtil.quoted2(toNode) +
                '}';
    }

    @Override
    public int compareTo(Edge o) {
        return COMPARATOR.compare(this, o);
    }
}
