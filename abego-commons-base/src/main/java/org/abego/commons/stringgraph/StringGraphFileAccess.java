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

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

class StringGraphFileAccess {
    public static final String FILE_FORMAT_NAME =
            "org.abego.commons.stringgraph.StringGraphFile";
    public static final FileFormatVersion FILE_FORMAT_VERSION =
            new FileFormatVersion(1, 0);

    private static final String NODES_TAG = "nodes"; //NON-NLS
    private static final String EDGES_TAG = "edges"; //NON-NLS
    private static final String END_TAG = "end"; //NON-NLS

    private StringGraphFileAccess() {
    }

    //region Writing
    public static void writeStringGraph(StringGraph stringGraph, File file) {
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(Files.newOutputStream(file.toPath()));

            writeFileFormat(objectOutputStream);
            writeNodes(stringGraph, objectOutputStream);
            writeEdges(stringGraph, objectOutputStream);
            writeEndTag(objectOutputStream);

            objectOutputStream.close();
        } catch (Exception e) {
            throw new StringGraphException(
                    String.format("Error when writing graph to %s: %s", //NON-NLS
                            file.getAbsolutePath(), e.getMessage()), e);
        }
    }

    private static void writeFileFormat(ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.writeObject(FILE_FORMAT_NAME);
        objectOutputStream.writeInt(FILE_FORMAT_VERSION.majorNumber);
        objectOutputStream.writeInt(FILE_FORMAT_VERSION.minorNumber);
    }

    private static void writeNodes(
            StringGraph stringGraph, ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.writeObject(NODES_TAG);
        Seq<String> allNodes = stringGraph.allNodes();
        objectOutputStream.writeInt(allNodes.getSize());
        for (String s : allNodes) {
            objectOutputStream.writeObject(s);
        }
    }

    private static void writeEdges(
            StringGraph stringGraph, ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.writeObject(EDGES_TAG);
        Seq<Edge> allEdges = stringGraph.allEdges();
        objectOutputStream.writeInt(allEdges.getSize());
        for (Edge e : allEdges) {
            objectOutputStream.writeObject(e.getFromNode());
            objectOutputStream.writeObject(e.getToNode());
            objectOutputStream.writeObject(e.getLabel());
        }
    }

    private static void writeEndTag(ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.writeObject(END_TAG);
    }

    public static void constructStringGraphFromFile(
            StringGraphConstructing graphConstructing,
            File file) {
        try {
            verifyRegularFileExists(file);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(Files.newInputStream(file.toPath()));

            FileFormatVersion version = readFileFormat(objectInputStream);
            // We can only read files that have the same major as we
            // (see JavaDoc FileFormatVersion)
            if (version.majorNumber != FILE_FORMAT_VERSION.majorNumber) {
                throw new StringGraphException(
                        String.format("Incompatible file version. Expected '%d', got '%d'", //NON-NLS
                                FILE_FORMAT_VERSION.majorNumber, version.majorNumber));
            }

            readFileBody(objectInputStream, graphConstructing);

            objectInputStream.close();
        } catch (StringGraphException e) {
            throw e;
        } catch (Exception e) {
            throw new StringGraphException(
                    String.format("Error when reading graph from %s: %s", //NON-NLS
                            file.getAbsolutePath(), e.getMessage()), e);
        }
    }

    //endregion

    //region Reading

    private static FileFormatVersion readFileFormat(
            ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String formatName = (String) objectInputStream.readObject();
        //noinspection CallToSuspiciousStringMethod
        if (!formatName.equals(FILE_FORMAT_NAME)) {
            throw new StringGraphException(
                    String.format("Invalid file format. Expected header '%s', got '%s'", //NON-NLS
                            FILE_FORMAT_NAME, formatName));
        }

        int major = objectInputStream.readInt();
        int minor = objectInputStream.readInt();
        return new FileFormatVersion(major, minor);
    }

    private static void readFileBody(
            ObjectInputStream objectInputStream,
            StringGraphConstructing graphConstucting) throws Exception {

        boolean fileEndReached = false;
        do {
            String tag = (String) objectInputStream.readObject();
            switch (tag) {
                case NODES_TAG:
                    readNodesBlock(objectInputStream, graphConstucting);
                    break;
                case EDGES_TAG:
                    readEdgesBlock(objectInputStream, graphConstucting);
                    break;
                case END_TAG:
                    fileEndReached = true;
                    break;
                default:
                    // to be able to read future file formats
                    // we ignore any tag we don't know.
                    // Future versions of this class will ensure to increase the
                    // major version number when the file format changes in a
                    // way that are incompatible to this approach.
                    break;
            }
        } while (!fileEndReached);
    }

    private static void readNodesBlock(
            ObjectInputStream objectInputStream, StringGraphConstructing graphConstucting)
            throws Exception {
        int n = objectInputStream.readInt();
        for (int i = 0; i < n; i++) {
            graphConstucting.addNode((String) objectInputStream.readObject());
        }
    }

    private static void readEdgesBlock(
            ObjectInputStream objectInputStream, StringGraphConstructing graphConstucting)
            throws Exception {
        int n = objectInputStream.readInt();
        for (int i = 0; i < n; i++) {
            String fromNode = (String) objectInputStream.readObject();
            String toNode = (String) objectInputStream.readObject();
            String label = (String) objectInputStream.readObject();
            graphConstucting.addEdge(fromNode, toNode, label);
        }
    }

    private static void verifyRegularFileExists(File file) {
        if (!file.exists()) {
            throw new StringGraphFileException(
                    String.format("File missing: '%s'",  //NON-NLS
                            file.getAbsolutePath()), file);
        }
    }

    /**
     * Indicates the version of the file format
     * <p>
     * With every release of this class that changes the file format the
     * version numbers are changed according to the following rules,
     * assuming the previous version number was {@code m.n}:
     * <ul>
     *     <li>the new file format is incompatible with at least one
     *     release of this class with {@code majorNumber == m}:
     *     set {@code majorNumber} to {@code m + 1} and
     *     {@code minorNumber} to {@code 0}</li>
     *     <li>otherwise: leave {@code majorNumber} unchanged and
     *     set {@code minorNumber} to {@code n + 1}</li>
     * </ul>
     * This means that a release of this class with a {@code majorNumber == m}
     * can read files written by any release of this class that shares the
     * same {@code majorNumber == m}.
     */
    public static class FileFormatVersion {
        public final int majorNumber;
        public final int minorNumber;

        private FileFormatVersion(int majorNumber, int minorNumber) {
            this.majorNumber = majorNumber;
            this.minorNumber = minorNumber;
        }
    }
    //endregion
}
