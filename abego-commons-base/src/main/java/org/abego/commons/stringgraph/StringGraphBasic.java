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

import org.abego.commons.seq.Seq;
import org.abego.commons.seq.SeqUtil;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Objects;

/**
 * A basic/simple implementation of the StringGraph.
 * <p>
 * This implementation should not be used when working on large graphs as most
 * of its code falls back to the default implementations in StringGraph, which
 * result in O(N) complexity for most queries on graphs.
 */
class StringGraphBasic implements StringGraph {
    private final Seq<String> nodes;
    private final Seq<Edge> edges;

    public static StringGraphBuilder createBuilder() {
        return new MyBuilder();
    }

    private StringGraphBasic(Seq<String> nodes, Seq<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    private static class MyBuilder extends AbstractStringGraphBuilder {
        public StringGraph build() {
            return createStringGraph(
                    SeqUtil.newSeq(getNodes()), SeqUtil.newSeq(getEdges()));
        }
    }

    static StringGraph createStringGraph(Seq<String> nodes, Seq<Edge> edges) {
        return new StringGraphBasic(nodes, edges);
    }

    @Override
    public Seq<String> nodes() {
        return nodes;
    }

    @Override
    public Seq<Edge> edges() {
        return edges;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringGraphBasic that = (StringGraphBasic) o;
        return nodes.equals(that.nodes) && edges.equals(that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }

    @Override
    public String toString() {
        //noinspection HardCodedStringLiteral,StringConcatenation,MagicCharacter,DuplicateStringLiteralInspection
        return "StringGraphBasic{" + "nodes=" + nodes + ", edges=" + edges + '}';
    }
}
