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

import org.abego.commons.lang.IterableUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ServiceConfigurationError;

import static org.abego.commons.util.ServiceLoaderUtil.loadService;
import static org.abego.commons.util.ServiceLoaderUtil.loadServices;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceLoaderUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, ServiceLoaderUtil::new);
    }

    @Test
    void loadService_happyPath() {
        Assertions.assertNotNull(loadService(LoadServiceTestInterfaceWithPublicClass.class));
    }

    @Test
    void loadService_privateConstructor() {
        assertThrows(ServiceConfigurationError.class, () ->
                loadService(LoadServiceTestInterfaceWithPrivateConstructor.class));
    }

    @Test
    void loadService_nonPublicClass() {
        assertThrows(ServiceConfigurationError.class, () ->
                loadService(LoadServiceTestInterfaceWithNonPublicClass.class));
    }

    @Test
    void loadService_missingImplementation() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                loadService(MyTestInterface.class));
        assertEquals("No implementation found for interface org.abego.commons.util.ServiceLoaderUtilTest$MyTestInterface", e.getMessage());
    }

    private interface MyTestInterface {
    }

    @Test
    void loadServices_happyPath() {
        Iterable<LoadServiceTestInterfaceWithPublicClass> services =
                loadServices(LoadServiceTestInterfaceWithPublicClass.class);

        Assertions.assertFalse(IterableUtil.isEmpty(services));
    }

    @Test
    void loadServices_happyPath_withPredicate() {

        Iterable<LoadServiceTestInterfaceWithPublicClass> services =
                loadServices(
                        LoadServiceTestInterfaceWithPublicClass.class,
                        // predicate is false, i.e. no service will be returned
                        s -> false);

        Assertions.assertTrue(IterableUtil.isEmpty(services));
    }

    @Test
    void loadServices_missingImplementation() {
        Iterable<MyTestInterface> services =
                loadServices(MyTestInterface.class);

        Assertions.assertTrue(IterableUtil.isEmpty(services));
    }

}
