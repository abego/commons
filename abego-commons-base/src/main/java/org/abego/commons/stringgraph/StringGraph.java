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

import java.util.function.Predicate;

import static org.abego.commons.seq.SeqUtil.newSeq;
import static org.abego.commons.seq.SeqUtil.newSeqUniqueItems;

public interface StringGraph {
    Seq<String> allNodes();

    Seq<Edge> allEdges();

    default Seq<String> allFromNodes() {
        return newSeqUniqueItems(allEdges().map(Edge::getFromNode));
    }

    default Seq<String> allToNodes() {
        return newSeqUniqueItems(allEdges().map(Edge::getToNode));
    }

    default Seq<String> allEdgeLabels() {
        return newSeqUniqueItems(allEdges().map(Edge::getLabel));
    }

    default Seq<String> allNodesFromNode(String fromNode) {
        return newSeqUniqueItems(allEdgesFromNode(fromNode).map(Edge::getToNode));
    }

    default Seq<String> allEdgeLabelsFromNode(String fromNode) {
        return newSeqUniqueItems(allEdgesFromNode(fromNode).map(Edge::getLabel));
    }

    default Seq<String> allNodesFromNodeViaEdgeLabeled(String fromNode, String edgeLabel) {
        //noinspection CallToSuspiciousStringMethod
        return newSeqUniqueItems(
                allEdgesWith(edge ->
                        edge.getFromNode().equals(fromNode) &&
                                edge.getLabel().equals(edgeLabel))
                        .map(Edge::getToNode));
    }

    default Seq<String> allNodesToNode(String toNode) {
        return newSeqUniqueItems(allEdgesToNode(toNode).map(Edge::getFromNode));
    }

    default Seq<String> allEdgeLabelsToNode(String toNode) {
        return newSeqUniqueItems(allEdgesToNode(toNode).map(Edge::getLabel));
    }

    default Seq<String> allNodesToNodeViaEdgeLabeled(String toNode, String edgeLabel) {
        //noinspection CallToSuspiciousStringMethod
        return newSeqUniqueItems(
                allEdgesWith(edge ->
                        edge.getToNode().equals(toNode) &&
                                edge.getLabel().equals(edgeLabel))
                        .map(Edge::getFromNode));
    }

    default Seq<Edge> allEdgesWith(Predicate<Edge> edgePredicate) {
        return newSeq(allEdges().filter(edgePredicate));
    }

    default Seq<Edge> allEdgesLabeled(String edgeLabel) {
        //noinspection CallToSuspiciousStringMethod
        return allEdgesWith(edge -> edge.getLabel().equals(edgeLabel));
    }

    default Seq<Edge> allEdgesFromNode(String fromNode) {
        //noinspection CallToSuspiciousStringMethod
        return allEdgesWith(edge -> edge.getFromNode().equals(fromNode));
    }

    default Seq<Edge> allEdgesToNode(String toNode) {
        //noinspection CallToSuspiciousStringMethod
        return allEdgesWith(edge -> edge.getToNode().equals(toNode));
    }
}
