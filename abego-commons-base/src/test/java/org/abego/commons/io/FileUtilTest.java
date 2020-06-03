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
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.abego.commons.TestData.EMPTY_TEXT;
import static org.abego.commons.TestData.MISSING_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_TEXT;
import static org.abego.commons.TestData.SAMPLE_TEXT_2;
import static org.abego.commons.TestData.SAMPLE_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_TXT_TEXT;
import static org.abego.commons.io.FileUtil.contains;
import static org.abego.commons.io.FileUtil.copyResourceToFile;
import static org.abego.commons.io.FileUtil.directory;
import static org.abego.commons.io.FileUtil.ensureDirectoryExists;
import static org.abego.commons.io.FileUtil.file;
import static org.abego.commons.io.FileUtil.fileForRun;
import static org.abego.commons.io.FileUtil.isDirectory;
import static org.abego.commons.io.FileUtil.normalFile;
import static org.abego.commons.io.FileUtil.setReadOnly;
import static org.abego.commons.io.FileUtil.tempDirectoryForRun;
import static org.abego.commons.io.FileUtil.tempFileForRun;
import static org.abego.commons.io.FileUtil.tempFileForRunFromResource;
import static org.abego.commons.io.FileUtil.textOf;
import static org.abego.commons.io.FileUtil.write;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUtilTest {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;


    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, FileUtil::new);
    }

    @Test
    void file_String_ok() {
        String pathname = SAMPLE_TXT_RESOURCE_NAME;
        File f = file(pathname);

        assertEquals(pathname, f.getName());
    }

    @Test
    void file_File_String_ok() throws IOException {
        File dir = tempDirectoryForRun();
        String pathname = SAMPLE_TXT_RESOURCE_NAME;
        File f = file(dir, pathname);

        assertEquals(pathname, f.getName());
        assertEquals(dir.getAbsolutePath(), f.getParent());
    }

    @Test
    void normalFile_withNormalFile() throws IOException {
        File file = tempFileForRun();

        File f = normalFile(file.getAbsolutePath());

        assertEquals(file, f);
    }

    @Test
    void normalFile_withDirectory_fails() throws IOException {
        File dir = tempDirectoryForRun();

        assertThrows(IOException.class,
                () -> normalFile(dir.getAbsolutePath()));
    }

    @Test
    void normalFile_withMissingFile() {
        assertThrows(IOException.class,
                () -> normalFile(MISSING_RESOURCE_NAME));
    }

    @Test
    void directory_withNormalFile() throws IOException {
        File file = tempFileForRun();

        assertThrows(IOException.class,
                () -> directory(file.getAbsolutePath()));
    }

    @Test
    void directory_withDirectory() throws IOException {
        File dir = tempDirectoryForRun();

        File f = directory(dir.getAbsolutePath());

        assertEquals(dir, f);
    }

    @Test
    void directory_withMissingFile() {
        assertThrows(IOException.class, () -> directory(MISSING_RESOURCE_NAME));
    }

    @Test
    void tempDirectoryForRun_ok() throws IOException {
        File dir = tempDirectoryForRun();

        assertTrue(dir.isDirectory());

        // No easy way to test the "deleteOnExit" feature.
    }

    @Test
    void tempFileForRun_ok() throws IOException {
        File file = tempFileForRun();

        assertTrue(file.isFile());

        // No easy way to test the "deleteOnExit" feature.
    }

    @Test
    void tempFileForRunWithResource_ok() throws IOException {
        File file = tempFileForRunFromResource(
                TestData.class, SAMPLE_TXT_RESOURCE_NAME);

        assertTrue(file.isFile());
        assertEquals(SAMPLE_TXT_TEXT, textOf(file));

        // No easy way to test the "deleteOnExit" feature.
    }

    @Test
    void fileForRun_ok() throws IOException {
        File baseDir = tempDirectoryForRun();

        String sampleName = "sampleName";  // NON-NLS
        File file = fileForRun(baseDir, sampleName);

        assertEquals(sampleName, file.getName());
        assertFalse(file.exists());

        // No easy way to test the "deleteOnExit" feature.
    }

    @Test
    void isDirectory_withDirectory() throws IOException {
        File dir = tempDirectoryForRun();

        assertTrue(isDirectory(dir.getAbsolutePath()));
    }

    @Test
    void isDirectory_withNormalFile() throws IOException {
        File file = tempFileForRun();

        assertFalse(isDirectory(file.getAbsolutePath()));
    }

    @Test
    void isDirectory_withMissingFile() {
        assertFalse(isDirectory(MISSING_RESOURCE_NAME));
    }

    @Test
    void textOf_File_ok() throws IOException {
        File file = tempFileForRun();
        FileUtil.write(file, SAMPLE_TEXT, UTF_8);

        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void textOf_File_withEmptyFile() throws IOException {
        File file = tempFileForRun();
        FileUtil.write(file, EMPTY_TEXT, UTF_8);

        assertEquals(EMPTY_TEXT, textOf(file));
    }

    @Test
    void textOfFile_File_withMissingFile() {
        assertThrows(IOException.class, () -> FileUtil
                .textOfFile(MISSING_RESOURCE_NAME));
    }

    @Test
    void textOfFile_String_ok() throws IOException {
        File file = tempFileForRun();
        FileUtil.write(file, SAMPLE_TEXT, UTF_8);

        assertEquals(SAMPLE_TEXT,
                FileUtil.textOfFile(file.getAbsolutePath()));
    }

    @Test
    void textOf_File_Charset_ok() throws IOException {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        FileUtil.write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT, FileUtil.textOf(file, sampleCharset));
    }

    @Test
    void textOf_File_String_ok() throws IOException {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        FileUtil.write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT,
                FileUtil.textOf(file, sampleCharset.name()));
    }

    @Test
    void textOfFile_String_Charset_ok()
            throws IOException {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        FileUtil.write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT, FileUtil.textOfFile(
                file.getAbsolutePath(),
                sampleCharset));
    }

    @Test
    void textOfFile_String_String_ok()
            throws IOException {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        FileUtil.write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT, FileUtil.textOfFile(
                file.getAbsolutePath(),
                sampleCharset.name()));
    }

    @Test
    void contains_ok() throws IOException {
        File dir = tempDirectoryForRun();

        File subDir = file(dir, "sub"); // NON-NLS
        File file = file(subDir, "foo"); // NON-NLS

        assertTrue(contains(dir, subDir));
        assertTrue(contains(dir, file));

        assertFalse(contains(dir, dir));

        File outerFile = file("bar"); // NON-NLS
        assertFalse(contains(dir, outerFile));
    }

    @Test
    void setReadOnly_File_ok() throws IOException {
        File file = tempFileForRun();

        write(file, SAMPLE_TEXT);

        setReadOnly(file);

        assertThrows(IOException.class, () -> write(file, SAMPLE_TEXT_2));
        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void setReadOnly_File_missingFile() {
        File file = new File(MISSING_RESOURCE_NAME);

        assertThrows(IOException.class, () -> setReadOnly(file));
    }

    @Test
    void setReadOnly_File_boolean_true() throws IOException {
        File file = tempFileForRun();

        write(file, SAMPLE_TEXT);

        setReadOnly(file, true);

        assertThrows(IOException.class, () -> write(file, SAMPLE_TEXT_2));
        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void setReadOnly_File_boolean_false() throws IOException {
        File file = tempFileForRun();

        write(file, SAMPLE_TEXT);
        setReadOnly(file, false);
        write(file, SAMPLE_TEXT_2);

        assertEquals(SAMPLE_TEXT_2, textOf(file));
    }

    @Test
    void write_ok() throws IOException {
        File file = tempFileForRun();

        write(file, SAMPLE_TEXT);

        assertEquals(SAMPLE_TEXT,
                FileUtil.textOfFile(file.getAbsolutePath()));
    }

    @Test
    void write_overwrite() throws IOException {
        File file = tempFileForRun();

        write(file, SAMPLE_TEXT);
        write(file, SAMPLE_TEXT_2);

        assertEquals(SAMPLE_TEXT_2,
                FileUtil.textOfFile(file.getAbsolutePath()));
    }

    @Test
    void write_Charset_ok() throws IOException {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        FileUtil.write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT,
                FileUtil.textOfFile(
                        file.getAbsolutePath(),
                        sampleCharset));
    }

    @Test
    void write_CharsetName_ok() throws IOException {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        FileUtil.write(file, SAMPLE_TEXT, sampleCharset.name());

        assertEquals(SAMPLE_TEXT,
                FileUtil.textOfFile(
                        file.getAbsolutePath(),
                        sampleCharset.name()));
    }

    @Test
    void ensureDirectoryExists_withExistingDir() throws IOException {

        File dir = tempDirectoryForRun();

        assertTrue(dir.exists());

        ensureDirectoryExists(dir);

        assertTrue(dir.exists());
    }

    @Test
    void ensureDirectoryExists_withMissingDir() throws IOException {

        File baseDir = tempDirectoryForRun();

        File dir = fileForRun(baseDir, "foo");

        assertFalse(dir.exists());

        ensureDirectoryExists(dir);

        assertTrue(dir.exists());
    }

    @Test
    void ensureDirectoryExists_withNullAsDir() throws IOException {
        ensureDirectoryExists(null);
    }

    @Test
    void ensureDirectoryExists_withMissingDirAndReadOnlyParent()
            throws IOException {

        File baseDir = tempDirectoryForRun();

        // make directory readonly to force an error when creating inner
        // directory
        setReadOnly(baseDir);

        File dir = new File(baseDir, "d");

        assertFalse(dir.exists());
        assertThrows(IOException.class,
                () -> ensureDirectoryExists(dir));
        assertFalse(dir.exists());
    }

    @Test
    void ensureDirectoryExists_withMissingNestedDir() throws IOException {

        File baseDir = tempDirectoryForRun();

        File d = fileForRun(baseDir, "foo");
        File dir = fileForRun(d, "bar");

        assertFalse(dir.exists());

        ensureDirectoryExists(dir);

        assertTrue(dir.exists());
    }

    @Test
    void copyResourceToFile_ok() throws IOException {
        File dir = tempDirectoryForRun();
        File file = file(dir, "foo/bar/text.txt");

        copyResourceToFile(TestData.class, SAMPLE_TXT_RESOURCE_NAME, file);

        assertEquals(SAMPLE_TXT_TEXT, textOf(file));
    }

    @Test
    void copyResourceToFile_missingResource() throws IOException {
        File dir = tempDirectoryForRun();
        File file = file(dir, "foo/bar/text.txt");

        Exception e = assertThrows(Exception.class,
                () -> copyResourceToFile(TestData.class, "foo", file));

        assertEquals("Resource `foo` missing (for class org.abego.commons.TestData)",
                e.getMessage());
    }

    @Test
    void ensureDirectoryExists_withExistingNonDirFile()
            throws IOException {

        File file = tempFileForRun();

        assertTrue(file.exists());
        assertThrows(IOException.class, () -> ensureDirectoryExists(file));
        assertTrue(file.exists());
    }
}
