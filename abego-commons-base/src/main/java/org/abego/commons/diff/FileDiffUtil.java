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

package org.abego.commons.diff;

import org.abego.commons.lang.ArrayUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.NonNull;

import java.io.File;
import java.util.regex.Pattern;

import static org.abego.commons.diff.FileDiffUtil.DirectoryDifferencesOptions.IGNORE_DOT_DS_STORE_FILES;
import static org.abego.commons.lang.RuntimeUtil.execAndReturnOutAndErr;

public final class FileDiffUtil {
    private static final String DIFF_COMMAND_NAME = "diff"; //NON-NLS
    private static final String DIFF_COMMAND_RECURSIVE_OPTION = "-r"; //NON-NLS
    /**
     * The pattern that matches a "diff" line containing a ".DS_Store" file
     */
    private static final Pattern DS_STORE_FILE_LINE_PATTERN = Pattern.compile("Only in [^:]+: \\.DS_Store\n");

    FileDiffUtil() {
        throw new MustNotInstantiateException();
    }


    /**
     * Return the difference of the two given directories or the empty string
     * when both directories are equal.
     *
     * <p>The difference is given in a text format similar to the output of the
     * Unix "diff" command. </p>
     *
     * <p>The current implementation only works on systems that provide the
     * Unix command "diff".</p>
     */
    public static String directoryDifferences(
            File directoryWithExpectedContent,
            File directoryWithActualContent,
            DirectoryDifferencesOptions... options) {
        boolean ignoreDSStoreFiles = ArrayUtil.contains(options, IGNORE_DOT_DS_STORE_FILES);

        @NonNull String diff = execAndReturnOutAndErr(
                DIFF_COMMAND_NAME, DIFF_COMMAND_RECURSIVE_OPTION,
                directoryWithExpectedContent.getAbsolutePath(),
                directoryWithActualContent.getAbsolutePath());

        if (ignoreDSStoreFiles) {
            diff = DS_STORE_FILE_LINE_PATTERN.matcher(diff).replaceAll("");
        }

        return diff;
    }

    public enum DirectoryDifferencesOptions {
        IGNORE_DOT_DS_STORE_FILES
    }


}
