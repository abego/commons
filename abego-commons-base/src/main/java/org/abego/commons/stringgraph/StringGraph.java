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
    Seq<String> nodes();

    Seq<Edge> edges();

    default Seq<String> fromNodes() {
        return newSeqUniqueItems(edges().map(Edge::getFromNode));
    }

    default Seq<String> toNodes() {
        return newSeqUniqueItems(edges().map(Edge::getToNode));
    }

    default Seq<String> edgeLabels() {
        return newSeqUniqueItems(edges().map(Edge::getLabel));
    }

    default Seq<String> nodesFromNode(String fromNode) {
        return newSeqUniqueItems(edgesFromNode(fromNode).map(Edge::getToNode));
    }

    default Seq<String> edgeLabelsFromNode(String fromNode) {
        return newSeqUniqueItems(edgesFromNode(fromNode).map(Edge::getLabel));
    }

    default Seq<String> nodesFromNodeViaEdgeLabeled(String fromNode, String edgeLabel) {
        //noinspection CallToSuspiciousStringMethod
        return newSeqUniqueItems(
                edgesWith(edge ->
                        edge.getFromNode().equals(fromNode) &&
                                edge.getLabel().equals(edgeLabel))
                        .map(Edge::getToNode));
    }

    default Seq<String> nodesToNode(String toNode) {
        return newSeqUniqueItems(edgesToNode(toNode).map(Edge::getFromNode));
    }

    default Seq<String> edgeLabelsToNode(String toNode) {
        return newSeqUniqueItems(edgesToNode(toNode).map(Edge::getLabel));
    }

    default Seq<String> nodesToNodeViaEdgeLabeled(String toNode, String edgeLabel) {
        //noinspection CallToSuspiciousStringMethod
        return newSeqUniqueItems(
                edgesWith(edge ->
                        edge.getToNode().equals(toNode) &&
                                edge.getLabel().equals(edgeLabel))
                        .map(Edge::getFromNode));
    }

    default Seq<Edge> edgesWith(Predicate<Edge> edgePredicate) {
        return newSeq(edges().filter(edgePredicate));
    }

    default Seq<Edge> edgesLabeled(String edgeLabel) {
        //noinspection CallToSuspiciousStringMethod
        return edgesWith(edge -> edge.getLabel().equals(edgeLabel));
    }

    default Seq<Edge> edgesFromNode(String fromNode) {
        //noinspection CallToSuspiciousStringMethod
        return edgesWith(edge -> edge.getFromNode().equals(fromNode));
    }

    default Seq<Edge> edgesToNode(String toNode) {
        //noinspection CallToSuspiciousStringMethod
        return edgesWith(edge -> edge.getToNode().equals(toNode));
    }
}
