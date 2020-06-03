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

package org.abego.commons.io;

import org.abego.commons.TestData;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.resourcebundle.ResourceBundleUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.abego.commons.TestData.SAMPLE_PROPERTIES_KEY_1;
import static org.abego.commons.TestData.SAMPLE_PROPERTIES_KEY_2;
import static org.abego.commons.TestData.SAMPLE_PROPERTIES_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_PROPERTIES_VALUE_1;
import static org.abego.commons.TestData.SAMPLE_PROPERTIES_VALUE_2;
import static org.abego.commons.io.FileUtil.tempFileForRunFromResource;
import static org.abego.commons.io.PropertiesIOUtil.readProperties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesIOUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, PropertiesIOUtil::new);
    }

    @Test
    void readProperties_ok() throws IOException {
        File file = tempFileForRunFromResource(
                TestData.class, SAMPLE_PROPERTIES_RESOURCE_NAME,
                "." + ResourceBundleUtil.PROPERTIES_FILE_EXTENSION); // NON-NLS

        Properties properties = readProperties(file, StandardCharsets.UTF_8);

        assertEquals(SAMPLE_PROPERTIES_VALUE_1, properties.getProperty(SAMPLE_PROPERTIES_KEY_1));
        assertEquals(SAMPLE_PROPERTIES_VALUE_2, properties.getProperty(SAMPLE_PROPERTIES_KEY_2));
    }
}