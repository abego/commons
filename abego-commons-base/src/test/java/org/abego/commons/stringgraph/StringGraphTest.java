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
    private static final StringGraph SAMPLE_1_BASIC =
            constructSample1(StringGraphBuilderBasic.createStringGraphBuilder()).build();
    private static final StringGraph SAMPLE_1_DEFAULT =
            constructSample1(StringGraphBuilderDefault.createStringGraphBuilder()).build();

    static Stream<StringGraph> stringGraphSample1Provider() {
        //TODO: use stringGraphBuilderProvider to create this
        return Stream.of(SAMPLE_1_BASIC, SAMPLE_1_DEFAULT);
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

    public static StringGraph getSample1() {
        return SAMPLE_1_BASIC;
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
        assertEqualsToAllNodesOfSample1(graph.allNodes());
        assertEqualsToAllEdgesOfSample1(graph.allEdges());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodes(StringGraph sample1) {
        assertEqualsToAllNodesOfSample1(sample1.allNodes());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdges(StringGraph sample1) {
        assertEqualsToAllEdgesOfSample1(sample1.allEdges());
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
                sample1.allFromNodes());
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
                sample1.allToNodes());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgeLabels(StringGraph sample1) {
        assertStringsAsLinesEquals("4\n" +
                        "\n" +
                        "cycle\n" +
                        "field\n" +
                        "h",
                sample1.allEdgeLabels());
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodesFromNode(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNode("a"));
        // single node
        assertStringsAsLinesEquals("1\n" +
                        "g",
                sample1.allNodesFromNode("f"));
        // multiple nodes
        assertStringsAsLinesEquals("3\n" +
                        "m1\n" +
                        "m2\n" +
                        "m3",
                sample1.allNodesFromNode("o"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                sample1.allNodesFromNode("c"));
        // toNode that is no fromNode
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNode("g"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgeLabelsFromNode(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.allEdgeLabelsFromNode("a"));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "",
                sample1.allEdgeLabelsFromNode("d"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "h",
                sample1.allEdgeLabelsFromNode("f"));
        // multiple nodes
        assertStringsAsLinesEquals("2\n" +
                        "\n" +
                        "field",
                sample1.allEdgeLabelsFromNode("o"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "cycle",
                sample1.allEdgeLabelsFromNode("c"));
        // toNode that is no fromNode
        assertStringsAsLinesEquals("0\n", sample1.allEdgeLabelsFromNode("g"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.allEdgeLabelsFromNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodesFromNodeViaEdgeLabeled(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNodeViaEdgeLabeled("a", ""));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "e",
                sample1.allNodesFromNodeViaEdgeLabeled("d", ""));
        // single node, empty label, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNodeViaEdgeLabeled("d", "x"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "g",
                sample1.allNodesFromNodeViaEdgeLabeled("f", "h"));
        // single node, non-empty label, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNodeViaEdgeLabeled("f", "x"));
        // multiple nodes
        assertStringsAsLinesEquals("1\n" +
                        "m3",
                sample1.allNodesFromNodeViaEdgeLabeled("o", ""));
        assertStringsAsLinesEquals("2\n" +
                        "m1\n" +
                        "m2",
                sample1.allNodesFromNodeViaEdgeLabeled("o", "field"));
        // multiple nodes, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNodeViaEdgeLabeled("o", "x"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                sample1.allNodesFromNodeViaEdgeLabeled("c", "cycle"));
        // self cyclic, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNodeViaEdgeLabeled("c", "x"));
        // toNode that is no fromNode
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNodeViaEdgeLabeled("g", "h"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.allNodesFromNodeViaEdgeLabeled("x", ""));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodesToNode(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.allNodesToNode("a"));
        // single node
        assertStringsAsLinesEquals("1\n" +
                        "d",
                sample1.allNodesToNode("e"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                sample1.allNodesToNode("c"));
        // fromNode that is no toNode
        assertStringsAsLinesEquals("0\n", sample1.allNodesToNode("d"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.allNodesToNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgeLabelsToNode(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.allEdgeLabelsToNode("a"));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "",
                sample1.allEdgeLabelsToNode("e"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "h",
                sample1.allEdgeLabelsToNode("g"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "cycle",
                sample1.allEdgeLabelsToNode("c"));
        // fromNode that is no toNode
        assertStringsAsLinesEquals("0\n", sample1.allEdgeLabelsToNode("d"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.allEdgeLabelsToNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allNodesToNodeViaEdgeLabeled(StringGraph sample1) {
        // node without edges
        assertStringsAsLinesEquals("0\n", sample1.allNodesToNodeViaEdgeLabeled("a", ""));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "d",
                sample1.allNodesToNodeViaEdgeLabeled("e", ""));
        // single node, empty label, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.allNodesToNodeViaEdgeLabeled("e", "x"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "f",
                sample1.allNodesToNodeViaEdgeLabeled("g", "h"));
        // single node, non-empty label, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.allNodesToNodeViaEdgeLabeled("g", "x"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                sample1.allNodesToNodeViaEdgeLabeled("c", "cycle"));
        // self cyclic, query wrong label
        assertStringsAsLinesEquals("0\n", sample1.allNodesToNodeViaEdgeLabeled("c", "x"));
        // fromNode that is no toNode
        assertStringsAsLinesEquals("0\n", sample1.allNodesToNodeViaEdgeLabeled("f", "h"));
        // missing node
        assertStringsAsLinesEquals("0\n", sample1.allNodesToNodeViaEdgeLabeled("x", ""));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgesWith(StringGraph sample1) {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("3\n" +
                        "\"o\" -> \"m3\"\n" +
                        "\"o\" -> \"m1\" : \"field\"\n" +
                        "\"o\" -> \"m2\" : \"field\"",
                sample1.allEdgesWith(e -> e.getToNode().startsWith("m")));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"f\" -> \"g\" : \"h\"",
                sample1.allEdgesWith(e -> e.getLabel().equals("h")));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.allEdgesWith(e -> false));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgesLabeled(StringGraph sample1) {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("2\n" +
                        "\"d\" -> \"e\"\n" +
                        "\"o\" -> \"m3\"",
                sample1.allEdgesLabeled(""));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("2\n" +
                        "\"c\" -> \"c\" : \"cycle\"\n" +
                        "\"i\" -> \"i\" : \"cycle\"",
                sample1.allEdgesLabeled("cycle"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.allEdgesLabeled("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgesFromNode(StringGraph sample1) {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.allEdgesFromNode("a"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"c\" -> \"c\" : \"cycle\"",
                sample1.allEdgesFromNode("c"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"d\" -> \"e\"",
                sample1.allEdgesFromNode("d"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("3\n" +
                        "\"o\" -> \"m3\"\n" +
                        "\"o\" -> \"m1\" : \"field\"\n" +
                        "\"o\" -> \"m2\" : \"field\"",
                sample1.allEdgesFromNode("o"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.allEdgesFromNode("x"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphSample1Provider")
    void allEdgesToNode(StringGraph sample1) {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.allEdgesToNode("a"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"c\" -> \"c\" : \"cycle\"",
                sample1.allEdgesToNode("c"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"d\" -> \"e\"",
                sample1.allEdgesToNode("e"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                sample1.allEdgesFromNode("x"));
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
                "\"d\" -> \"e\" : \"f\"", graph.allEdges());
    }
}
