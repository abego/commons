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

package org.abego.commons.lang;


import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.abego.commons.lang.exception.UncheckedException;
import org.eclipse.jdt.annotation.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.util.MissingResourceException;

import static java.lang.String.format;
import static org.abego.commons.lang.exception.UncheckedException.newUncheckedException;

public final class ClassUtil {

    static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource not found"; //NON-NLS

    ClassUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Return the resource <code>resourceName</code> of <code>theClass</code> as an {@link URL},
     * throw a {@link MissingResourceException} when the resource does not exist.
     *
     * <p>For details see {@link Class#getResource(String)}</p>
     */
    public static URL resource(Class<?> theClass, String resourceName) {

        URL url = theClass.getResource(resourceName);
        if (url == null) {
            throw new MissingResourceException(
                    RESOURCE_NOT_FOUND_MESSAGE, theClass.getName(), resourceName);
        }
        return url;
    }

    /**
     * Return the resource <code>resourceName</code> of <code>theClass</code> as an {@link InputStream},
     * throw a {@link MissingResourceException} when the resource does not exist.
     *
     * <p>For details see {@link Class#getResourceAsStream(String)}</p>
     */
    public static InputStream resourceAsStream(Class<?> theClass, String resourceName) {

        InputStream stream = theClass.getResourceAsStream(resourceName);
        if (stream == null) {
            throw new MissingResourceException(
                    RESOURCE_NOT_FOUND_MESSAGE, theClass.getName(), resourceName);
        }
        return stream;
    }

    /**
     * Return the name of the class of the {@code object}, or {@code null}
     * when {@code object} is {@code null}.
     */
    @Nullable
    public static String classNameOrNull(@Nullable Object object) {
        return object != null ? object.getClass().getName() : null;
    }

    /**
     * Return the package of <code>theClass</code> as a path, i.e. the name of
     * the package with the dots (".") replaced by "/".
     */
    public static String packagePath(Class<?> theClass) {
        return theClass.getPackage().getName().replaceAll("\\.", "/");
    }

    @SuppressWarnings("WeakerAccess")
    public static <T> T newInstanceOfClassNamed(String className, Class<T> expectedType) {
        try {
            Class<?> aClass = Class.forName(className);
            Object instance = aClass.getConstructor().newInstance();
            if (!expectedType.isInstance(instance)) {
                throw newUncheckedException(
                        format("%s expected, got %s", //NON-NLS
                                expectedType.getName(),
                                instance.getClass().getName()));
            }
            return expectedType.cast(instance);

        } catch (UncheckedException e) {
            throw e;
        } catch (Exception e) {
            throw newUncheckedException(
                    format("Error when loading '%s'", className), e); //NON-NLS
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T requireType(@Nullable Object object, Class<T> type) {
        if (!type.isInstance(object)) {
            throw new IllegalArgumentException(format("Expected type %s, got %s" //NON-NLS
                    , type.getName(), classNameOrNull(object)));
        }
        return (T) object;
    }

}
