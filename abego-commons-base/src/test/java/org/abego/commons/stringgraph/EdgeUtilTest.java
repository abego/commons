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

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.seq.Seq;
import org.junit.jupiter.api.Test;

import static org.abego.commons.seq.SeqUtil.newSeq;
import static org.abego.commons.stringgraph.EdgeDefaultTest.assertEdgesEquals;
import static org.abego.commons.stringgraph.EdgeDefaultTest.getEdgeSample;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EdgeUtilTest {
    private final static Seq<Edge> EDGES_TO_SORT = newSeq(
            EdgeDefault.createEdge("a", "b", "c"),
            EdgeDefault.createEdge("", "b", "c"),
            EdgeDefault.createEdge("a", "", "c"),
            EdgeDefault.createEdge("a", "b", ""),
            EdgeDefault.createEdge("a", "b", "b"),
            EdgeDefault.createEdge("a", "b", "a"),
            EdgeDefault.createEdge("a", "a", "a"),
            EdgeDefault.createEdge("b", "a", ""),
            EdgeDefault.createEdge("b", "b", ""),
            EdgeDefault.createEdge("b", "b", "c")
    );

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, EdgeUtil::new);
    }

    @Test
    void edgeText() {
        String edgeText = EdgeUtil.calcEdgeText(getEdgeSample());

        assertEquals("\"f\" -> \"t\" : \"lbl\"", edgeText);
    }

    @Test
    void getComparator() {

        // use sorting to call "compareTo" repeatedly, avoiding writing
        // multiple "compare" calls explicitly
        Seq<Edge> sortedEdges = EDGES_TO_SORT.sorted(EdgeUtil.getComparator());

        // the default sort order is (fromNode, label, toLabel)
        assertEdgesEquals("10\n" +
                "\"\" -> \"b\" : \"c\"\n" +
                "\"a\" -> \"b\"\n" +
                "\"a\" -> \"a\" : \"a\"\n" +
                "\"a\" -> \"b\" : \"a\"\n" +
                "\"a\" -> \"b\" : \"b\"\n" +
                "\"a\" -> \"\" : \"c\"\n" +
                "\"a\" -> \"b\" : \"c\"\n" +
                "\"b\" -> \"a\"\n" +
                "\"b\" -> \"b\"\n" +
                "\"b\" -> \"b\" : \"c\"", sortedEdges);
    }

    @Test
    void getComparatorLabelLast() {

        // use sorting to call "compareTo" repeatedly, avoiding writing
        // multiple "compare" calls explicitly
        Seq<Edge> sortedEdges =
                EDGES_TO_SORT.sorted(EdgeUtil.getComparatorLabelLast());

        // the sort order is (fromNode, toLabel, label)
        assertEdgesEquals("10\n" +
                "\"\" -> \"b\" : \"c\"\n" +
                "\"a\" -> \"\" : \"c\"\n" +
                "\"a\" -> \"a\" : \"a\"\n" +
                "\"a\" -> \"b\"\n" +
                "\"a\" -> \"b\" : \"a\"\n" +
                "\"a\" -> \"b\" : \"b\"\n" +
                "\"a\" -> \"b\" : \"c\"\n" +
                "\"b\" -> \"a\"\n" +
                "\"b\" -> \"b\"\n" +
                "\"b\" -> \"b\" : \"c\"", sortedEdges);
    }
}
