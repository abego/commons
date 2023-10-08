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

package org.abego.commons.lang;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.abego.commons.lang.ObjectUtil.checkType;
import static org.abego.commons.lang.ObjectUtil.compareAsTexts;
import static org.abego.commons.lang.ObjectUtil.ignore;
import static org.abego.commons.lang.ObjectUtil.valueOrElse;
import static org.abego.commons.lang.ObjectUtil.valueOrFail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectUtilTest {

    @Test
    void constructorFails() {
        assertThrows(MustNotInstantiateException.class, ObjectUtil::new);
    }

    @Test
    void ignoreOk() {
        ignore(null);
        ignore("a");
        ignore(1);
        ignore(true);
    }

    @Test
    void checkTypeOk() {
        String s = checkType("foo", String.class);
        assertEquals("foo", s);
    }

    @Test
    void checkType_failOk() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> checkType(1, String.class));
        assertEquals("Object is not of type java.lang.String (but java.lang.Integer)", e.getMessage());
    }

    @Test
    void checkType_nullFailOk() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> checkType(null, String.class));
        assertEquals("Object is not of type java.lang.String (but null)", e.getMessage());
    }

    @Test
    void valueOrFail_withMessage() {

        String value = "foo";
        assertEquals(value, valueOrFail(value, "value is null"));

        NullPointerException e = assertThrows(NullPointerException.class,
                () -> valueOrFail(null, "value is null"));
        assertEquals("value is null", e.getMessage());
    }

    @Test
    void valueOrFail_noMessage() {

        String value = "foo";
        assertEquals(value, valueOrFail(value));

        NullPointerException e = assertThrows(NullPointerException.class,
                () -> valueOrFail(null));
        assertNull(e.getMessage());
    }

    @Test
    void valueOrFail_withMessageSupplier() {

        String value = "foo";
        Supplier<String> messageSupplier = () -> "value is null";

        assertEquals(value, ObjectUtil.valueOrFail(value, messageSupplier));

        NullPointerException e = assertThrows(NullPointerException.class,
                () -> ObjectUtil.valueOrFail(null, messageSupplier));
        assertEquals("value is null", e.getMessage());
    }

    @Test
    void valueOrElseOK() {
        @Nullable String foo = "foo";
        assertEquals("foo", valueOrElse(foo, "bar"));
        assertEquals("bar", valueOrElse(null, "bar"));
    }

    @Test
    void compareAsText_OK() {
        assertEquals(0, compareAsTexts(null, null));
        assertTrue(compareAsTexts("foo", null) < 0);
        assertTrue(compareAsTexts(null, "foo") > 0);

        assertEquals(0, compareAsTexts("0", 0));
        assertTrue(compareAsTexts("10", 2) < 0);

        assertTrue(compareAsTexts("foo", "bar") > 0);
        assertEquals(0, compareAsTexts("foo", "foo"));
        assertTrue(compareAsTexts("Foo", "foo") < 0);
        assertTrue(compareAsTexts("foo", "Foo") > 0);
    }

    @Test
    void allAreNotNull() {
        assertTrue(ObjectUtil.allAreNotNull());
        assertTrue(ObjectUtil.allAreNotNull("a"));
        assertTrue(ObjectUtil.allAreNotNull("a", 0));

        assertFalse(ObjectUtil.allAreNotNull((Object) null));
        assertFalse(ObjectUtil.allAreNotNull("a", null));
        assertFalse(ObjectUtil.allAreNotNull("a", "b", null));
        assertFalse(ObjectUtil.allAreNotNull(null, null));
    }

    @Test
    void instanceOfOrNull() {
        assertEquals("foo", ObjectUtil.instanceOfOrNull("foo", String.class));
        assertNull(ObjectUtil.instanceOfOrNull(1, String.class));
        assertNull(ObjectUtil.instanceOfOrNull(null, String.class));
    }
}

