/*
 * MIT License
 *
 * Copyright (c) 2020 Udo Borkowski, (ub@abego.org)
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

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.abego.commons.resourcebundle.ResourceBundleSpecifierDefault.newResourceBundleSpecifierDefault;
import static org.abego.commons.resourcebundle.ResourceBundleUtil.PROPERTIES_FILE_EXTENSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceBundleUtilTest {

    ResourceBundleSpecifier newResourceBundleSpecifier(File file) {
        return newResourceBundleSpecifierDefault(file);
    }

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, ResourceBundleUtil::new);
    }


    @Test
    void newResourceBundleSpecifier_complete() {
        File file = new File("sub/dir/Sample_de_CH_Unix.properties");

        ResourceBundleSpecifier bs = newResourceBundleSpecifier(file);

        assertEquals("Sample", bs.getBundleBaseName());
        assertEquals("de", bs.getLanguage());
        assertEquals("CH", bs.getCountry());
        assertEquals("Unix", bs.getPlatform());
        assertEquals(PROPERTIES_FILE_EXTENSION, bs.getFileExtension());
    }

    @Test
    void newResourceBundleSpecifier_noPlatform() {
        File file = new File("sub/dir/Sample_de_CH.properties");

        ResourceBundleSpecifier bs = newResourceBundleSpecifier(file);

        assertEquals("Sample", bs.getBundleBaseName());
        assertEquals("de", bs.getLanguage());
        assertEquals("CH", bs.getCountry());
        assertEquals("", bs.getPlatform());
        assertEquals(PROPERTIES_FILE_EXTENSION, bs.getFileExtension());
    }

    @Test
    void newResourceBundleSpecifier_language() {
        File file = new File("sub/dir/Sample_de.properties");

        ResourceBundleSpecifier bs = newResourceBundleSpecifier(file);

        assertEquals("Sample", bs.getBundleBaseName());
        assertEquals("de", bs.getLanguage());
        assertEquals("", bs.getCountry());
        assertEquals("", bs.getPlatform());
        assertEquals(PROPERTIES_FILE_EXTENSION, bs.getFileExtension());
    }

    @Test
    void newResourceBundleSpecifier_minimal() {
        File file = new File("sub/dir/Sample.properties");

        ResourceBundleSpecifier bs = newResourceBundleSpecifier(file);

        assertEquals("Sample", bs.getBundleBaseName());
        assertEquals("", bs.getLanguage());
        assertEquals("", bs.getCountry());
        assertEquals("", bs.getPlatform());
        assertEquals(PROPERTIES_FILE_EXTENSION, bs.getFileExtension());
    }

    @Test
    void newResourceBundleSpecifier_wrongExtension() {
        File file = new File("Sample.txt"); // no "*.properties" file

        assertThrows(IllegalArgumentException.class,
                () -> newResourceBundleSpecifier(file));
    }

    @Test
    void newResourceBundleSpecifier_equals() {
        File file = new File("sub/dir/Sample.properties");
        ResourceBundleSpecifier bs1a = newResourceBundleSpecifier(file);
        ResourceBundleSpecifier bs1b = newResourceBundleSpecifier(file);
        File file2 = new File("sub/dir/Sample_de.properties");
        ResourceBundleSpecifier bs2 = newResourceBundleSpecifier(file2);


        assertEquals(bs1a, bs1a);
        assertEquals(bs1a, bs1b);
        assertEquals(bs1b, bs1a);

        assertEquals(bs2, bs2);

        assertNotEquals(bs1a, bs2);
    }

    @Test
    void newResourceBundleSpecifier_hashCode() {
        File file = new File("sub/dir/Sample.properties");
        ResourceBundleSpecifier bs1a = newResourceBundleSpecifier(file);
        ResourceBundleSpecifier bs1b = newResourceBundleSpecifier(file);
        File file2 = new File("sub/dir/Sample_de.properties");
        ResourceBundleSpecifier bs2 = newResourceBundleSpecifier(file2);


        assertEquals(bs1a.hashCode(), bs1a.hashCode());
        assertEquals(bs1a.hashCode(), bs1b.hashCode());
        assertEquals(bs1b.hashCode(), bs1a.hashCode());

        assertEquals(bs2.hashCode(), bs2.hashCode());

        assertNotEquals(bs1a.hashCode(), bs2.hashCode());
    }

}