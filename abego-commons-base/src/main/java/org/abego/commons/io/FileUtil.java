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

import org.abego.commons.lang.StringUtil;
import org.abego.commons.lang.ThrowableUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.abego.commons.io.FileCannotBeDeletedException.newFileCannotBeDeletedException;

public final class FileUtil {

    FileUtil() {
        throw new MustNotInstantiateException();
    }

    // --- Factories / Conversions ---

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
            File file = File.createTempFile("abego", tempFileSuffix);
            file.deleteOnExit();
            return file;
        });
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

    // --- Queries ---

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

    // --- Commands ---

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

    /**
     * Returns a {@link Supplier} to get the text of the given {@code file},
     * assuming the text is in UTF-8 encoding, or an error text
     * when the text cannot be read.
     */
    public static Supplier<String> getTextOfFileSupplierOrError(File file) {
        return getTextOfFileSupplierOrError(file, UTF_8);
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

    public static String stripExtension(String s) {
        //noinspection MagicCharacter
        int endIndex = s.lastIndexOf('.');
        return endIndex >= 0 ? s.substring(0, endIndex) : s;
    }

    public static String absolutePath(File parent, String filename) {
        return new File(parent, filename).getAbsolutePath();
    }

    public static String absolutePath(String pathname) {
        return new File(pathname).getAbsolutePath();
    }

    public static String absolutePath(File file) {
        return file.getAbsolutePath();
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

    public static void copyResourcesToDirectory(
            File source,
            String absoluteResourceDirectoryPath,
            String... fileNames) {
        for (String name : fileNames) {
            //noinspection StringConcatenation
            FileUtil.copyResourceToFile(
                    Object.class,
                    absoluteResourceDirectoryPath + name,
                    new File(source, name));
        }
    }

    @SuppressWarnings("StringConcatenation")
    public static void copyResourcesToDirectoryFlat(
            File directory,
            String absoluteResourceDirectoryPath,
            String... fileNames) {
        for (String name : fileNames) {
            FileUtil.copyResourceToFile(
                    Object.class,
                    absoluteResourceDirectoryPath + name,
                    new File(directory, new File(name).getName()));
        }
    }

    /**
     * A parameterless function with return type {@code T} that may throw an {@link IOException}
     */
    @FunctionalInterface
    public interface IOFunction<T> {
        T run() throws IOException;
    }

    /**
     * A {@link Runnable} that may throw an {@link IOException}.
     */
    @FunctionalInterface
    public interface IOCommand {
        void run() throws IOException;
    }


}
