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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringGraphTest {
    private static final StringGraph ANY_SAMPLE1 =
            stringGraphSample1Provider().findAny()
                    .orElseThrow(() -> new IllegalStateException(
                            "No StringGraphBuilderProvider defined."));

    static Stream<StringGraph> stringGraphSample1Provider() {
        return stringGraphBuilderProvider()
                .map(b -> constructSample1(b).build());
    }

    static Stream<StringGraphBuilder> stringGraphBuilderProvider() {
        return StringGraphBuilderTest.stringGraphBuilderProvider();
    }

    public static void assertStringsAsLinesEquals(
            String expectedStringsInLines, Seq<String> nodes) {
        assertEquals(expectedStringsInLines,
                nodes.size() + "\n" +
                        nodes.sorted().joined("\n"));
    }

    public static void assertNodesEquals(
            String expectedStringsInLines, Seq<String> nodes) {
        assertStringsAsLinesEquals(expectedStringsInLines, nodes);
    }

    /**
     * Returns a "Sample1" graph, created by any available StringGraphBuilder.
     * <p>
     * The StringGraphBuilder used may change any time.
     */
    public static StringGraph getAnySample1() {
        return ANY_SAMPLE1;
    }

    public static <T extends StringGraphConstructing> T constructSample1(T constructing) {
        constructing.addNode("a");
        constructing.addNode("b");
        constructing.addNode("c"); // will later become a cycle
        constructing.addEdge("d", "e");
        constructing.addEdge("f", "g", "h");
        constructing.addEdge("i", "i", "cycle");
        constructing.addEdge("c", "c", "cycle");
        constructing.addEdge("o", "m1", "field");
        constructing.addEdge("o", "m2", "field");
        constructing.addEdge("o", "m3", "");
        return constructing;
    }

    public static void assertEqualsToAllNodesOfSample1(Seq<String> nodes) {
        assertNodesEquals("12\n" +
                        "a\n" +
                        "b\n" +
                        "c\n" +
                        "d\n" +
                        "e\n" +
                        "f\n" +
                        "g\n" +
                        "i\n" +
                        "m1\n" +
                        "m2\n" +
                        "m3\n" +
                        "o",
                nodes);
    }

    public static void assertEqualsToAllEdgesOfSample1(Seq<Edge> edges) {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("7\n" +
                        "\"c\" -> \"c\" : \"cycle\"\n" +
                        "\"d\" -> \"e\"\n" +
                        "\"f\" -> \"g\" : \"h\"\n" +
                        "\"i\" -> \"i\" : \"cycle\"\n" +
                        "\"o\" -> \"m3\"\n" +
                        "\"o\" -> \"m1\" : \"field\"\n" +
                        "\"o\" -> \"m2\" : \"field\"",
                edges);
    }

    public static void assertEqualToSample1(StringGraph graph) {
        assertEqualsToAllNodesOfSample1(graph.nodes());
        assertEqualsToAllEdgesOfSample1(graph.edges());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodes(StringGraph sample1) {
        assertEqualsToAllNodesOfSample1(sample1.nodes());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdges(StringGraph sample1) {
        assertEqualsToAllEdgesOfSample1(sample1.edges());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allFromNodes(StringGraph sample1) {
        assertNodesEquals("5\n" +
                        "c\n" +
                        "d\n" +
                        "f\n" +
                        "i\n" +
                        "o",
                sample1.fromNodes());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allToNodes(StringGraph sample1) {
        assertNodesEquals("7\n" +
                        "c\n" +
                        "e\n" +
                        "g\n" +
                        "i\n" +
                        "m1\n" +
                        "m2\n" +
                        "m3",
                sample1.toNodes());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgeLabels(StringGraph sample1) {
        assertStringsAsLinesEquals("4\n" +
                        "\n" +
                        "cycle\n" +
                        "field\n" +
                        "h",
                sample1.edgeLabels());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodesFromNode(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNode("a"));
        // single node
        assertStringsAsLinesEquals("1\n" +
                        "g",
                sample1.nodesFromNode("f"));
        // multiple nodes
        assertStringsAsLinesEquals("3\n" +
                        "m1\n" +
                        "m2\n" +
                        "m3",
                sample1.nodesFromNode("o"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                sample1.nodesFromNode("c"));
        // toNode that is no fromNode
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNode("g"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgeLabelsFromNode(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.edgeLabelsFromNode("a"));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "",
                sample1.edgeLabelsFromNode("d"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "h",
                sample1.edgeLabelsFromNode("f"));
        // multiple nodes
        assertStringsAsLinesEquals("2\n" +
                        "\n" +
                        "field",
                sample1.edgeLabelsFromNode("o"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "cycle",
                sample1.edgeLabelsFromNode("c"));
        // toNode that is no fromNode
        assertStringsAsLinesEquals("0\n", sample1.edgeLabelsFromNode("g"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.edgeLabelsFromNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodesFromNodeViaEdgeLabeled(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNodeViaEdgeLabeled("a", ""));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "e",
                sample1.nodesFromNodeViaEdgeLabeled("d", ""));
        // single node, empty label, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNodeViaEdgeLabeled("d", "x"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "g",
                sample1.nodesFromNodeViaEdgeLabeled("f", "h"));
        // single node, non-empty label, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNodeViaEdgeLabeled("f", "x"));
        // multiple nodes
        assertStringsAsLinesEquals("1\n" +
                        "m3",
                sample1.nodesFromNodeViaEdgeLabeled("o", ""));
        assertStringsAsLinesEquals("2\n" +
                        "m1\n" +
                        "m2",
                sample1.nodesFromNodeViaEdgeLabeled("o", "field"));
        // multiple nodes, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNodeViaEdgeLabeled("o", "x"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                sample1.nodesFromNodeViaEdgeLabeled("c", "cycle"));
        // self cyclic, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNodeViaEdgeLabeled("c", "x"));
        // toNode that is no fromNode
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNodeViaEdgeLabeled("g", "h"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.nodesFromNodeViaEdgeLabeled("x", ""));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodesToNode(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.nodesToNode("a"));
        // single node
        assertStringsAsLinesEquals("1\n" +
                        "d",
                sample1.nodesToNode("e"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                sample1.nodesToNode("c"));
        // fromNode that is no toNode
        assertStringsAsLinesEquals("0\n", sample1.nodesToNode("d"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.nodesToNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgeLabelsToNode(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.edgeLabelsToNode("a"));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "",
                sample1.edgeLabelsToNode("e"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "h",
                sample1.edgeLabelsToNode("g"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "cycle",
                sample1.edgeLabelsToNode("c"));
        // fromNode that is no toNode
        assertStringsAsLinesEquals("0\n", sample1.edgeLabelsToNode("d"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.edgeLabelsToNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodesToNodeViaEdgeLabeled(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.nodesToNodeViaEdgeLabeled("a", ""));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "d",
                sample1.nodesToNodeViaEdgeLabeled("e", ""));
        // single node, empty label, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.nodesToNodeViaEdgeLabeled("e", "x"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "f",
                sample1.nodesToNodeViaEdgeLabeled("g", "h"));
        // single node, non-empty label, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.nodesToNodeViaEdgeLabeled("g", "x"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                sample1.nodesToNodeViaEdgeLabeled("c", "cycle"));
        // self cyclic, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.nodesToNodeViaEdgeLabeled("c", "x"));
        // fromNode that is no toNode
        assertStringsAsLinesEquals("0\n", sample1.nodesToNodeViaEdgeLabeled("f", "h"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.nodesToNodeViaEdgeLabeled("x", ""));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgesWith(StringGraph sample1) {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("3\n" +
                        "\"o\" -> \"m3\"\n" +
                        "\"o\" -> \"m1\" : \"field\"\n" +
                        "\"o\" -> \"m2\" : \"field\"",
                sample1.edgesWith(e -> e.getToNode().startsWith("m")));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"f\" -> \"g\" : \"h\"",
                sample1.edgesWith(e -> e.getLabel().equals("h")));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.edgesWith(e -> false));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgesLabeled(StringGraph sample1) {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("2\n" +
                        "\"d\" -> \"e\"\n" +
                        "\"o\" -> \"m3\"",
                sample1.edgesLabeled(""));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("2\n" +
                        "\"c\" -> \"c\" : \"cycle\"\n" +
                        "\"i\" -> \"i\" : \"cycle\"",
                sample1.edgesLabeled("cycle"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.edgesLabeled("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgesFromNode(StringGraph sample1) {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.edgesFromNode("a"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"c\" -> \"c\" : \"cycle\"",
                sample1.edgesFromNode("c"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"d\" -> \"e\"",
                sample1.edgesFromNode("d"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("3\n" +
                        "\"o\" -> \"m3\"\n" +
                        "\"o\" -> \"m1\" : \"field\"\n" +
                        "\"o\" -> \"m2\" : \"field\"",
                sample1.edgesFromNode("o"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.edgesFromNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgesToNode(StringGraph sample1) {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.edgesToNode("a"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"c\" -> \"c\" : \"cycle\"",
                sample1.edgesToNode("c"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"d\" -> \"e\"",
                sample1.edgesToNode("e"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.edgesFromNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphBuilderProvider")
    void equalsAndHashcode(StringGraphBuilder builder) {
        StringGraph sample1 = constructSample1(builder).build();
        StringGraph otherSample1 = constructSample1(builder).build();
        int h1 = sample1.hashCode();
        int h2 = otherSample1.hashCode();

        assertNotEquals(sample1, "not a graph");
        assertNotEquals(sample1, null);
        assertEquals(sample1, sample1);
        assertEquals(sample1, otherSample1);
        assertEquals(h1, h2);
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void toStringTest(StringGraph sample1) {
        String s = sample1.toString();

        assertTrue(s.startsWith("StringGraph"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphBuilderProvider")
    void duplicateEdges(StringGraphBuilder builder) {
        builder.addEdge("a", "b", "c");
        builder.addEdge("d", "e", "f");
        // adding an edge that already exists will not change the graph
        builder.addEdge("a", "b", "c");
        StringGraph graph = builder.build();

        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("2\n" +
                "\"a\" -> \"b\" : \"c\"\n" +
                "\"d\" -> \"e\" : \"f\"", graph.edges());
    }
}
