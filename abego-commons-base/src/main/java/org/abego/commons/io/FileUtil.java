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
package org.abego.commons.io;

import org.abego.commons.util.ScannerUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Scanner;

import static org.abego.commons.io.PrintWriterUtil.printWriter;
import static org.abego.commons.lang.exception.MustNotInstantiateException.throwMustNotInstantiate;

public class FileUtil {

    FileUtil() {
        throwMustNotInstantiate();
    }

    // --- Factories ---

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
     * <p>Throw an {@link IOException} when the file is not a normal file (e.g.
     * a directory) or the file is missing.</p>
     */
    public static File normalFile(String pathname) throws IOException {
        File result = file(pathname);
        if (!result.isFile()) {
            throw new IOException(
                    MessageFormat.format(
                            "Pathname of normal file expected. Got {0}", // NON-NLS
                            pathname));
        }
        return result;
    }

    /**
     * Return a {@link File} for the directory with the pathname
     * <code>pathname</code>.
     *
     * <p>Throw an {@link IOException} when the file is not a directory (e.g.
     * a normal file) or the file is missing.</p>
     */
    public static File directory(String pathname) throws IOException {
        File result = file(pathname);
        if (!result.isDirectory()) {
            throw new IOException(MessageFormat.format(
                    "Pathname of (existing) directory expected. Got {0}", // NON-NLS
                    pathname));
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
    public static File tempDirectoryForRun() throws IOException {
        File dir = Files.createTempDirectory(null).toFile();
        dir.deleteOnExit();
        return dir;
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
    public static File tempFileForRun() throws IOException {
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
    public static File tempFileForRun(String tempFileSuffix)
            throws IOException {
        File file = File.createTempFile("abego", tempFileSuffix);
        file.deleteOnExit();
        return file;
    }

    /**
     * Return a new temporary file with a suffix <code>tempFileSuffix</code> and
     * the content of the resource <code>resourceName</code> of <code>class_</code>.
     *
     * <p>The file will only exist during this virtual machine run and will
     * be deleted automatically when the virtual machine terminates
     * (normally).</p>
     *
     * <p>See also {@link File#createTempFile(String, String)}.</p>
     */
    public static File tempFileForRunFromResource(
            Class<?> class_, String resourceName,
            String tempFileSuffix)
            throws IOException {
        File file = tempFileForRun(tempFileSuffix);
        copyResourceToFile(class_, resourceName, file);
        return file;
    }

    /**
     * Return a new temporary file with  the content of the resource
     * <code>resourceName</code> of <code>class_</code>.
     *
     * <p>The file will only exist during this virtual machine run and will
     * be deleted automatically when the virtual machine terminates
     * (normally).</p>
     *
     * <p>See also {@link File#createTempFile(String, String)}.</p>
     */
    public static File tempFileForRunFromResource(
            Class<?> class_, String resourceName)
            throws IOException {
        return tempFileForRunFromResource(class_, resourceName, null);
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
    public static String textOf(File file) throws FileNotFoundException {
        return textOf(file, StandardCharsets.UTF_8);
    }

    /**
     * Return the text of <code>file</code> (a text file encoded with the
     * {@link Charset} <code>charset</code>).
     */
    public static String textOf(File file, Charset charset)
            throws FileNotFoundException {
        String charsetName = charset.name();
        return textOf(file, charsetName);
    }

    /**
     * Return the text of <code>file</code> (a text file encoded with the
     * {@link Charset} named <code>charsetName</code>).
     */
    public static String textOf(File file, String charsetName)
            throws FileNotFoundException {
        return ScannerUtil.textOf(() -> new Scanner(file, charsetName));
    }

    /**
     * Return the text of the file named <code>pathname</code>, an
     * UTF-8 encoded text file.
     */
    public static String textOfFile(String pathname) throws FileNotFoundException {
        return textOfFile(pathname, StandardCharsets.UTF_8);
    }

    /**
     * Return the text of the file named <code>pathname</code> (a text file
     * encoded with the {@link Charset} <code>charset</code>)
     */
    public static String textOfFile(String pathname, Charset charset)
            throws FileNotFoundException {
        return textOf(file(pathname), charset);
    }

    /**
     * Return the text of the file named <code>pathname</code> (a text file
     * encoded with the {@link Charset} named <code>charsetName</code>).
     */
    public static String textOfFile(String pathname, String charsetName)
            throws FileNotFoundException {
        return textOf(file(pathname), charsetName);
    }

    // --- Commands ---

    /**
     * Make the <code>file</code> read-only.
     */
    public static void setReadOnly(File file) throws IOException {
        setReadOnly(file, true);
    }


    /**
     * Make the <code>file</code> read-only when <code>state</code> is
     * <code>true</code>, otherwise make the file writeable.
     */
    public static void setReadOnly(File file, boolean state) throws IOException {
        boolean success = state ? file.setReadOnly() : file.setWritable(true);
        if (!success) {
            throw new IOException(MessageFormat.format(
                    "Could not change the `readOnly` state of file {0} to {1}", // NON-NLS
                    file.getAbsolutePath(),
                    state));
        }
    }

    /**
     * Write {@code text} to {@code file} using the UTF-8 charset.
     */
    public static void write(final File file, final String text)
            throws IOException {
        write(file, text, StandardCharsets.UTF_8);
    }

    /**
     * Write {@code text} to {@code file} using {@code charset}.
     */
    public static void write(
            final File file,
            final String text,
            final Charset charset)
            throws IOException {

        write(file, text, charset.name());
    }

    /**
     * Write {@code text} to {@code file} using the {@link Charset} named
     * <code>charsetName</code>.
     */
    public static void write(
            final File file,
            final String text,
            final String charsetName)
            throws IOException {

        try (PrintWriter output = printWriter(file, charsetName)) {
            output.write(text);
        }
    }

    /**
     * Ensure the {@code directory} exists.
     *
     * <p>When the directory is missing, create it (and all its missing
     * parents.</p>
     *
     * <p>Throw an {@link IOException} when the directory could not be
     * created.</p>
     *
     * <p>Do nothing when {@code directory} is {@code null}.</p>
     */
    public static void ensureDirectoryExists(File directory)
            throws IOException {
        if (directory == null) {
            return;
        }

        if (!directory.exists()) {
            boolean success = directory.mkdirs();
            if (!success) {
                throw new FileNotFoundException(String.format(
                        "Directory does not exist and could not be created: %s", // NON-NLS
                        directory.getAbsolutePath()));
            }
        } else if (!directory.isDirectory()) {
            throw new IOException(
                    String.format("File exists but is not a directory: %s", // NON-NLS
                            directory.getAbsolutePath()));
        }
    }

    /**
     * Copy (the content of) the resource <code>resourceName</code> of
     * <code>class_</code> to <code>file</code>.
     */
    public static void copyResourceToFile(Class<?> class_, String resourceName,
                                          File file) throws IOException {
        ensureDirectoryExists(file.getParentFile());
        InputStream inputStream = class_.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IOException(MessageFormat.format(
                    "Resource `{0}` missing (for class {1})",  // NON-NLS
                    resourceName, class_.getName()));
        }
        InputStreamUtil.write(inputStream, file);
    }

}
