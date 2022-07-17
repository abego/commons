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
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.abego.commons.io.ReaderUtil.reader;
import static org.abego.commons.util.PropertiesUtil.homeDirectory;
import static org.abego.commons.util.PropertiesUtil.userName;
import static org.abego.commons.util.PropertiesUtil.workingDirectory;

public final class PropertiesIOUtil {
    private static final String PROPERTIES_FILE_EXTENSION = ".properties";


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

    /**
     * Returns the {@link Properties} of the Properties group with the given
     * {@code groupName}.
     * <p>
     * A Properties group's content is defined by multiple Properties
     * locations (any file is optional):
     * <ul>
     *     <li><em>System Properties</em></li>
     *     <li>{@code "{workingDirectory}/{groupName}-{userName}.properties"}</li>
     *     <li>{@code "{workingDirectory}/{groupName}.properties"}</li>
     *     <li>{@code "{homeDirectory}/{groupName}-{userName}.properties"}</li>
     *     <li>{@code "{homeDirectory}/.config/abego.org/{groupName}-{userName}.properties"}</li>
     *     <li>{@code "{homeDirectory}/{groupName}.properties"}</li>
     *     <li>{@code "{homeDirectory}/.config/abego.org/{groupName}.properties"}</li>
     *     <li>{@code "{homeDirectory}/.config/abego.org/abego-{userName}.properties"}</li>
     *     <li>{@code "{homeDirectory}/.config/abego.org/abego.properties"}</li>
     * </ul>
     * {@code "{workingDirectory}"}, {@code "{homeDirectory}"} and
     * {@code "{userName}"} correspond to the System properties
     * {@code "{user.dir}"}, {@code "{user.home}"} and {@code "{user.name}"}.
     * <p>
     * When checking for a value files earlier in the above list take precedence
     * over the later ones.
     */
    public static Properties readPropertiesGroup(String groupName) {
        try {
            Properties result = readProperties(propertiesGroupFilesInReversedLookupOrder(groupName));
            addProperties(result, System.getProperties());
            return result;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Returns the reversed default lookup path for properties files with the
     * given {@code groupName}, as defined on {@link #readPropertiesGroup(String)}.
     * <p>
     * Notice: the {@link File}s are returned reversed to the order given
     * in {@link #readPropertiesGroup(String)}. This fits to the approach to
     * fill a Properties object by loading content from multiple files, with
     * the later ones overwriting the former ones. This way a lookup in the
     * Properties object will see the "last" value written for a key, i.e.
     * from the later ones in the list. (See {@link #readProperties(File...)}
     */
    private static File[] propertiesGroupFilesInReversedLookupOrder(String groupName) {
        File workDir = workingDirectory();
        File homeDir = homeDirectory();
        File configDir = new File(homeDirectory(), ".config/abego.org");
        //noinspection StringConcatenation
        String fileName = groupName + PROPERTIES_FILE_EXTENSION;
        //noinspection StringConcatenation
        String userSpecificFileName = groupName + "-" + userName() + PROPERTIES_FILE_EXTENSION;
        return new File[]{
                new File(configDir, "abego" + PROPERTIES_FILE_EXTENSION),
                new File(configDir, "abego-" + userName() + PROPERTIES_FILE_EXTENSION),
                new File(configDir, fileName),
                new File(homeDir, fileName),
                new File(configDir, userSpecificFileName),
                new File(homeDir, userSpecificFileName),
                new File(workDir, fileName),
                new File(workDir, userSpecificFileName),
        };
    }
}
