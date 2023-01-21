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
import org.abego.commons.seq.SeqUtil;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class StringGraphBuilderDefault implements StringGraphBuilder {
    private final Set<String> nodes = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();

    private StringGraphBuilderDefault() {
    }

    static StringGraphBuilder createStringGraphBuilder() {
        return new StringGraphBuilderDefault();
    }

    public static String calcEdgeText(Edge edge) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.quoted2(edge.getFromNode()));
        sb.append(" -> ");
        sb.append(StringUtil.quoted2(edge.getToNode()));
        String label = edge.getLabel();
        if (!label.isEmpty()) {
            sb.append(" : ");
            sb.append(StringUtil.quoted2(label));
        }
        return sb.toString();
    }

    private Edge newEdge(String fromNode, String toNode, String edgeLabel) {
        return new EdgeImpl(fromNode, toNode, edgeLabel);
    }

    @Override
    public StringGraph build() {
        return StringGraphImpl.createStringGraph(
                SeqUtil.newSeq(nodes), SeqUtil.newSeq(edges));
    }

    @Override
    public void addNode(String node) {
        nodes.add(node);
    }

    @Override
    public void addEdge(String fromNode, String toNode, String edgeLabel) {
        addNode(fromNode);
        addNode(toNode);
        edges.add(newEdge(fromNode, toNode, edgeLabel));
    }

    private static class EdgeImpl implements Edge {
        private static final Comparator<Edge> COMPARATOR = createComparator();
        private final String fromNode;
        private final String toNode;
        private final String label;

        private EdgeImpl(String fromNode, String toNode, String label) {
            this.fromNode = fromNode;
            this.toNode = toNode;
            this.label = label;
        }

        private static Comparator<Edge> createComparator() {
            return Comparator
                    .comparing(Edge::getFromNode)
                    .thenComparing(Edge::getLabel)
                    .thenComparing(Edge::getToNode);
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
            return calcEdgeText(this);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EdgeImpl edge = (EdgeImpl) o;
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
            return "EdgeImpl{" +
                    "fromNode=" + StringUtil.quoted2(fromNode) +
                    ", label='" + StringUtil.quoted2(label) +
                    ", toNode='" + StringUtil.quoted2(toNode) +
                    '}';
        }

        @Override
        public int compareTo(Edge o) {
            return COMPARATOR.compare(this, o);
        }
    }
}
