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

package org.abego.commons.io;

import org.abego.commons.TestData;
import org.abego.commons.lang.ClassUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.net.URLUtil;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.abego.commons.TestData.EMPTY_TEXT;
import static org.abego.commons.TestData.MISSING_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_TEXT;
import static org.abego.commons.TestData.SAMPLE_TEXT_2;
import static org.abego.commons.TestData.SAMPLE_TXT_RESOURCE_NAME;
import static org.abego.commons.TestData.SAMPLE_TXT_TEXT;
import static org.abego.commons.io.FileUtil.absolutePath;
import static org.abego.commons.io.FileUtil.appendText;
import static org.abego.commons.io.FileUtil.contains;
import static org.abego.commons.io.FileUtil.copyResourceToFile;
import static org.abego.commons.io.FileUtil.deleteFile;
import static org.abego.commons.io.FileUtil.directory;
import static org.abego.commons.io.FileUtil.ensureDirectoryExists;
import static org.abego.commons.io.FileUtil.ensureFileExists;
import static org.abego.commons.io.FileUtil.existingFileOrNull;
import static org.abego.commons.io.FileUtil.file;
import static org.abego.commons.io.FileUtil.fileForRun;
import static org.abego.commons.io.FileUtil.getPathSeparator;
import static org.abego.commons.io.FileUtil.isDirectory;
import static org.abego.commons.io.FileUtil.normalFile;
import static org.abego.commons.io.FileUtil.pathRelativeTo;
import static org.abego.commons.io.FileUtil.requireDirectory;
import static org.abego.commons.io.FileUtil.runIOCode;
import static org.abego.commons.io.FileUtil.setReadOnly;
import static org.abego.commons.io.FileUtil.stripExtension;
import static org.abego.commons.io.FileUtil.tempDirectoryForRun;
import static org.abego.commons.io.FileUtil.tempFileForRun;
import static org.abego.commons.io.FileUtil.tempFileForRunFromResource;
import static org.abego.commons.io.FileUtil.textOf;
import static org.abego.commons.io.FileUtil.textOfFile;
import static org.abego.commons.io.FileUtil.textOfFileIfExisting;
import static org.abego.commons.io.FileUtil.toFile;
import static org.abego.commons.io.FileUtil.toURL;
import static org.abego.commons.io.FileUtil.writeText;
import static org.abego.commons.io.WriterUtil.write;
import static org.abego.commons.lang.ObjectUtil.ignore;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
    void file_File_String_ok() {
        File dir = tempDirectoryForRun();
        String pathname = SAMPLE_TXT_RESOURCE_NAME;
        File f = file(dir, pathname);

        assertEquals(pathname, f.getName());
        assertEquals(dir.getAbsolutePath(), f.getParent());
    }

    @Test
    void normalFile_withNormalFile() {
        File file = tempFileForRun();

        File f = normalFile(file.getAbsolutePath());

        assertEquals(file, f);
    }

    @Test
    void normalFile_withDirectory_fails() {
        File dir = tempDirectoryForRun();

        assertThrows(UncheckedIOException.class,
                () -> normalFile(dir.getAbsolutePath()));
    }

    @Test
    void normalFile_withMissingFile() {
        assertThrows(UncheckedIOException.class,
                () -> normalFile(MISSING_RESOURCE_NAME));
    }

    @Test
    void directory_withNormalFile() {
        File file = tempFileForRun();

        assertThrows(UncheckedIOException.class,
                () -> directory(file.getAbsolutePath()));
    }

    @Test
    void directory_withDirectory() {
        File dir = tempDirectoryForRun();

        File f = directory(dir.getAbsolutePath());

        assertEquals(dir, f);
    }

    @Test
    void directory_withMissingFile() {
        assertThrows(UncheckedIOException.class, () -> directory(MISSING_RESOURCE_NAME));
    }

    @Test
    void tempDirectoryForRun_ok() {
        File dir = tempDirectoryForRun();

        assertTrue(dir.isDirectory());

        // No easy way to test the "deleteOnExit" feature.
    }

    @Test
    void tempFileForRun_ok() {
        File file = tempFileForRun();

        assertTrue(file.isFile());

        // No easy way to test the "deleteOnExit" feature.
    }

    @Test
    void tempFileForRunWithResource_ok() {
        File file = tempFileForRunFromResource(
                TestData.class, SAMPLE_TXT_RESOURCE_NAME);

        assertTrue(file.isFile());
        assertEquals(SAMPLE_TXT_TEXT, textOf(file));

        // No easy way to test the "deleteOnExit" feature.
    }

    @Test
    void fileForRun_ok() {
        File baseDir = tempDirectoryForRun();

        String sampleName = "sampleName";  // NON-NLS
        File file = fileForRun(baseDir, sampleName);

        assertEquals(sampleName, file.getName());
        assertFalse(file.exists());

        // No easy way to test the "deleteOnExit" feature.
    }

    @Test
    void isDirectory_withDirectory() {
        File dir = tempDirectoryForRun();

        assertTrue(isDirectory(dir.getAbsolutePath()));
    }

    @Test
    void isDirectory_withNormalFile() {
        File file = tempFileForRun();

        assertFalse(isDirectory(file.getAbsolutePath()));
    }

    @Test
    void isDirectory_withMissingFile() {
        assertFalse(isDirectory(MISSING_RESOURCE_NAME));
    }

    @Test
    void textOf_File_ok() {
        File file = tempFileForRun();
        write(file, SAMPLE_TEXT, UTF_8);

        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void textOf_File_withEmptyFile() {
        File file = tempFileForRun();
        write(file, EMPTY_TEXT, UTF_8);

        assertEquals(EMPTY_TEXT, textOf(file));
    }

    @Test
    void textOfFile_File_withMissingFile() {
        assertThrows(UncheckedIOException.class, () -> textOfFile(MISSING_RESOURCE_NAME));
    }

    @Test
    void textOfFile_String_ok() {
        File file = tempFileForRun();
        write(file, SAMPLE_TEXT, UTF_8);

        assertEquals(SAMPLE_TEXT, textOfFile(file.getAbsolutePath()));
    }

    @Test
    void textOf_File_Charset_ok() {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT, textOf(file, sampleCharset));
    }

    @Test
    void textOf_File_String_ok() {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT, textOf(file, sampleCharset.name()));
    }

    @Test
    void textOfFile_String_Charset_ok() {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT, textOfFile(
                file.getAbsolutePath(), sampleCharset));
    }

    @Test
    void textOfFile_String_String_ok() {
        File file = tempFileForRun();
        Charset sampleCharset = StandardCharsets.ISO_8859_1;
        write(file, SAMPLE_TEXT, sampleCharset);

        assertEquals(SAMPLE_TEXT, textOfFile(
                file.getAbsolutePath(), sampleCharset.name()));
    }

    @Test
    void contains_ok() {
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
    void setReadOnly_File_ok() {
        File file = tempFileForRun();

        write(file, SAMPLE_TEXT);

        setReadOnly(file);

        assertThrows(UncheckedIOException.class, () -> write(file, SAMPLE_TEXT_2));
        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void setReadOnly_File_missingFile() {
        File file = new File(MISSING_RESOURCE_NAME);

        assertThrows(UncheckedIOException.class, () -> setReadOnly(file));
    }

    @Test
    void setReadOnly_File_boolean_true() {
        File file = tempFileForRun();

        write(file, SAMPLE_TEXT);

        setReadOnly(file, true);

        assertThrows(UncheckedIOException.class, () -> write(file, SAMPLE_TEXT_2));
        assertEquals(SAMPLE_TEXT, textOf(file));
    }

    @Test
    void setReadOnly_File_boolean_false() {
        File file = tempFileForRun();

        write(file, SAMPLE_TEXT);
        setReadOnly(file, false);
        write(file, SAMPLE_TEXT_2);

        assertEquals(SAMPLE_TEXT_2, textOf(file));
    }


    @Test
    void ensureDirectoryExists_withExistingDir() {

        File dir = tempDirectoryForRun();

        assertTrue(dir.exists());

        ensureDirectoryExists(dir);

        assertTrue(dir.exists());
    }

    @Test
    void ensureDirectoryExists_withMissingDir() {

        File baseDir = tempDirectoryForRun();

        File dir = fileForRun(baseDir, "foo");

        assertFalse(dir.exists());

        ensureDirectoryExists(dir);

        assertTrue(dir.exists());
    }

    @Test
    void ensureDirectoryExists_withNullAsDir() {
        ensureDirectoryExists(null);
    }

    @Test
    void ensureDirectoryExists_withMissingDirAndReadOnlyParent() {

        File baseDir = tempDirectoryForRun();

        // make directory readonly to force an error when creating inner
        // directory
        setReadOnly(baseDir);

        File dir = new File(baseDir, "d");

        assertFalse(dir.exists());
        assertThrows(UncheckedIOException.class, () -> ensureDirectoryExists(dir));
        assertFalse(dir.exists());
    }

    @Test
    void ensureDirectoryExists_withMissingNestedDir() {

        File baseDir = tempDirectoryForRun();

        File d = fileForRun(baseDir, "foo");
        File dir = fileForRun(d, "bar");

        assertFalse(dir.exists());

        ensureDirectoryExists(dir);

        assertTrue(dir.exists());
    }

    @Test
    void ensureFileExists_OK() {

        File baseDir = tempDirectoryForRun();
        File file = new File(baseDir, "newFile");
        assertFalse(file.exists());

        ensureFileExists(file);

        assertTrue(file.exists());

        ensureFileExists(file);

        assertTrue(file.exists());
    }

    @Test
    void ensureFileExists_withReadOnlyParent() {

        File baseDir = tempDirectoryForRun();

        // make directory readonly to force an error when creating inner
        // directory
        setReadOnly(baseDir);

        File file = new File(baseDir, "newFile");
        assertFalse(file.exists());

        assertThrows(UncheckedIOException.class, () -> ensureFileExists(file));
        assertFalse(file.exists());
    }


    @Test
    void copyResourceToFile_ok() {
        File dir = tempDirectoryForRun();
        File file = file(dir, "foo/bar/text.txt");

        copyResourceToFile(TestData.class, SAMPLE_TXT_RESOURCE_NAME, file);

        assertEquals(SAMPLE_TXT_TEXT, textOf(file));
    }

    @Test
    void copyResourceToFile_missingResource() {
        File dir = tempDirectoryForRun();
        File file = file(dir, "foo/bar/text.txt");

        Exception e = assertThrows(Exception.class,
                () -> copyResourceToFile(TestData.class, "foo", file));

        assertEquals("java.io.IOException: Resource `foo` missing (for class org.abego.commons.TestData)",
                e.getMessage());
    }

    @Test
    void ensureDirectoryExists_withExistingNonDirFile() {

        File file = tempFileForRun();

        assertTrue(file.exists());
        assertThrows(UncheckedIOException.class, () -> ensureDirectoryExists(file));
        assertTrue(file.exists());
    }

    @Test
    void appendText_OK() {
        File file = tempFileForRun();

        appendText(file, "foo");
        appendText(file, "bar");

        assertEquals("foobar", textOf(file));
    }

    @Test
    void runIOCode_command() {
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () -> runIOCode(() -> {
            File file = new File("missingDir/missingFile");
            ignore(file.createNewFile());
        }));
        assertEquals("java.io.IOException: No such file or directory", e.getMessage());
    }

    @Test
    void runIOCode_function() {
        UncheckedIOException e = assertThrows(UncheckedIOException.class, () -> runIOCode(() -> {
            File file = new File("missingDir/missingFile");
            ignore(file.createNewFile());
            return true;
        }));
        assertEquals("java.io.IOException: No such file or directory", e.getMessage());
    }

    @Test
    void toFile_OK() throws MalformedURLException {
        URL url = new URL("file:/some/file.txt");

        File file = toFile(url);

        assertEquals("/some/file.txt", file.getAbsolutePath());
    }

    @Test
    void toFile_withWrongScheme() throws MalformedURLException {

        URL url = new URL("http:/some/file.txt");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> toFile(url));

        assertEquals("URI scheme is not \"file\"", e.getMessage());
    }

    @Test
    void toFile_withURISyntax() throws MalformedURLException {

        // Regarding URL and URI one often hears: "Every URL is also an URI".
        //
        // However this is not true when using the Java implementations. In Java
        // the "[...]" (valid in an URL) are not accepted in an URI.
        // Therefore we can create a valid URL to a file that makes toFile
        // fail.
        URL url = new URL("file:/some/[2]/file.txt");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> toFile(url));

        assertEquals("java.net.URISyntaxException: Illegal character in path at index 11: file:/some/[2]/file.txt", e.getMessage());
    }

    @Test
    void toURL_OK() {

        File file = new File("/some/file.txt");

        URL url = toURL(file);

        assertEquals("file:/some/file.txt", url.toString());
    }

    @Test
    void textOfFileIfExisting_missingFile(@TempDir File tempDir) {
        File missingFile = new File(tempDir, "missingFile");

        assertEquals("", textOfFileIfExisting(missingFile));
    }

    @Test
    void textOfFileIfExisting_utf8File(@TempDir File tempDir) {
        File file = new File(tempDir, "file.txt");

        writeText(file, "foo bär");

        assertEquals("foo bär", textOfFileIfExisting(file));
        assertEquals("foo bär", textOfFileIfExisting(file, StandardCharsets.UTF_8));
        assertEquals("foo bär", textOfFileIfExisting(file, StandardCharsets.UTF_8.name()));

        // A final check to ensure our test really deals with encoding differences
        assertNotEquals("foo bär", textOfFileIfExisting(file, StandardCharsets.ISO_8859_1));
    }

    @Test
    void textOfFileIfExisting_windowsFile(@TempDir File tempDir) {
        File file = new File(tempDir, "file.txt");

        writeText(file, "foo bär", StandardCharsets.ISO_8859_1);

        assertEquals("foo bär", textOfFileIfExisting(file, StandardCharsets.ISO_8859_1));
        assertEquals("foo bär", textOfFileIfExisting(file, StandardCharsets.ISO_8859_1.name()));

        // A final check to ensure our test really deals with encoding differences
        assertNotEquals("foo bär", textOfFileIfExisting(file, StandardCharsets.UTF_8));
    }

    @Test
    void writeTextOK(@TempDir File tempDir) {
        String filename = "file.txt";

        writeText(tempDir, filename, "foo bär");

        assertEquals("foo bär", textOf(new File(tempDir, filename)));
    }

    @Test
    void writeText_charsetOK(@TempDir File tempDir) {
        String filename = "file.txt";

        writeText(tempDir, filename, "foo bär", StandardCharsets.UTF_8);

        assertEquals("foo bär", textOf(new File(tempDir, filename)));
    }

    @Test
    void deleteFile_missingFile(@TempDir File tempDir) {

        File missingFile = new File(tempDir, "missingFile");

        deleteFile(missingFile); // does nothing
    }

    @Test
    void deleteFile_existingFile(@TempDir File tempDir) {
        File file = new File(tempDir, "file.txt");

        writeText(file, "foo bar");

        assertTrue(file.exists());

        deleteFile(file);

        assertFalse(file.exists());
    }

    @Test
    void deleteFile_nonEmptyDirectory(@TempDir File tempDir) {

        // create a non-empty directory
        File subDir = new File(tempDir, "subDir");
        ignore(subDir.mkdir());
        File file = new File(subDir, "file.txt");
        writeText(file, "foo bar");

        // trying to delete a non-empty directory throws an exception
        FileCannotBeDeletedException e = assertThrows(FileCannotBeDeletedException.class, () -> deleteFile(subDir));
        assertEquals("Error when deleting file " + subDir.getAbsolutePath(), e.getMessage());
        assertEquals(subDir, e.getFile());
    }

    @Test
    void getTextOfFileSupplierOrError(@TempDir File tempDir) {
        // null case
        @Nullable Supplier<String> supplier =
                FileUtil.getTextOfFileSupplierOrError((@Nullable String) null, UTF_8);
        assertNull(supplier);

        File file = new File(tempDir, "file.txt");
        writeText(file, "foo bar");
        String pathname = file.getAbsolutePath();

        supplier = FileUtil.getTextOfFileSupplierOrError(pathname, UTF_8);

        if (supplier == null) {
            fail("Must not be null");
        } else {
            assertEquals("foo bar", supplier.get());
        }
    }

    @Test
    void getTextOfFileSupplierOrError_IOException(@TempDir File tempDir) {
        Supplier<String> supplier =
                FileUtil.getTextOfFileSupplierOrError(tempDir);

        assertEquals(
                "java.io.IOException: Is a directory",
                supplier.get());
    }

    @Test
    void requireFileExists(@TempDir File tempDir) {

        File file = new File(tempDir, "file.txt");

        // missing file
        Exception e = assertThrows(Exception.class, () ->
                FileUtil.requireFileExists(file));
        assertEquals("File '" + file.getAbsolutePath() + "' does not exist.",
                e.getMessage());

        // file exists
        writeText(file, "foo bar");
        File result = FileUtil.requireFileExists(file);
        assertEquals(file, result);
    }

    @Test
    void existingFileOrNull_OK(@TempDir File tempDir) {
        // create the file we want to check for
        File file = new File(tempDir, "file.txt");
        writeText(file, "foo bar");

        @Nullable File f = existingFileOrNull(file.getAbsolutePath());

        assertNotNull(f);
        assertEquals(file.getAbsolutePath(), f.getAbsolutePath());
    }

    @Test
    void existingFileOrNull_missingFile_OK(@TempDir File tempDir) {
        File file = new File(tempDir, "file.txt");

        @Nullable File f = existingFileOrNull(file.getAbsolutePath());

        assertNull(f);
    }

    @Test
    void requireDirectoryOK(@TempDir File tempDir) {
        File dir = new File(tempDir, "someDir");
        ensureDirectoryExists(dir);

        File d = requireDirectory(dir, "myDir");

        assertEquals(dir.getAbsolutePath(), d.getAbsolutePath());
    }

    @Test
    void requireDirectory_missingDirOK(@TempDir File tempDir) {
        File dir = new File(tempDir, "someDir");

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                requireDirectory(dir, "myDir"));

        assertEquals(String.format("Directory expected for myDir, got %s", dir.getAbsolutePath()), e.getMessage());
    }

    @Test
    void stripExtensionOK() {
        assertEquals("foo", stripExtension("foo.txt"));
        assertEquals("foo", stripExtension("foo"));
        assertEquals("a/b.c/foo", stripExtension("a/b.c/foo.txt"));
        assertEquals("foo", stripExtension("foo."));
    }

    @Test
    void absolutePathOK(@TempDir File tempDir) {
        File dir = new File(tempDir, "dir");
        File file = new File(dir, "file.txt");

        assertEquals(dir.getAbsolutePath(), absolutePath(dir));
        assertEquals(file.getAbsolutePath(), absolutePath(file));
        assertEquals(file.getAbsolutePath(), absolutePath(file.getAbsolutePath()));
        assertEquals(file.getAbsolutePath(), absolutePath(dir, "file.txt"));
    }

    @Test
    void pathRelativeToOK(@TempDir File tempDir) {
        File dir = new File(tempDir, "dir");
        File dir2 = new File(dir, "foo");
        File file = new File(dir2, "bar");

        assertEquals("foo", pathRelativeTo(dir2, dir));
        assertEquals("bar", pathRelativeTo(file, dir2));
        assertEquals("foo" + File.separator + "bar", pathRelativeTo(file, dir));
    }

    @Test
    void removeFileExtension() {
        assertEquals("foo", FileUtil.removeFileExtension("foo"));
        assertEquals("foo", FileUtil.removeFileExtension("foo.txt"));
        assertEquals("foo.txt", FileUtil.removeFileExtension("foo.txt.bak"));
        assertEquals("foo", FileUtil.removeFileExtension("foo."));
        assertEquals(".foo", FileUtil.removeFileExtension(".foo"));
        assertEquals(".foo", FileUtil.removeFileExtension(".foo.txt"));
        assertEquals(".foo", FileUtil.removeFileExtension(".foo."));
    }

    @Test
    void filesInDirectory(@TempDir File tempDir) {
        File[] files = FileUtil.filesInDirectory(tempDir);
        assertEquals(0, files.length);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> FileUtil.filesInDirectory(new File(tempDir, "missing")));
        Assertions.assertTrue(e.getMessage().startsWith("Not a directory:"));
    }

    @Test
    void filePathLines(@TempDir File tempDir) {
        File file1 = new File(tempDir, "file1");
        File file2 = new File(tempDir, "file2");

        assertEquals("", FileUtil.filePathLines(new File[]{}));
        assertEquals(
                file1.getAbsolutePath(),
                FileUtil.filePathLines(new File[]{file1}));
        assertEquals(
                file1.getAbsolutePath() + "\n" + file2.getAbsolutePath(),
                FileUtil.filePathLines(new File[]{file1, file2}));
    }

    @Test
    void allFilesExist(@TempDir File tempDir) {
        File file = new File(tempDir, "file");
        FileUtil.writeText(file, "foo");
        File missing = new File(tempDir, "missing");

        assertTrue(FileUtil.allFilesExist());
        assertTrue(FileUtil.allFilesExist(file));
        assertFalse(FileUtil.allFilesExist(file, missing));
        assertFalse(FileUtil.allFilesExist(missing));
    }

    @Test
    void canonicalPath(@TempDir File tempDir) throws IOException {
        File file = new File(tempDir, "file");

        assertEquals(file.getCanonicalPath(), FileUtil.canonicalPath(file));
    }

    @Test
    void parseFiles() {
        File[] files = FileUtil.parseFiles("", ";");

        assertEquals(0, files.length);


        files = FileUtil.parseFiles("file1;file2;file1", ";");

        assertEquals(2, files.length);
        assertEquals("file1", files[0].getName());
        assertEquals("file2", files[1].getName());
    }

    @Test
    void parseFilesIterable() {

        File[] files = FileUtil.parseFiles(new ArrayList<>(), ";");
        assertEquals(0, files.length);

        List<String> list = new ArrayList<>();
        list.add("file1");
        list.add("file2;file3");
        files = FileUtil.parseFiles(list, ";");

        assertEquals(3, files.length);
        assertEquals("file1", files[0].getName());
        assertEquals("file2", files[1].getName());
        assertEquals("file3", files[2].getName());
    }

    @Test
    void parseFilesImplicitSeparator() {
        String sep = getPathSeparator();
        File[] files = FileUtil.parseFiles("");
        assertEquals(0, files.length);

        files = FileUtil.parseFiles("file1" + sep + "file2" + sep + "file1");
        assertEquals(2, files.length);
        assertEquals("file1", files[0].getName());
        assertEquals("file2", files[1].getName());
    }

    @Test
    void findExistingDirectory(@TempDir File tempDir) {
        assertEquals(tempDir, FileUtil.findExistingDirectory(
                new File[]{tempDir}));
        assertNull(FileUtil.findExistingDirectory(
                new File[]{new File(tempDir, "missing")}));
        assertNull(FileUtil.findExistingDirectory(
                new File[]{}));
    }

    @Test
    void findExistingDirectoryWithFilePaths(@TempDir File tempDir) {
        assertEquals(tempDir.getAbsolutePath(), FileUtil.findExistingDirectory(
                new String[]{tempDir.getAbsolutePath()}));
        assertNull(FileUtil.findExistingDirectory(
                new String[]{new File(tempDir, "missing").getAbsolutePath()}));
        assertNull(FileUtil.findExistingDirectory(
                new String[]{}));
    }

    @Test
    void copyResourcesToDirectory(@TempDir File tempDir) {
        FileUtil.copyResourcesToDirectory(
                tempDir,
                "/org/abego/commons/dir1",
                "foo.txt", "subdir/bar.txt");

        assertTrue(new File(tempDir, "foo.txt").isFile());
        assertTrue(new File(tempDir, "subdir/bar.txt").isFile());
    }

    @Test
    void copyResourcesToDirectoryFlat(@TempDir File tempDir) {
        FileUtil.copyResourcesToDirectoryFlat(
                tempDir,
                "/org/abego/commons/dir1",
                "foo.txt", "subdir/bar.txt");

        assertTrue(new File(tempDir, "foo.txt").isFile());
        assertTrue(new File(tempDir, "bar.txt").isFile());
    }

    @Test
    void copyResourcesDeep(@TempDir File tempDir) {
        FileUtil.copyResourcesDeep(
                getClass(), "/org/abego/commons/dir1", tempDir);

        assertTrue(new File(tempDir, "dir1/foo.txt").isFile());
        assertTrue(new File(tempDir, "dir1/subdir/bar.txt").isFile());
    }

    @Test
    void copyResourcesDeepMissingResource(@TempDir File tempDir) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                FileUtil.copyResourcesDeep(
                        getClass(), "/org/abego/commons/missingDir", tempDir));
        assertEquals("Resource '/org/abego/commons/missingDir' missing in class class org.abego.commons.io.FileUtilTest", e.getMessage());
    }

    @Test
    void copyResourcesInLocationDeep(@TempDir File tempDir) {
        FileUtil.copyResourcesInLocationDeep(
                getClass(), "/org/abego/commons/dir1", tempDir);

        assertTrue(new File(tempDir, "foo.txt").isFile());
        assertTrue(new File(tempDir, "subdir/bar.txt").isFile());
    }

    @Test
    void copyResourcesInLocationDeepWithURL(@TempDir File tempDir) {
        FileUtil.copyResourcesInLocationDeep(
                Objects.requireNonNull(getClass().getResource("/org/abego/commons/dir1")), tempDir);

        assertTrue(new File(tempDir, "foo.txt").isFile());
        assertTrue(new File(tempDir, "subdir/bar.txt").isFile());
    }

    @Test
    void copyResourcesDeepWithURL(@TempDir File tempDir) {
        URL resource = getClass().getResource("/org/abego/commons/dir1");
        if (resource == null) {
            throw new IllegalStateException("Resource missing");
        }
        FileUtil.copyResourcesDeep(resource, tempDir);

        assertTrue(new File(tempDir, "dir1/foo.txt").isFile());
        assertTrue(new File(tempDir, "dir1/subdir/bar.txt").isFile());
    }

    @Test
    void copyResourcesDeepJarFile(@TempDir File tempDir) {
        URL resource = ClassUtil.resource(
                getClass(), "/org/abego/commons/hello.jar");

        FileUtil.copyResourcesDeep(resource, tempDir);

        // The jar-file is copied, but not the content extracted
        assertTrue(new File(tempDir, "hello.jar").isFile());
        assertFalse(new File(tempDir, "META-INF/MANIFEST.MF").isFile());
        assertFalse(new File(tempDir, "org/abego/commons/Main.class").isFile());
    }

    @Test
    void copyResourcesDeepJarFileContent(@TempDir File tempDir) {
        URL resource = getClass().getResource("/org/abego/commons/hello.jar");
        if (resource == null) {
            throw new IllegalStateException("Resource missing");
        }

        URL jarProtocolUrl = URLUtil.asJarProtocolURL(resource);
        FileUtil.copyResourcesDeep(jarProtocolUrl, tempDir);

        // The jar-file is NOT copied, but its content extracted
        // (because we are using the "jar:" protocol)
        assertFalse(new File(tempDir, "hello.jar").isFile());
        assertTrue(new File(tempDir, "META-INF/MANIFEST.MF").isFile());
        assertTrue(new File(tempDir, "org/abego/commons/Main.class").isFile());
    }

    @Test
    void emptyFile(@TempDir File tempDir) {
        File file = new File(tempDir, "file.txt");

        FileUtil.emptyFile(file);

        assertEquals("", FileUtil.textOf(file));
    }

    @Test
    void mkdirs(@TempDir File tempDir) {
        FileUtil.mkdirs(tempDir, "foo/bar");

        assertTrue(new File(tempDir, "foo").isDirectory());
        assertTrue(new File(tempDir, "foo/bar").isDirectory());
    }

}
