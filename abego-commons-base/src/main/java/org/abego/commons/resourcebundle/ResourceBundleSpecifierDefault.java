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

import java.io.File;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.abego.commons.lang.StringUtil.string;

class ResourceBundleSpecifierDefault implements ResourceBundleSpecifier {
    private static final String BUNDLE_FILE_NAME_REGEX =
            "([^_.]+)(?:_([^_.]+))?(?:_([^_.]+))?(?:_([^_.]+))?\\.(properties)"; // NON-NLS
    /**
     * The Pattern to identify the parts of a ResourceBundle file name.
     *
     * <ul>
     * <li>group(1): bundle base name</li>
     * <li>group(2): language (may be null/empty)</li>
     * <li>group(3): country (may be null/empty)</li>
     * <li>group(4): platform (may be null/empty)</li>
     * <li>group(5): file extension (without '.')</li>
     * </ul>
     */
    private static final Pattern BUNDLE_NAME_PATTERN =
            Pattern.compile(BUNDLE_FILE_NAME_REGEX, Pattern.CASE_INSENSITIVE);
    private final String bundleBaseName;
    private final String language;
    private final String country;
    private final String platform;
    private final String fileExtension;

    private ResourceBundleSpecifierDefault(File resourceBundleFile) {
        String name = resourceBundleFile.getName();

        Matcher partMatcher = BUNDLE_NAME_PATTERN.matcher(name);
        if (!partMatcher.matches()) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "ResourceBundle file ('*.properties') expected, got {0}", // NON-NLS
                    resourceBundleFile.getAbsolutePath()));
        }

        this.bundleBaseName = partMatcher.group(1);
        this.language = string(partMatcher.group(2));
        this.country = string(partMatcher.group(3));
        this.platform = string(partMatcher.group(4));
        this.fileExtension = partMatcher.group(5);
    }

    static ResourceBundleSpecifier resourceBundleSpecifier(File resourceBundleFile) {
        return new ResourceBundleSpecifierDefault(resourceBundleFile);
    }

    @Override
    public String bundleBaseName() {
        return bundleBaseName;
    }

    @Override
    public String language() {
        return language;
    }

    @Override
    public String country() {
        return country;
    }

    @Override
    public String platform() {
        return platform;
    }

    @Override
    public String fileExtension() {
        return fileExtension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceBundleSpecifierDefault)) return false;
        ResourceBundleSpecifierDefault that = (ResourceBundleSpecifierDefault) o;
        return bundleBaseName.equals(that.bundleBaseName) &&
                language.equals(that.language) &&
                country.equals(that.country) &&
                platform.equals(that.platform) &&
                fileExtension.equals(that.fileExtension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bundleBaseName, language, country, platform, fileExtension);
    }
}
