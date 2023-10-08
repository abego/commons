/*
 * MIT License
 *
 * Copyright (c) 2023 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.test;

import org.abego.commons.io.FileUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.range.IntRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.abego.commons.lang.ClassUtil.classNameOrNull;
import static org.abego.commons.range.IntRangeDefault.newIntRange;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class JUnit5Util {

    JUnit5Util() {
        throw new MustNotInstantiateException();
    }

    /**
     * Asserts the actual String has the same lines as the expected String,
     * ignoring possible difference is the line separators ("\n" vs "\r\n")
     */
    public static void assertEqualLines(String expected, String actual) {
        String e = expected.replaceAll("\r", "");
        String a = actual.replaceAll("\r", "");
        assertEquals(e, a);
    }

    public static void assertTextContains(
            String expectedSubstring,
            String text,
            String descriptor) {
        if (!text.contains(expectedSubstring)) {
            fail(MessageFormat.format(
                    "{0} does not contain \"{1}\".\nGot:\n\"{2}\"", // NON-NLS
                    descriptor, expectedSubstring, text));
        }
    }

    public static void assertTextOfFileEquals(
            final String expected,
            final File file,
            final Charset charset) {
        assertEquals(
                expected,
                FileUtil.textOf(file, charset),
                MessageFormat.format(
                        "Wrong text in file {0}", // NON-NLS
                        file.getAbsolutePath()));
    }

    public static void assertTextOfFileEquals(
            final String expected,
            final File file) {

        assertTextOfFileEquals(expected, file, StandardCharsets.UTF_8);
    }

    public static <T extends Throwable> T assertThrowsWithMessage(
            Class<T> expectedType,
            String expectedMessage, Executable executable) {
        T result = assertThrows(expectedType, executable);
        assertEquals(expectedMessage, result.getMessage(),
                () -> format("Expected exception message: '%s', got: '%s'", //NON-NLS
                        expectedMessage, result.getMessage()));
        return result;
    }

    public static void assertIntRangeEquals(int expectedStart,
                                            int expectedEnd,
                                            IntRange intRange) {
        assertEquals(newIntRange(expectedStart, expectedEnd), intRange);
    }

    @SuppressWarnings("unchecked")
    public static <T> T castOrFail(Class<T> type, Object object) {
        if (!type.isInstance(object)) {
            fail(format("expected %s, got %s", type.getName(), classNameOrNull(object))); //NON-NLS
        }
        return (T) object;
    }

    /**
     * Asserts the directories {@code expectedDir} and {@code actualDir}
     * contain equal files, also comparing the content of matching regular files
     * (assume text files with UTF-8 encoding).
     */
    public static void assertEqualFiles(File expectedDir, File actualDir) {
        List<Executable> allAsserts = new ArrayList<>();
        addAssertsComparingDirectories(allAsserts, expectedDir, actualDir, "");
        Assertions.assertAll(allAsserts);
    }

    private static void addAssertsComparingDirectories(
            List<Executable> allAsserts, File expectedDir, File actualDir, String prefix) {
        Set<String> expectedFiles = Arrays.stream(FileUtil.filesInDirectory(expectedDir))
                .map(File::getName).collect(Collectors.toSet());
        Set<String> actualFiles = Arrays.stream(FileUtil.filesInDirectory(actualDir))
                .map(File::getName).collect(Collectors.toSet());

        allAsserts.add(() ->
                assertEquals(expectedFiles.stream()
                                .sorted()
                                .map(s -> prefix + s)
                                .collect(Collectors.joining("\n")),
                        actualFiles.stream()
                                .sorted()
                                .map(s -> prefix + s)
                                .collect(Collectors.joining("\n")), "Missing/Extra Files")
        );

        // work on all files that exist in both directories
        expectedFiles.retainAll(actualFiles);
        expectedFiles.stream().sorted().forEach(filename -> {
            File expectedFile = new File(expectedDir, filename);
            File actualFile = new File(actualDir, filename);
            if (expectedFile.isDirectory() != actualFile.isDirectory()) {
                allAsserts.add(() -> fail("Mixing directory and regular file: " + prefix + filename));
            } else {
                if (expectedFile.isDirectory()) {
                    addAssertsComparingDirectories(allAsserts, expectedFile, actualFile, prefix + filename + "/");
                } else {
                    allAsserts.add(() ->
                            assertEquals(org.abego.commons.io.FileUtil.textOf(expectedFile),
                                    org.abego.commons.io.FileUtil.textOf(actualFile),
                                    "Difference in file: " + prefix + filename));
                }
            }
        });
    }
}
