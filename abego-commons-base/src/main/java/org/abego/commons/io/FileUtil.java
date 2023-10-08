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

import org.abego.commons.lang.ArrayUtil;
import org.abego.commons.lang.StringUtil;
import org.abego.commons.lang.ThrowableUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.abego.commons.io.FileCannotBeDeletedException.newFileCannotBeDeletedException;
import static org.abego.commons.io.InputStreamUtil.copyStream;
import static org.abego.commons.lang.StringUtil.removePrefix;
import static org.abego.commons.util.ListUtil.toList;

public final class FileUtil {

    FileUtil() {
        throw new MustNotInstantiateException();
    }

    //region Types

    /**
     * Return a new temporary file with a suffix <code>tempFileSuffix</code>.
     *
     * <p>The file will only exist during this virtual machine run and will
     * be deleted automatically when the virtual machine terminates
     * (normally).</p>
     *
     * <p>See also {@link File#createTempFile(String, String)}.</p>
     */
    public static File tempFileForRun(@Nullable String tempFileSuffix) {
        return runIOCode(() -> {
            File file = File.createTempFile("abego-tmp", tempFileSuffix);
            file.deleteOnExit();
            return file;
        });
    }

    /**
     * Returns a {@link Supplier} to get the text of the given {@code file},
     * assuming the text is in the given {@code encoding}, or an error text
     * when the text cannot be read.
     */
    public static Supplier<String> getTextOfFileSupplierOrError(
            File file, Charset encoding) {
        return () -> {
            try {
                return FileUtil.textOfFileIfExisting(file, encoding);
            } catch (Exception e) {
                return ThrowableUtil.messageOrClassName(e);
            }
        };
    }
    //endregion

    //region Factories / Conversions

    /**
     * Return a {@link File} for the file with the pathname
     * <code>pathname</code>.
     *
     * <p>The File may not (yet) exist in the file system.</p>
     */
    public static File file(String pathname) {
        return new File(pathname);
    }

    /**
     * Return a {@link File} for the file with the
     * <code>parent</code>-relative pathname <code>pathname</code>.
     *
     * <p>The file may not (yet) exist in the file system.</p>
     */
    public static File file(File parent, String pathname) {
        return new File(parent, pathname);
    }

    /**
     * Return a {@link File} for the normal file with the pathname
     * <code>pathname</code>.
     *
     * <p>Throw an {@link UncheckedIOException} when the file is not a normal file (e.g.
     * a directory) or the file is missing.</p>
     */
    public static File normalFile(String pathname) {
        File result = file(pathname);
        if (!result.isFile()) {
            throw new UncheckedIOException(new IOException(
                    MessageFormat.format(
                            "Pathname of normal file expected. Got {0}", // NON-NLS
                            pathname)));
        }
        return result;
    }

    /**
     * Return a {@link File} for the directory with the pathname
     * <code>pathname</code>.
     *
     * <p>Throw an {@link UncheckedIOException} when the file is not a directory (e.g.
     * a normal file) or the file is missing.</p>
     */
    public static File directory(String pathname) {
        File result = file(pathname);
        if (!result.isDirectory()) {
            throw new UncheckedIOException(new FileNotFoundException(MessageFormat.format(
                    "Pathname of (existing) directory expected. Got {0}", // NON-NLS
                    pathname)));
        }
        return result;
    }

    /**
     * Return a {@link File} for the file with the
     * <code>parent</code>-relative pathname <code>pathname</code> and delete
     * the file when the virtual machine terminates.
     *
     * <p>This will not create a file in the file system, just the Java object.
     * If later a real file is created it will only exist during this virtual
     * machine run and will be deleted automatically when the virtual machine
     * terminates (normally).</p>
     */
    public static File fileForRun(File parent, String pathname) {
        File file = new File(parent, pathname);
        file.deleteOnExit();
        return file;
    }

    /**
     * Return a new temporary directory.
     *
     * <p>The directory will only exist during this virtual machine run and will
     * be deleted automatically when the virtual machine terminates
     * (normally).</p>
     */
    public static File tempDirectoryForRun() {
        return runIOCode(() -> {
            File dir = Files.createTempDirectory(null).toFile();
            dir.deleteOnExit();
            return dir;
        });
    }

