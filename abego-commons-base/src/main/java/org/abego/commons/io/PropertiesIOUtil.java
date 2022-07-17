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
package org.abego.commons.io;

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.abego.commons.io.ReaderUtil.reader;

public final class PropertiesIOUtil {

    PropertiesIOUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Adds the properties of {@code newProperties} to the {@code target}.
     * <p>
     * Only properties with both key and value being {@link String} objects are
     * added. (see {@link Properties#stringPropertyNames()}).
     */
    public static void addProperties(Properties target, Properties newProperties) {
        for (String key : newProperties.stringPropertyNames()) {
            target.setProperty(key, newProperties.getProperty(key));
        }
    }

    public static Properties readProperties(File file, Charset charset)
            throws IOException {
        Properties properties = new Properties();
        properties.load(reader(file, charset));
        return properties;
    }

    /**
     * Reads the properties files and returns the combined result.
     * <p>The files are read in the given order, later files possibly
     * overwriting values of earlier files.
     * <p>
     * The files' encoding must be ISO 8859-1 (Latin1).
     */
    public static Properties readProperties(File... files)
            throws IOException {
        Properties properties = new Properties();
        for (File file : files) {
            if (file.isFile()) {
                properties.load(reader(file, StandardCharsets.ISO_8859_1));
            }
        }
        return properties;
    }
}
