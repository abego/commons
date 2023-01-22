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
import org.eclipse.jdt.annotation.Nullable;

import java.util.Objects;

class StringGraphDefault implements StringGraph {
    private final Seq<String> nodes;
    private final Seq<Edge> edges;

    private StringGraphDefault(Seq<String> nodes, Seq<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    static StringGraph createStringGraph(Seq<String> nodes, Seq<Edge> edges) {
        return new StringGraphDefault(nodes, edges);
    }

    @Override
    public Seq<String> allNodes() {
        return nodes;
    }

    @Override
    public Seq<Edge> allEdges() {
        return edges;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringGraphDefault that = (StringGraphDefault) o;
        return nodes.equals(that.nodes) && edges.equals(that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }

    @Override
    public String toString() {
        //noinspection HardCodedStringLiteral,StringConcatenation,MagicCharacter
        return "StringGraphDefault{" + "nodes=" + nodes + ", edges=" + edges + '}';
    }
}