    /**
     * Return a new temporary file.
     *
     * <p>The file will only exist during this virtual machine run and will
     * be deleted automatically when the virtual machine terminates
     * (normally).</p>
     *
     * <p>See also {@link File#createTempFile(String, String)}.</p>
     */
    public static File tempFileForRun() {
        return tempFileForRun(null);
    }

    /**
     * Returns a {@link Supplier} to get the text of the given {@code file},
     * assuming the text is in UTF-8 encoding, or an error text
     * when the text cannot be read.
     */
    public static Supplier<String> getTextOfFileSupplierOrError(File file) {
        return getTextOfFileSupplierOrError(file, UTF_8);
    }

    /**
     * Return a new temporary file with a suffix <code>tempFileSuffix</code> and
     * the content of the resource <code>resourceName</code> of <code>theClass</code>.
     *
     * <p>The file will only exist during this virtual machine run and will
     * be deleted automatically when the virtual machine terminates
     * (normally).</p>
     *
     * <p>See also {@link File#createTempFile(String, String)}.</p>
     */
    public static File tempFileForRunFromResource(
            Class<?> theClass, String resourceName,
            @Nullable String tempFileSuffix) {
        File file = tempFileForRun(tempFileSuffix);
        copyResourceToFile(theClass, resourceName, file);
        return file;
    }

    /**
     * Return a new temporary file with  the content of the resource
     * <code>resourceName</code> of <code>theClass</code>.
     *
     * <p>The file will only exist during this virtual machine run and will
     * be deleted automatically when the virtual machine terminates
     * (normally).</p>
     *
     * <p>See also {@link File#createTempFile(String, String)}.</p>
     */
    public static File tempFileForRunFromResource(
            Class<?> theClass, String resourceName) {
        return tempFileForRunFromResource(theClass, resourceName, null);
    }

    public static URL toURL(File file) {
        return runIOCode(() -> file.toURI().toURL());
    }

