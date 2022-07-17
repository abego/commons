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

import org.eclipse.jdt.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

import static org.abego.commons.io.PropertiesIOUtil.addProperties;
import static org.abego.commons.io.PropertiesIOUtil.readProperties;
import static org.abego.commons.util.PropertiesUtil.homeDirectory;
import static org.abego.commons.util.PropertiesUtil.userName;
import static org.abego.commons.util.PropertiesUtil.workingDirectory;

/**
 * Provides access to the {@link Properties} of a Properties group with a given
 * {@code groupName}.
 * <p>
 * A Properties group's content is accumulated from multiple Properties
 * sources:
 * <ul>
 *     <li>The System Properties</li>
 *     <li>Properties files in:
 *     <ul>
 *         <li> the working directory</li>
 *         <li> the home directory</li>
 *         <li> the abego config directory ({@code "{homeDirectory}/.config/abego.org/"})</li>
 *     </ul>
 *     </li>
 * </ul>
 * A Properties file may be:
 * <ul>
 *     <li>group- and user-specific ({@code "{groupName}-{userName}.properties"})</li>
 *     <li>group-specific ({@code "{groupName}.properties"})</li>
 *     <li>shared and user-specific ({@code "abego-{userName}.properties"})</li>
 *     <li>shared ({@code "abego.properties"})</li>
 * </ul>
 * The following rules hold:
 * <ul>
 *     <li>A value defined in a System property overrules any value
 *     defined in a Properties file.</li>
 *     <li>A group-specific value overrules any shared value.</li>
 *     <li>A value defined in the working directory overrules any value defined
 *     in the home directory, which overrules any value defined in the abego
 *     config directory.</li>
 *     <li>Within a directory a user-specific value overrules a non-user-specific value.</li>
 * </ul>
 * <p>
 * These rules lead to the following "lookup order":
 * <ul>
 *     <li><em>System Properties</em></li>
 *     <li>{@code "{workingDirectory}/{groupName}-{userName}.properties"}</li>
 *     <li>{@code "{workingDirectory}/{groupName}.properties"}</li>
 *     <li>{@code "{homeDirectory}/{groupName}-{userName}.properties"}</li>
 *     <li>{@code "{homeDirectory}/{groupName}.properties"}</li>
 *     <li>{@code "{homeDirectory}/.config/abego.org/{groupName}-{userName}.properties"}</li>
 *     <li>{@code "{homeDirectory}/.config/abego.org/{groupName}.properties"}</li>
 *     <li>{@code "{workingDirectory}/abego-{userName}.properties"}</li>
 *     <li>{@code "{workingDirectory}/abego.properties"}</li>
 *     <li>{@code "{homeDirectory}/abego-{userName}.properties"}</li>
 *     <li>{@code "{homeDirectory}/abego.properties"}</li>
 *     <li>{@code "{homeDirectory}/.config/abego.org/abego-{userName}.properties"}</li>
 *     <li>{@code "{homeDirectory}/.config/abego.org/abego.properties"}</li>
 * </ul>
 * {@code "{workingDirectory}"}, {@code "{homeDirectory}"} and
 * {@code "{userName}"} correspond to the System properties
 * {@code "{user.dir}"}, {@code "{user.home}"} and {@code "{user.name}"}.
 */
public final class PropertiesGroup {
    private static final String PROPERTIES_FILE_EXTENSION = ".properties";
    private static final String SHARED_GROUP = "abego"; //NON-NLS

    private final String groupName;
    @Nullable
    private Properties properties;

    private PropertiesGroup(String groupName) {
        this.groupName = groupName;
    }

    public static PropertiesGroup newPropertiesGroup(String groupName) {
        return new PropertiesGroup(groupName);
    }

    private static Properties readPropertiesGroup(String groupName) {
        //noinspection CallToSuspiciousStringMethod
        if (groupName.equals(SHARED_GROUP)) {
            throw new IllegalArgumentException(
                    "`" + SHARED_GROUP + "` must not be used as a groupName");
        }

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
     * from the later ones in the list. (See {@link PropertiesIOUtil#readProperties(File...)}
     */
    private static File[] propertiesGroupFilesInReversedLookupOrder(String groupName) {
        File workDir = workingDirectory();
        File homeDir = homeDirectory();
        File configDir = new File(homeDirectory(), ".config/abego.org");
        //noinspection StringConcatenation
        String fileName = groupName + PROPERTIES_FILE_EXTENSION;
        //noinspection StringConcatenation
        String userSpecificFileName = groupName + "-" + userName() + PROPERTIES_FILE_EXTENSION;
        //noinspection StringConcatenation
        String sharedFileName = SHARED_GROUP + PROPERTIES_FILE_EXTENSION;
        //noinspection StringConcatenation
        String sharedUserSpecificFileName = SHARED_GROUP + "-" + userName() + PROPERTIES_FILE_EXTENSION;
        return new File[]{
                new File(configDir, sharedFileName),
                new File(configDir, sharedUserSpecificFileName),
                new File(homeDir, sharedFileName),
                new File(homeDir, sharedUserSpecificFileName),
                new File(workDir, sharedFileName),
                new File(workDir, sharedUserSpecificFileName),
                new File(configDir, fileName),
                new File(configDir, userSpecificFileName),
                new File(homeDir, fileName),
                new File(homeDir, userSpecificFileName),
                new File(workDir, fileName),
                new File(workDir, userSpecificFileName),
        };
    }

    public Properties getProperties() {
        if (properties == null) {
            properties = readPropertiesGroup(groupName);
        }
        return properties;
    }

    public String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }
}
