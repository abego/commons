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

import java.util.HashSet;
import java.util.Set;

abstract class AbstractStringGraphBuilder implements StringGraphBuilder {
    private final Set<String> nodes = new HashSet<>();
    private final Set<Edge> edges = new HashSet<>();

    AbstractStringGraphBuilder() {
    }

    protected Edge newEdge(String fromNode, String toNode, String edgeLabel) {
        return EdgeDefault.createEdge(fromNode, toNode, edgeLabel);
    }

    public Set<String> getNodes() {
        return nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
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

}
