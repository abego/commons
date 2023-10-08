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

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesGroupTest {
    @Test
    void smoketest() {
        PropertiesGroup pg = PropertiesGroup.newPropertiesGroup(
                "abego.commons.testing", "testing.abego", ".config/abego.de/testing");
        Properties props = pg.getProperties();
        // use some "strange" property names to avoid the test fails because
        // accidently somebody used the same name in a shared/global properties
        // file.
        String fooValue = pg.getProperty("foo-used-for-abego.commons-tests");
        String barValue = pg.getProperty("bar-used-for-abego.commons-tests", "baz");

        assertNotNull(props);
        assertNull(fooValue);
        assertEquals("baz", barValue);


        PropertiesGroup pg2 =
                PropertiesGroup.newPropertiesGroup("abego.commons.testing2");
        assertNotNull(pg2);

    }

    @Test
    void wrongGroupName() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> PropertiesGroup.newPropertiesGroup("abego"));

        assertEquals(
                "`abego` is the shared name and must not be used as a groupName.",
                e.getMessage());
    }
}
