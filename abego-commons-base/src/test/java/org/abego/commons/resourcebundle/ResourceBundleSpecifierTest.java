/*
 * MIT License
 *
 * Copyright (c) 2018 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.resourcebundle;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.abego.commons.resourcebundle.ResourceBundleSpecifier.resourceBundleSpecifier;
import static org.abego.commons.resourcebundle.ResourceBundleUtil.PROPERTIES_FILE_EXTENSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceBundleSpecifierTest {


    @Test
    void resourceBundleSpecifier_complete() {
        File file = new File("sub/dir/Sample_de_CH_Unix.properties");

        ResourceBundleSpecifier bs = resourceBundleSpecifier(file);

        assertEquals("Sample", bs.bundleBaseName());
        assertEquals("de", bs.language());
        assertEquals("CH", bs.country());
        assertEquals("Unix", bs.platform());
        assertEquals(PROPERTIES_FILE_EXTENSION, bs.fileExtension());
    }

    @Test
    void resourceBundleSpecifier_noPlatform() {
        File file = new File("sub/dir/Sample_de_CH.properties");

        ResourceBundleSpecifier bs = resourceBundleSpecifier(file);

        assertEquals("Sample", bs.bundleBaseName());
        assertEquals("de", bs.language());
        assertEquals("CH", bs.country());
        assertEquals("", bs.platform());
        assertEquals(PROPERTIES_FILE_EXTENSION, bs.fileExtension());
    }

    @Test
    void resourceBundleSpecifier_language() {
        File file = new File("sub/dir/Sample_de.properties");

        ResourceBundleSpecifier bs = resourceBundleSpecifier(file);

        assertEquals("Sample", bs.bundleBaseName());
        assertEquals("de", bs.language());
        assertEquals("", bs.country());
        assertEquals("", bs.platform());
        assertEquals(PROPERTIES_FILE_EXTENSION, bs.fileExtension());
    }

    @Test
    void resourceBundleSpecifier_minimal() {
        File file = new File("sub/dir/Sample.properties");

        ResourceBundleSpecifier bs = resourceBundleSpecifier(file);

        assertEquals("Sample", bs.bundleBaseName());
        assertEquals("", bs.language());
        assertEquals("", bs.country());
        assertEquals("", bs.platform());
        assertEquals(PROPERTIES_FILE_EXTENSION, bs.fileExtension());
    }

    @Test
    void resourceBundleSpecifier_wrongExtension() {
        File file = new File("Sample.txt"); // no "*.properties" file

        assertThrows(IllegalArgumentException.class,
                () -> resourceBundleSpecifier(file));
    }

    @Test
    void resourceBundleSpecifier_equals() {
        File file = new File("sub/dir/Sample.properties");
        ResourceBundleSpecifier bs1a = resourceBundleSpecifier(file);
        ResourceBundleSpecifier bs1b = resourceBundleSpecifier(file);
        File file2 = new File("sub/dir/Sample_de.properties");
        ResourceBundleSpecifier bs2 = resourceBundleSpecifier(file2);


        assertEquals(bs1a, bs1a);
        assertEquals(bs1a, bs1b);
        assertEquals(bs1b, bs1a);

        assertEquals(bs2, bs2);

        assertNotEquals(bs1a, bs2);
    }

    @Test
    void resourceBundleSpecifier_hashCode() {
        File file = new File("sub/dir/Sample.properties");
        ResourceBundleSpecifier bs1a = resourceBundleSpecifier(file);
        ResourceBundleSpecifier bs1b = resourceBundleSpecifier(file);
        File file2 = new File("sub/dir/Sample_de.properties");
        ResourceBundleSpecifier bs2 = resourceBundleSpecifier(file2);


        assertEquals(bs1a.hashCode(), bs1a.hashCode());
        assertEquals(bs1a.hashCode(), bs1b.hashCode());
        assertEquals(bs1b.hashCode(), bs1a.hashCode());

        assertEquals(bs2.hashCode(), bs2.hashCode());

        assertNotEquals(bs1a.hashCode(), bs2.hashCode());
    }


}