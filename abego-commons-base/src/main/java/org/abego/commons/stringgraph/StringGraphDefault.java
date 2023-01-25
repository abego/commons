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
import java.util.stream.Stream;

import static org.abego.commons.seq.SeqUtil.emptySeq;
import static org.abego.commons.seq.SeqUtil.newSeq;
import static org.abego.commons.util.function.PredicateUtil.and;

class StringGraphDefault implements StringGraph {
    private final Seq<String> nodes;
    private final Seq<Edge> edges;
    private final Set<Edge> edgesSet;
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
        this.edgesSet = edges;
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

    /**
     * Returns the values of the given property for all Edges associated with
     * the given key, without duplicate values.
     */
    private static Seq<String> uniqueResultsOfEdgesWithPropertyEqualTo(
            Function<Edge, String> edgeToResultMapper,
            EdgePropertyEqualsToTest propertyTest) {
        Set<Edge> edges = propertyTest.edges();
        if (edges.isEmpty()) {
            return emptySeq();
        } else {
            return newSeq(edges.stream().map(edgeToResultMapper).distinct());
        }
    }

    private Stream<Edge> edgesWithPropertiesEqualTo(
            EdgePropertyEqualsToTest... propertyTests) {
        // get the Edge Sets for every property set.
        // return with emptySeq if any of these sets is empty.
        List<Set<Edge>> edgesPerTest = new ArrayList<>();
        for (EdgePropertyEqualsToTest t : propertyTests) {
            Set<Edge> edges = t.edges();
            if (edges.isEmpty()) {
                return Stream.empty();
            }
            edgesPerTest.add(edges);
        }

        Set<Edge> edgesToTest = edgesSet;
        Predicate<Edge>[] remainingTests = propertyTests;

        // Find the test with the smallest Edge set associated to it (if any)
        int minEdgeCountTestIndex = indexOfTestWithSmallestEdgeSet(edgesPerTest);
        if (minEdgeCountTestIndex >= 0) {
            // If any test has an associated Edge set we can use that Edge set
            // (instead of "all" edges) to find the Edges matching all
            // property tests.
            edgesToTest = edgesPerTest.get(minEdgeCountTestIndex);
            // As the found test is already applied the Edge set we don't need
            // to re-test and can remove that test from the remainingTests.
            remainingTests = withItemAtIndexRemoved(
                    propertyTests, minEdgeCountTestIndex);
        }

        Stream<Edge> result = edgesToTest.stream();
        if (remainingTests.length > 0) {
            result = result.filter(and(remainingTests));
        }
        return result;
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

    private Stream<String> findEdgesWithPropertiesEqualToAndMap(
            Function<Edge, String> edgeToResultMapper,
            EdgePropertyEqualsToTest... propertyTests) {
        return edgesWithPropertiesEqualTo(propertyTests).map(edgeToResultMapper);
    }

    @Override
    public Seq<String> fromNodes() {
        return newSeq(fromNodeToEdgesLinks.keySet());
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
    public Seq<String> toNodes() {
        return newSeq(toNodeToEdgesLinks.keySet());
    }

    @Override
    public Seq<String> edgeLabels() {
        return newSeq(labelToEdgesLinks.keySet());
    }

    @Override
    public Seq<String> nodesFromNode(String fromNode) {
        return uniqueResultsOfEdgesWithPropertyEqualTo(
                Edge::getToNode, edgePropertyFromNode.isEqualTo(fromNode));
    }

    @Override
    public Seq<String> edgeLabelsFromNode(String fromNode) {
        return uniqueResultsOfEdgesWithPropertyEqualTo(
                Edge::getLabel, edgePropertyFromNode.isEqualTo(fromNode));
    }

    @Override
    public Seq<String> nodesFromNodeViaEdgeLabeled(
            String fromNode, String edgeLabel) {
        return newSeq(findEdgesWithPropertiesEqualToAndMap(
                Edge::getToNode,
                edgePropertyFromNode.isEqualTo(fromNode),
                edgePropertyLabel.isEqualTo(edgeLabel)));
    }

    @Override
    public Seq<String> nodesToNode(String toNode) {
        return uniqueResultsOfEdgesWithPropertyEqualTo(
                Edge::getFromNode, edgePropertyToNode.isEqualTo(toNode));
    }

    @Override
    public Seq<String> edgeLabelsToNode(String toNode) {
        return uniqueResultsOfEdgesWithPropertyEqualTo(
                Edge::getLabel, edgePropertyToNode.isEqualTo(toNode));
    }

    @Override
    public Seq<String> nodesToNodeViaEdgeLabeled(String toNode, String edgeLabel) {
        return newSeq(findEdgesWithPropertiesEqualToAndMap(
                Edge::getFromNode,
                edgePropertyToNode.isEqualTo(toNode),
                edgePropertyLabel.isEqualTo(edgeLabel)));
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
