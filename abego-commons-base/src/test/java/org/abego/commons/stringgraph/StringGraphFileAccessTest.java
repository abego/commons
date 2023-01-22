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

import org.abego.commons.io.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringGraphFileAccessTest {

    @Test
    void readMissingFile(@TempDir File tempDir) {
        File file = new File(tempDir, "missing.graph");

        StringGraphFileException e = assertThrows(StringGraphFileException.class,
                () -> StringGraphs.readStringGraph(file));
        assertEquals(file, e.getFile());
    }

    @Test
    void readEmptyFile(@TempDir File tempDir) {
        File file = new File(tempDir, "empty");
        FileUtil.ensureFileExists(file);

        StringGraphException e = assertThrows(StringGraphException.class,
                () -> StringGraphs.readStringGraph(file));
        assertTrue(e.getMessage().startsWith("Error when reading graph from "));
    }

    @Test
    void readFileWithWrongFileFormatName(@TempDir File tempDir) throws IOException {
        File file = new File(tempDir, "some.graph");
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(Files.newOutputStream(file.toPath()));
        objectOutputStream.writeObject("wrong header");
        objectOutputStream.close();

        StringGraphException e = assertThrows(StringGraphException.class,
                () -> StringGraphs.readStringGraph(file));
        assertEquals("Invalid file format. Expected header 'org.abego.commons.stringgraph.StringGraphFile', got 'wrong header'", e.getMessage());
    }

    @Test
    void readFileWithWrongFileFormatVersion(@TempDir File tempDir) throws IOException {
        File file = new File(tempDir, "some.graph");
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(Files.newOutputStream(file.toPath()));
        objectOutputStream.writeObject(StringGraphFileAccess.FILE_FORMAT_NAME);
        objectOutputStream.writeInt(StringGraphFileAccess.FILE_FORMAT_VERSION.majorNumber + 1);
        objectOutputStream.writeInt(StringGraphFileAccess.FILE_FORMAT_VERSION.minorNumber);
        objectOutputStream.close();

        StringGraphException e = assertThrows(StringGraphException.class,
                () -> StringGraphs.readStringGraph(file));
        assertEquals("Incompatible file version. Expected '1', got '2'", e.getMessage());
    }

    @Test
    void constructStringGraphFromFile(@TempDir File tempDir) {
        StringGraph graph = StringGraphTest.getAnySample1();
        StringGraphException e = assertThrows(StringGraphException.class,
                () -> StringGraphs.writeStringGraph(graph, tempDir));
        assertTrue(e.getMessage().startsWith("Error when writing graph to "));
    }
}
