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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringGraphTest {
    private static final StringGraph SAMPLE_1 =
            constructSample1(StringGraphs.createStringGraphBuilder()).build();

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
        return SAMPLE_1;
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

    @Test
    void allNodes() {
        assertEqualsToAllNodesOfSample1(getSample1().allNodes());
    }

    @Test
    void allEdges() {
        assertEqualsToAllEdgesOfSample1(getSample1().allEdges());
    }

    @Test
    void allFromNodes() {
        assertNodesEquals("5\n" +
                        "c\n" +
                        "d\n" +
                        "f\n" +
                        "i\n" +
                        "o",
                getSample1().allFromNodes());
    }

    @Test
    void allToNodes() {
        assertNodesEquals("7\n" +
                        "c\n" +
                        "e\n" +
                        "g\n" +
                        "i\n" +
                        "m1\n" +
                        "m2\n" +
                        "m3",
                getSample1().allToNodes());
    }

    @Test
    void allEdgeLabels() {
        assertStringsAsLinesEquals("4\n" +
                        "\n" +
                        "cycle\n" +
                        "field\n" +
                        "h",
                getSample1().allEdgeLabels());
    }

    @Test
    void allNodesFromNode() {
        // node without edges
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNode("a"));
        // single node
        assertStringsAsLinesEquals("1\n" +
                        "g",
                getSample1().allNodesFromNode("f"));
        // multiple nodes
        assertStringsAsLinesEquals("3\n" +
                        "m1\n" +
                        "m2\n" +
                        "m3",
                getSample1().allNodesFromNode("o"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                getSample1().allNodesFromNode("c"));
        // toNode that is no fromNode
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNode("g"));
        // missing node
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNode("x"));
    }

    @Test
    void allEdgeLabelsFromNode() {
        // node without edges
        assertStringsAsLinesEquals("0\n", getSample1().allEdgeLabelsFromNode("a"));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "",
                getSample1().allEdgeLabelsFromNode("d"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "h",
                getSample1().allEdgeLabelsFromNode("f"));
        // multiple nodes
        assertStringsAsLinesEquals("2\n" +
                        "\n" +
                        "field",
                getSample1().allEdgeLabelsFromNode("o"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "cycle",
                getSample1().allEdgeLabelsFromNode("c"));
        // toNode that is no fromNode
        assertStringsAsLinesEquals("0\n", getSample1().allEdgeLabelsFromNode("g"));
        // missing node
        assertStringsAsLinesEquals("0\n", getSample1().allEdgeLabelsFromNode("x"));
    }

    @Test
    void allNodesFromNodeViaEdgeLabeled() {
        // node without edges
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNodeViaEdgeLabeled("a", ""));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "e",
                getSample1().allNodesFromNodeViaEdgeLabeled("d", ""));
        // single node, empty label, query wrong label
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNodeViaEdgeLabeled("d", "x"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "g",
                getSample1().allNodesFromNodeViaEdgeLabeled("f", "h"));
        // single node, non-empty label, query wrong label
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNodeViaEdgeLabeled("f", "x"));
        // multiple nodes
        assertStringsAsLinesEquals("1\n" +
                        "m3",
                getSample1().allNodesFromNodeViaEdgeLabeled("o", ""));
        assertStringsAsLinesEquals("2\n" +
                        "m1\n" +
                        "m2",
                getSample1().allNodesFromNodeViaEdgeLabeled("o", "field"));
        // multiple nodes, query wrong label
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNodeViaEdgeLabeled("o", "x"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                getSample1().allNodesFromNodeViaEdgeLabeled("c", "cycle"));
        // self cyclic, query wrong label
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNodeViaEdgeLabeled("c", "x"));
        // toNode that is no fromNode
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNodeViaEdgeLabeled("g", "h"));
        // missing node
        assertStringsAsLinesEquals("0\n", getSample1().allNodesFromNodeViaEdgeLabeled("x", ""));
    }

    @Test
    void allNodesToNode() {
        // node without edges
        assertStringsAsLinesEquals("0\n", getSample1().allNodesToNode("a"));
        // single node
        assertStringsAsLinesEquals("1\n" +
                        "d",
                getSample1().allNodesToNode("e"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                getSample1().allNodesToNode("c"));
        // fromNode that is no toNode
        assertStringsAsLinesEquals("0\n", getSample1().allNodesToNode("d"));
        // missing node
        assertStringsAsLinesEquals("0\n", getSample1().allNodesToNode("x"));
    }

    @Test
    void allEdgeLabelsToNode() {
        // node without edges
        assertStringsAsLinesEquals("0\n", getSample1().allEdgeLabelsToNode("a"));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "",
                getSample1().allEdgeLabelsToNode("e"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "h",
                getSample1().allEdgeLabelsToNode("g"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "cycle",
                getSample1().allEdgeLabelsToNode("c"));
        // fromNode that is no toNode
        assertStringsAsLinesEquals("0\n", getSample1().allEdgeLabelsToNode("d"));
        // missing node
        assertStringsAsLinesEquals("0\n", getSample1().allEdgeLabelsToNode("x"));
    }

    @Test
    void allNodesToNodeViaEdgeLabeled() {
        // node without edges
        assertStringsAsLinesEquals("0\n", getSample1().allNodesToNodeViaEdgeLabeled("a", ""));
        // single node, empty label
        assertStringsAsLinesEquals("1\n" +
                        "d",
                getSample1().allNodesToNodeViaEdgeLabeled("e", ""));
        // single node, empty label, query wrong label
        assertStringsAsLinesEquals("0\n", getSample1().allNodesToNodeViaEdgeLabeled("e", "x"));
        // single node, non-empty label
        assertStringsAsLinesEquals("1\n" +
                        "f",
                getSample1().allNodesToNodeViaEdgeLabeled("g", "h"));
        // single node, non-empty label, query wrong label
        assertStringsAsLinesEquals("0\n", getSample1().allNodesToNodeViaEdgeLabeled("g", "x"));
        // self cyclic
        assertStringsAsLinesEquals("1\n" +
                        "c",
                getSample1().allNodesToNodeViaEdgeLabeled("c", "cycle"));
        // self cyclic, query wrong label
        assertStringsAsLinesEquals("0\n", getSample1().allNodesToNodeViaEdgeLabeled("c", "x"));
        // fromNode that is no toNode
        assertStringsAsLinesEquals("0\n", getSample1().allNodesToNodeViaEdgeLabeled("f", "h"));
        // missing node
        assertStringsAsLinesEquals("0\n", getSample1().allNodesToNodeViaEdgeLabeled("x", ""));
    }

    @Test
    void allEdgesWith() {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("3\n" +
                        "\"o\" -> \"m3\"\n" +
                        "\"o\" -> \"m1\" : \"field\"\n" +
                        "\"o\" -> \"m2\" : \"field\"",
                getSample1().allEdgesWith(e -> e.getToNode().startsWith("m")));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"f\" -> \"g\" : \"h\"",
                getSample1().allEdgesWith(e -> e.getLabel().equals("h")));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                getSample1().allEdgesWith(e -> false));
    }

    @Test
    void allEdgesLabeled() {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("2\n" +
                        "\"d\" -> \"e\"\n" +
                        "\"o\" -> \"m3\"",
                getSample1().allEdgesLabeled(""));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("2\n" +
                        "\"c\" -> \"c\" : \"cycle\"\n" +
                        "\"i\" -> \"i\" : \"cycle\"",
                getSample1().allEdgesLabeled("cycle"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                getSample1().allEdgesLabeled("x"));
    }

    @Test
    void allEdgesFromNode() {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                getSample1().allEdgesFromNode("a"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"c\" -> \"c\" : \"cycle\"",
                getSample1().allEdgesFromNode("c"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"d\" -> \"e\"",
                getSample1().allEdgesFromNode("d"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("3\n" +
                        "\"o\" -> \"m3\"\n" +
                        "\"o\" -> \"m1\" : \"field\"\n" +
                        "\"o\" -> \"m2\" : \"field\"",
                getSample1().allEdgesFromNode("o"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                getSample1().allEdgesFromNode("x"));
    }

    @Test
    void allEdgesToNode() {
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                getSample1().allEdgesToNode("a"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"c\" -> \"c\" : \"cycle\"",
                getSample1().allEdgesToNode("c"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("1\n" +
                        "\"d\" -> \"e\"",
                getSample1().allEdgesToNode("e"));
        EdgeDefaultTest.assertEdgesEqualsIgnoreOrder("0\n",
                getSample1().allEdgesFromNode("x"));
    }

    @Test
    void equalsAndHashcode() {
        StringGraph g1 = getSample1();
        StringGraph g2 = constructSample1(StringGraphs.createStringGraphBuilder()).build();
        int h1 = g1.hashCode();
        int h2 = g2.hashCode();

        assertNotEquals(g1, "not a graph");
        assertNotEquals(g1, null);
        assertEquals(g1, g1);
        assertEquals(g1, g2);
        assertEquals(h1, h2);
    }

    @Test
    void toStringTest() {
        String s = getSample1().toString();

        assertTrue(s.startsWith("StringGraphDefault{nodes="));
    }
}
