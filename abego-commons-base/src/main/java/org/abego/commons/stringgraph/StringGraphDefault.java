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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.abego.commons.seq.SeqUtil.emptySeq;
import static org.abego.commons.seq.SeqUtil.newSeq;
import static org.abego.commons.util.function.PredicateUtil.and;

class StringGraphDefault implements StringGraph {
    private final Seq<String> nodes;
    private final Seq<Edge> edges;
    /**
     * Links every fromNode to the Edges it belongs to.
     */
    private final Map<String, Set<Edge>> fromNodeToEdgesLinks = new HashMap<>();
    /**
     * Links every toNode to the Edges it belongs to.
     */
    private final Map<String, Set<Edge>> toNodeToEdgesLinks = new HashMap<>();
    /**
     * Links every label to the Edges it belongs to.
     */
    private final Map<String, Set<Edge>> labelToEdgesLinks = new HashMap<>();

    private final EdgeProperty edgePropertyFromNode = new EdgeProperty(Edge::getFromNode, fromNodeToEdgesLinks);
    private final EdgeProperty edgePropertyToNode = new EdgeProperty(Edge::getToNode, toNodeToEdgesLinks);
    private final EdgeProperty edgePropertyLabel = new EdgeProperty(Edge::getLabel, labelToEdgesLinks);

    private StringGraphDefault(Set<String> nodes, Set<Edge> edges) {
        this.nodes = newSeq(nodes);
        this.edges = newSeq(edges);

        for (Edge e : edges) {
            updateMap(fromNodeToEdgesLinks, e.getFromNode(), e);
            updateMap(toNodeToEdgesLinks, e.getToNode(), e);
            updateMap(labelToEdgesLinks, e.getLabel(), e);
        }
    }

    private static EdgePropertyEqualsToTest[] withItemAtIndexRemoved(
            EdgePropertyEqualsToTest[] propertyTests, int index) {
        int n = propertyTests.length;
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException();
        }

        EdgePropertyEqualsToTest[] result = new EdgePropertyEqualsToTest[n - 1];
        System.arraycopy(propertyTests, 0, result, 0, index);
        int nRight = n - index - 1;
        if (nRight > 0)
            System.arraycopy(propertyTests, index + 1, result, index, nRight);
        return result;
    }

    private Seq<String> findEdgesWithPropertiesEqualToAndMap(
            Function<Edge, String> edgeToResultMapper,
            EdgePropertyEqualsToTest... propertyTests) {
        // get the Edge Sets for every property set.
        // return with emptySeq if any of these sets is empty.
        List<Set<Edge>> edgesPerTest = new ArrayList<>();
        for (EdgePropertyEqualsToTest t : propertyTests) {
            Set<Edge> edges = t.edges();
            if (edges.isEmpty()) {
                return emptySeq();
            }
            edgesPerTest.add(edges);
        }

        Seq<Edge> edgesToTest = edges;
        Predicate<Edge>[] remainingTests = propertyTests;

        // Find the test that has the smallest Edge set associated (if any)
        int minEdgeCountTestIndex = indexOfTestWithSmallestEdgeSet(edgesPerTest);
        if (minEdgeCountTestIndex >= 0) {
            // If any test has an associated Edge set we can use that Edge set
            // (instead of "all" edges) to find the Edges matching all
            // property tests.
            edgesToTest = newSeq(edgesPerTest.get(minEdgeCountTestIndex));
            // As the found test is already applied the Edge set we don't need
            // to re-test and can remove that test from the remainingTests.
            remainingTests = withItemAtIndexRemoved(
                    propertyTests, minEdgeCountTestIndex);
        }

        Seq<Edge> foundEdges = remainingTests.length == 0
                ? edgesToTest : edgesToTest.filter(and(remainingTests));
        return foundEdges.map(edgeToResultMapper);
    }

    private int indexOfTestWithSmallestEdgeSet(List<Set<Edge>> edgesPerTest) {
        int minEdgeCount = Integer.MAX_VALUE;
        int minEdgeCountTestIndex = -1;
        for (int i = 0; i < edgesPerTest.size(); i++) {
            int n = edgesPerTest.get(i).size();
            if (n < minEdgeCount) {
                minEdgeCount = n;
                minEdgeCountTestIndex = i;
            }
        }
        return minEdgeCountTestIndex;
    }

    private static void updateMap(Map<String, Set<Edge>> map, String key, Edge edge) {
        map.computeIfAbsent(key, k -> new HashSet<>()).add(edge);
    }

    static StringGraphBuilder createBuilder() {
        return new MyBuilder();
    }

    static StringGraph createStringGraph(Set<String> nodes, Set<Edge> edges) {
        return new StringGraphDefault(nodes, edges);
    }

    /**
     * Returns the values of the given property for all Edges associated with
     * the given key, without duplicate values.
     */
    private static Seq<String> getUniquePropertyValues(
            Map<String, Set<Edge>> map,
            String key,
            Function<Edge, String> edgePropertyMapper) {
        @Nullable Set<Edge> edges = map.get(key);
        if (edges == null) {
            return emptySeq();
        } else {
            return newSeq(edges.stream().map(edgePropertyMapper).distinct());
        }
    }

    @Override
    public Seq<String> allFromNodes() {
        return newSeq(fromNodeToEdgesLinks.keySet());
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
    public Seq<String> allToNodes() {
        return newSeq(toNodeToEdgesLinks.keySet());
    }

    @Override
    public Seq<String> allEdgeLabels() {
        return newSeq(labelToEdgesLinks.keySet());
    }

    @Override
    public Seq<String> allNodesFromNode(String fromNode) {
        return getUniquePropertyValues(
                fromNodeToEdgesLinks, fromNode, Edge::getToNode);
    }

    @Override
    public Seq<String> allEdgeLabelsFromNode(String fromNode) {
        return getUniquePropertyValues(
                fromNodeToEdgesLinks, fromNode, Edge::getLabel);
    }

    @Override
    public Seq<String> allNodesFromNodeViaEdgeLabeled(
            String fromNode, String edgeLabel) {
        return findEdgesWithPropertiesEqualToAndMap(
                Edge::getToNode,
                edgePropertyFromNode.isEqualTo(fromNode),
                edgePropertyLabel.isEqualTo(edgeLabel)
        );
    }

    @Override
    public Seq<String> allNodesToNode(String toNode) {
        return getUniquePropertyValues(
                toNodeToEdgesLinks, toNode, Edge::getFromNode);
    }

    @Override
    public Seq<String> allEdgeLabelsToNode(String toNode) {
        return getUniquePropertyValues(
                toNodeToEdgesLinks, toNode, Edge::getLabel);
    }

    @Override
    public Seq<String> allNodesToNodeViaEdgeLabeled(String toNode, String edgeLabel) {
        return findEdgesWithPropertiesEqualToAndMap(
                Edge::getFromNode,
                edgePropertyToNode.isEqualTo(toNode),
                edgePropertyLabel.isEqualTo(edgeLabel)
        );
    }

    @Override
    public String toString() {
        //noinspection HardCodedStringLiteral,StringConcatenation,MagicCharacter,DuplicateStringLiteralInspection
        return "StringGraphDefault{" + "nodes=" + nodes + ", edges=" + edges + '}';
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

    private static class MyBuilder extends AbstractStringGraphBuilder {
        @Override
        public StringGraph build() {
            return createStringGraph(getNodes(), getEdges());
        }
    }
}