    public static File toFile(URL url) {
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns the filename with the (last) extension (e.g. ".txt") removed, if
     * there is any extension, otherwise returns the unchanged filename.
     * <p>
     * A filename starting with a dot but without any other dots has no
     * extension.
     */
    public static String removeFileExtension(String filename) {
        return filename.replaceAll("(?<!^)[.][^.]*$", "");
    }
    //endregion

    //region Queries

    /**
     * Predicate: the file <code>pathname</code> is a directory.
     * <p>
     * See also: {@link File#File(String)}
     */
    public static boolean isDirectory(String pathname) {
        return new File(pathname).isDirectory();
    }

    /**
     * Predicate: <code>directory</code> contains <code>file</code>.
     */
    public static boolean contains(File directory, File file) {
        File f = file.getParentFile();
        while (f != null) {
            if (f.equals(directory)) {
                return true;
            }
            f = f.getParentFile();
        }
        return false;
    }

    public static boolean allFilesExist(File... files) {
        for (File file : files) {
            if (!file.exists()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the text of <code>file</code> (an UTF-8 encoded text file).
     */
    public static String textOf(File file) {
        return textOf(file, UTF_8);
    }

    /**
     * Return the text of <code>file</code> (a text file encoded with the
     * {@link Charset} <code>charset</code>).
     */
    public static String textOf(File file, Charset charset) {
        String charsetName = charset.name();
        return textOf(file, charsetName);
    }

    /**
     * Return the text of <code>file</code> (a text file encoded with the
     * {@link Charset} named <code>charsetName</code>).
     */
    public static String textOf(File file, String charsetName) {
        try {
            return new String(Files.readAllBytes(file.toPath()), Charset.forName(charsetName));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Return the text of the file named <code>pathname</code>, an
     * UTF-8 encoded text file.
     */
    public static String textOfFile(String pathname) {
        return textOfFile(pathname, UTF_8);
    }

    /**
     * Return the text of the file named <code>pathname</code> (a text file
     * encoded with the {@link Charset} <code>charset</code>)
     */
    public static String textOfFile(String pathname, Charset charset) {
        return textOf(file(pathname), charset);
    }

    /**
     * Return the text of <code>file</code> (a text file encoded with the
     * {@link Charset} named <code>charsetName</code>), if the file
     * exists, otherwise the empty string.
     */
    public static String textOfFileIfExisting(File file, String charsetName) {
        return file.exists() ? textOf(file, charsetName) : "";
    }

    /**
     * Return the text of <code>file</code> (a text file encoded with the
     * {@link Charset} <code>charset</code>), if the file
     * exists, otherwise the empty string.
     */
    public static String textOfFileIfExisting(File file, Charset charset) {
        return textOfFileIfExisting(file, charset.name());
    }

    /**
     * Return the text of <code>file</code>, an UTF-8 encoded text file,
     * if the file exists, otherwise the empty string.
     */
    public static String textOfFileIfExisting(File file) {
        return textOfFileIfExisting(file, UTF_8);
    }

    /**
     * Return the text of the file named <code>pathname</code> (a text file
     * encoded with the {@link Charset} named <code>charsetName</code>).
     */
    public static String textOfFile(String pathname, String charsetName) {
        return textOf(file(pathname), charsetName);
    }

    /**
     * Returns a {@link Supplier} to get the text of the file defined
     * by {@code pathname}, assuming the text is in the given {@code encoding}),
     * or an error text when the text cannot be read,
     * when {@code pathname} is not {@code null} and not empty; returns
     * {@code null} otherwise.
     */
    @Nullable
    public static Supplier<String> getTextOfFileSupplierOrError(
            @Nullable String pathname,
            Charset encoding) {

        if (StringUtil.hasText(pathname)) {
            return getTextOfFileSupplierOrError(new File(pathname), encoding);

        } else {
            return null;
        }
    }

    public static String stripExtension(String s) {
        //noinspection MagicCharacter
        int endIndex = s.lastIndexOf('.');
        return endIndex >= 0 ? s.substring(0, endIndex) : s;
    }

    public static String absolutePath(File parent, String filename) {
        return absolutePath(new File(parent, filename));
    }

    public static String absolutePath(String pathname) {
        return absolutePath(new File(pathname));
    }

    public static String absolutePath(File file) {
        return file.getAbsolutePath();
    }

    public static String canonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Returns the path of the {@code file} relative to the given {@code
     * baseDirectory}
     *
     * @param file          the file to consider
     * @param baseDirectory the directory serving as the base
     * @return the path of the {@code file} relative to the given {@code
     * baseDirectory}
     */
    public static String pathRelativeTo(File file, File baseDirectory) {
        return baseDirectory.toPath().relativize(file.toPath()).toString();
    }

    public static File requireFileExists(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format(
                    "File '%s' does not exist.", file.getAbsolutePath())); //NON-NLS
        }
        return file;
    }

    public static File requireDirectory(File directory, String parameterName) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(
                    String.format("Directory expected for %s, got %s", //NON-NLS
                            parameterName,
                            directory.getAbsolutePath()));
        }
        return directory;
    }

    /**
     * Returns an array with {@link File} objects corresponding to the
     * list of file pathes in {@code text}, each item in the list separated by
     * {@code separator}.
     */
    public static File[] parseFiles(String text, String separator) {
        if (text.isEmpty()) {
            return new File[0];
        }

        Set<File> pathes = new HashSet<>();
        List<File> files = new ArrayList<>();
        for (String s : text.split(Pattern.quote(separator))) {
            File f = new File(s).getAbsoluteFile();
            // don't add duplicates
            if (pathes.add(f)) {
                files.add(f);
            }
        }
        return files.toArray(new File[0]);
    }

    /**
     * Returns an array with {@link File} objects corresponding to the
     * list of file pathes in {@code text}, each item in the list separated by
     * the system's path separator.
     */
    public static File[] parseFiles(String text) {
        return parseFiles(text, getPathSeparator());
    }

    /**
     * Returns all files defined in paths, assuming that each String in paths
     * is a {@code separator}-separated list of file paths.
     */
    public static File[] parseFiles(Iterable<String> paths, String separator) {
        List<File> files = new ArrayList<>();
        paths.forEach(s -> files.addAll(toList(parseFiles(s, separator))));
        return files.toArray(new File[0]);
    }

    /**
     * Returns all files defined in paths, assuming that each String in paths
     * is a list of file paths separated by the system's path separator.
     */
    public static File[] parseFiles(Iterable<String> paths) {
        return parseFiles(paths, getPathSeparator());
    }

    /**
     * Returns all {@link File}s containing in the given {@code directory}.
     * <p>
     * Throws an {@link Exception} when {@code directory} does not exist or
     * is not a directory.
     */
    public static File[] filesInDirectory(File directory) {
        checkIsDirectory(directory);

        File[] files = directory.listFiles();
        if (files == null) {
            throw new IllegalStateException("Got 'null' for `listFiles` for " + directory.getAbsolutePath()); //NON-NLS
        }
        return files;
    }

    /**
     * Return the absolute paths of the given {@code files}, separated by
     * newlines.
     */
    public static String filePathLines(File[] files) {
        return Arrays.stream(files)
                .map(File::getAbsolutePath)
                .collect(Collectors.joining("\n"));
    }

    //endregion

    //region Checks

    /**
     * Returns the first File in {@code files} that specifies
     * an existing directory, or {@code null}, when
     * none of the files in {@code files} specifies an existing directory.
     */
    @Nullable
    public static File findExistingDirectory(File[] files) {
        return ArrayUtil.firstOrNull(files, File::isDirectory);
    }

    /**
     * A parameterless function with return type {@code T} that may throw an {@link IOException}
     */
    @FunctionalInterface
    public interface IOFunction<T> {
        T run() throws IOException;
    }

    //endregion

    //region Commands

    /**
     * Make the <code>file</code> read-only.
     */
    public static void setReadOnly(File file) {
        setReadOnly(file, true);
    }


    /**
     * Make the <code>file</code> read-only when <code>state</code> is
     * <code>true</code>, otherwise make the file writeable.
     */
    public static void setReadOnly(File file, boolean state) {
        boolean success = state ? file.setReadOnly() : file.setWritable(true);
        if (!success) {
            throw new UncheckedIOException(new IOException(MessageFormat.format(
                    "Could not change the `readOnly` state of file {0} to {1}", // NON-NLS
                    file.getAbsolutePath(),
                    state)));
        }
    }

    /**
     * Checks if the {@code file} is a directory, throwing an {@link Exception}
     * otherwise.
     */
    public static void checkIsDirectory(File file) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + file.getAbsolutePath());
        }
    }

    /**
     * Ensure the {@code directory} exists.
     *
     * <p>When the directory is missing, create it (and all its missing
     * parents.</p>
     *
     * <p>Throw an {@link UncheckedIOException} when the directory could not be
     * created.</p>
     *
     * <p>Do nothing when {@code directory} is {@code null}.</p>
     */
    public static void ensureDirectoryExists(@Nullable File directory) {
        if (directory == null) {
            return;
        }

        if (!directory.exists()) {
            boolean success = directory.mkdirs();
            if (!success) {
                throw new UncheckedIOException(new FileNotFoundException(String.format(
                        "Directory does not exist and could not be created: %s", // NON-NLS
                        directory.getAbsolutePath())));
            }
        } else if (!directory.isDirectory()) {
            throw new UncheckedIOException(new IOException(
                    String.format("File exists but is not a directory: %s", // NON-NLS
                            directory.getAbsolutePath())));
        }
    }

    public static void ensureFileExists(File file) {
        if (!file.exists()) {
            ensureDirectoryExists(file.getParentFile());

            // The result of createNewFile (createdNewFile) will always be
            // true as we only run into this branch when the file did not
            // exist (ignoring multi-threading issues).
            // So we can safely ignore checking createdNewFile and suppress
            // the related warning.
            //
            // (If we would check the flag we would generate dead code and
            // thus trigger the code coverage tool as we never enter the
            // "false" branch.)
            runIOCode(file::createNewFile);
        }
    }

    @Nullable
    public static File existingFileOrNull(String path) {
        File f = new File(path);
        return f.isFile() ? f : null;
    }

    /**
     * Checks the file paths in {@code filePaths} and returns the absolute path
     * to the first existing directory found, or returns {@code null}, when
     * none of the paths in {@code filePaths} specifies an existing directory.
     */
    @Nullable
    public static String findExistingDirectory(String[] filePaths) {
        for (String path : filePaths) {
            File dir = new File(path);
            if (dir.isDirectory()) {
                return dir.getAbsolutePath();
            }
        }
        return null;
    }

    /**
     * A {@link Runnable} that may throw an {@link IOException}.
     */
    @FunctionalInterface
    public interface IOCommand {
        void run() throws IOException;
    }

    public static File mkdirs(File parentDir, String directoryName) {
        File dir = new File(parentDir, directoryName);
        if (!dir.mkdirs()) {
            throw new IllegalStateException(String.format(
                    "Error creating directory: %s", directoryName)); //NON-NLS
        }
        return dir;
    }

    public static void copyFile(File source, File destination) {
        try {
            ensureDirectoryExists(destination.getParentFile());
            Files.copy(
                    source.toPath(),
                    destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Copy (the content of) the resource <code>resourceName</code> of
     * <code>theClass</code> to <code>file</code>.
     */
    @SuppressWarnings("WeakerAccess")
    public static void copyResourceToFile(Class<?> theClass, String resourceName, File file) {
        ensureDirectoryExists(file.getParentFile());
        @Nullable InputStream inputStream = theClass.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new UncheckedIOException(new IOException(MessageFormat.format(
                    "Resource `{0}` missing (for class {1})",  // NON-NLS
                    resourceName, theClass.getName())));
        }
        InputStreamUtil.write(inputStream, file);
    }

    public static void appendText(File file, String text, Charset charset) {
        writeToFile(file, text, charset, true);
    }

    public static void appendText(File file, String text) {
        appendText(file, text, UTF_8);
    }

    public static File writeText(File file, String text, Charset charset) {
        writeToFile(file, text, charset, false);
        return file;
    }

    public static File writeText(File file, String text) {
        return writeText(file, text, UTF_8);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static File writeText(File directory, String fileName, String text) {
        return writeText(file(directory, fileName), text);
    }

    public static File writeText(
            File directory, String fileName, String text, Charset charset) {
        return writeText(file(directory, fileName), text, charset);
    }

    private static void writeToFile(File file, String text, Charset charset, boolean append) {
        runIOCode(() -> {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, append), charset);

            try (BufferedWriter out = new BufferedWriter(outputStreamWriter)) {
                out.write(text);
            }
        });
    }

    /**
     * Creates an empty file
     */
    public static void emptyFile(File file) {
        ensureDirectoryExists(file.getParentFile());
        org.abego.commons.io.FileUtil.writeText(file, "");
    }

    /**
     * Run the {@code ioFunction} and return its result or throw an
     * {@link UncheckedIOException} when the ioFunction fails with an
     * {@link IOException}.
     */
    public static <T> T runIOCode(IOFunction<T> ioFunction) {
        try {
            return ioFunction.run();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Run the {@code ioCommand} and throw an {@link UncheckedIOException} when
     * the ioCommand fails with an {@link IOException}.
     */
    public static void runIOCode(IOCommand ioCommand) {
        try {
            ioCommand.run();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    /**
     * Delete the {@code file} when it exists.
     *
     * <p>Throw a {@link FileCannotBeDeletedException} when the file exists but cannot be deleted.
     * Do nothing when the file does not exist.</p>
     */
    public static void deleteFile(File file) {
        if (file.exists() && !file.delete()) {
            throw newFileCannotBeDeletedException(file);
        }
    }

    public static void copyFilesDeep(File sourceFile,
                                     File targetDirectory) {
        ensureDirectoryExists(targetDirectory);

        if (sourceFile.isDirectory()) {
            File[] files = filesInDirectory(sourceFile);
            File dir = new File(targetDirectory, sourceFile.getName());
            ensureDirectoryExists(dir);
            for (File child : files) {
                copyFilesDeep(child, dir);
            }
        } else {
            // single file to copy
            copyFile(sourceFile, new File(targetDirectory, sourceFile.getName()));
        }
    }

    public static void copyFilesInDirectoryDeep(File sourceDirectory,
                                                File targetDirectory) {
        checkIsDirectory(sourceDirectory);

        File[] files = filesInDirectory(sourceDirectory);
        ensureDirectoryExists(targetDirectory);
        for (File child : files) {
            copyFilesDeep(child, targetDirectory);
        }
    }

    public static void copyResourcesToDirectory(
            File directory,
            String absoluteResourceDirectoryPath,
            String... fileNames) {
        String prefix = ensureTrailingSlash(absoluteResourceDirectoryPath);

        for (String name : fileNames) {
            //noinspection StringConcatenation
            FileUtil.copyResourceToFile(
                    Object.class,
                    prefix + name,
                    new File(directory, name));
        }
    }

    @SuppressWarnings("StringConcatenation")
    public static void copyResourcesToDirectoryFlat(
            File directory,
            String absoluteResourceDirectoryPath,
            String... fileNames) {
        String prefix = ensureTrailingSlash(absoluteResourceDirectoryPath);
        for (String name : fileNames) {
            FileUtil.copyResourceToFile(
                    Object.class,
                    prefix + name,
                    new File(directory, new File(name).getName()));
        }
    }

    public static void copyResourcesDeep(
            URL rootResourceUrl, File targetDirectory) {

        copyResourcesDeep(rootResourceUrl, targetDirectory, false);
    }

    public static void copyResourcesInLocationDeep(
            URL rootResourceUrl, File targetDirectory) {

        copyResourcesDeep(rootResourceUrl, targetDirectory, true);
    }

    private static void copyResourcesDeep(
            URL rootResourceUrl, File targetDirectory, boolean copyDirectoryContent) {

        try {
            URLConnection urlConnection = rootResourceUrl.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                copyJarResourcesDeep(
                        (JarURLConnection) urlConnection, targetDirectory);
            } else {
                if (copyDirectoryContent) {
                    copyFilesInDirectoryDeep(
                            new File(rootResourceUrl.getPath()), targetDirectory);
                } else {
                    copyFilesDeep(
                            new File(rootResourceUrl.getPath()), targetDirectory);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void copyResourcesDeep(
            Class<?> theClass, String resourceName, File targetDirectory) {
        copyResourcesDeep(theClass, resourceName, targetDirectory, false);
    }

    public static void copyResourcesInLocationDeep(
            Class<?> theClass, String resourceName, File targetDirectory) {
        copyResourcesDeep(theClass, resourceName, targetDirectory, true);
    }

    /**
     * Copy (the content of) the resource <code>resourceName</code> of
     * <code>theClass</code> to <code>file</code>.
     */
    private static void copyResourcesDeep(
            Class<?> theClass, String resourceName, File targetDirectory, boolean copyDirectoryContent) {

        URL resource = theClass.getResource(resourceName);
        if (resource == null) {
            throw new IllegalArgumentException(
                    String.format("Resource '%s' missing in class %s", resourceName, theClass));
        }

        copyResourcesDeep(resource, targetDirectory, copyDirectoryContent);
    }

    private static void copyJarResourcesDeep(
            JarURLConnection jarURLConnection, File targetDirectory) throws IOException {

        JarFile jarFile = jarURLConnection.getJarFile();
        String prefix = jarURLConnection.getEntryName();

        for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); ) {
            JarEntry entry = e.nextElement();
            if (prefix == null || entry.getName().startsWith(prefix)) {
                String filename = removePrefix(entry.getName(), prefix);

                File f = new File(targetDirectory, filename);
                if (!entry.isDirectory()) {
                    try (InputStream entryInputStream = jarFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(f)) {
                        copyStream(entryInputStream, fos);
                    }
                } else {
                    ensureDirectoryExists(f);
                }
            }
        }
    }


    public static File[] filesInDirectoryAndDeeper(File directory, Predicate<File> selector) {
        List<File> result = new ArrayList<>();
        withFilesInDirectoryAndDeeperDo(directory, selector, result::add);
        return result.toArray(new File[0]);
    }

    public static File[] filesInDirectoryAndDeeper(File directory) {
        return filesInDirectoryAndDeeper(directory, f -> true);
    }

    public static void withFilesInDirectoryAndDeeperDo(
            File directory, Predicate<File> selector, Consumer<File> action) {
        try {
            Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    File file = path.toFile();
                    if (selector.test(file)) {
                        action.accept(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void withFilesInDirectoryAndDeeperDo(File directory, Consumer<File> action) {
        withFilesInDirectoryAndDeeperDo(directory, f -> true, action);
    }


    //region writeFilesIfOutdated

    /**
     * Writes the {@code targetFile} using {@code writeFile} when the
     * targetFile does not exist or any file of the {@code dependencies} has
     * changed since the targetFile was created.
     * <p>
     * To be able to check if any file of the {@code dependencies} has changed
     * a hidden file ".info.{nameOfTargetFile}" is maintained in the directory
     * of the targetFile.
     * <p>
     * The method will fail when files of the dependencies are changed while
     * the targetFile is created.
     * <p>
     * Calls {@code onFileUpToDate} when the file is up-to-date and
     * {@code writeFile} is not called.
     */
    public static void writeFileIfOutdated(
            File targetFile, File[] dependencies, Consumer<File> writeFile, Runnable onFileUpToDate) {

        File infoFile = new File(
                targetFile.getAbsoluteFile().getParentFile(),
                ".info." + targetFile.getName());
        writeFilesIfOutdated(
                new File[]{targetFile}, infoFile, dependencies,
                writeFile, onFileUpToDate);
    }

    /**
     * As {@link #writeFileIfOutdated(File, File[], Consumer, Runnable)}, but
     * without the option to pass an {@code onFileUpToDate} Runnable.
     */
    public static void writeFileIfOutdated(
            File targetFile, File[] dependencies, Consumer<File> writeFile) {
        writeFileIfOutdated(targetFile, dependencies, writeFile, () -> {});
    }

    private static void writeFilesIfOutdated(
            File[] targetFiles, File infoFile, File[] dependencies, Consumer<File> writeFile, Runnable onFileUpToDate) {

        String dependenciesSummary = fileSummaryText(dependencies);
        if (allFilesExist(targetFiles) && infoFile.exists()) {
            // We don't need to create a new targetFiles when they already exist 
            // and all files they depend on haven't changed since the last time
            // the targetFiles were created.
            String oldFileSummary = textOf(infoFile);
            if (oldFileSummary.equals(dependenciesSummary)) {
                onFileUpToDate.run();
                return;
            }
        }

        for (File f : targetFiles) {
            deleteFile(f);
        }
        deleteFile(infoFile);

        for (File f : targetFiles) {
            writeFile.accept(f);
        }

        // The dependencies must not change while we create our target files 
        // otherwise we may have some inconsistent state.
        if (!dependenciesSummary.equals(fileSummaryText(dependencies))) {
            String affectedFileBulletList = Arrays.stream(targetFiles)
                    .map(f -> "- " + f.getAbsolutePath())
                    .collect(Collectors.joining(System.lineSeparator()));
            throw new IllegalStateException(
                    String.format("The dependencies changed while processing.%nAffected:%s ", //NON-NLS
                            affectedFileBulletList));
        }

        writeText(infoFile, dependenciesSummary);
    }


    private static String fileSummaryText(File[] files) {
        return Arrays.stream(files)
                .sorted()
                .map(f -> f.getAbsolutePath() + " - " + f.length() + " - " + f.lastModified())
                .collect(Collectors.joining("\n"));
    }
    //endregion
    //endregion

    public static String getPathSeparator() {
        return System.getProperty("path.separator");
    }

    @NonNull
    private static String ensureTrailingSlash(String string) {
        return string.endsWith("/") ? string : string + "/";
    }
}
