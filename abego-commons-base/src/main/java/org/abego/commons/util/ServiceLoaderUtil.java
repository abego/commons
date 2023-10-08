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

package org.abego.commons.util;

import org.abego.commons.lang.exception.MustNotInstantiateException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.ServiceLoader.load;

public final class ServiceLoaderUtil {

    ServiceLoaderUtil() {
        throw new MustNotInstantiateException();
    }

    //region loadService
    public static <T> T loadService(
            Class<T> service, ClassLoader loader, Predicate<T> selector) {
        int i = 0;
        for (T candidate : load(service, loader)) {
            i++;
            if (selector.test(candidate)) {
                return candidate;
            }
        }

        String message = i == 0
                ? String.format("No implementation found for %s", service)//NON-NLS
                : String.format("No appropriate implementation found for %s, %d checked.", service, i);//NON-NLS

        throw new IllegalArgumentException(message);
    }

    public static <T> T loadService(Class<T> service, ClassLoader loader) {
        return loadService(service, loader, o -> true);
    }

    public static <T> T loadService(Class<T> service, Predicate<T> selector) {
        return loadService(
                service, Thread.currentThread().getContextClassLoader(), selector);
    }

    public static <T> T loadService(Class<T> service) {
        return loadService(service, o -> true);
    }
    //endregion

    //region loadServices
    public static <T> Iterable<T> loadServices(
            Class<T> service, ClassLoader loader, Predicate<T> selector) {
        List<T> result = new ArrayList<>();
        for (T candidate : load(service, loader)) {
            if (selector.test(candidate)) {
                result.add(candidate);
            }
        }
        return result;
    }

    public static <T> Iterable<T> loadServices(Class<T> service, ClassLoader loader) {
        return loadServices(service, loader, o -> true);
    }

    public static <T> Iterable<T> loadServices(Class<T> service, Predicate<T> selector) {
        return loadServices(
                service, Thread.currentThread().getContextClassLoader(), selector);
    }

    public static <T> Iterable<T> loadServices(Class<T> service) {
        return loadServices(service, o -> true);
    }
    //endregion
}
