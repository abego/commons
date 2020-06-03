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

package org.abego.commons.var;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class VarTest {
    abstract Var<String> newStringVar();

    @Test
    void get_noValueOK() {
        Var<String> v = newStringVar();

        // Accessing a new Var without initializing the value fails
        assertThrows(IllegalStateException.class, v::get);
    }

    @Test
    void setGet() {
        Var<String> v = newStringVar();

        v.set("foo");
        assertEquals("foo", v.get());
        v.set("bar");
        assertEquals("bar", v.get());
    }

    @Test
    void getOrElse_defaultValue() {
        Var<String> v = newStringVar();

        assertEquals("qux", v.getOrElse("qux"));

        v.set("foo");
        assertEquals("foo", v.getOrElse("qux"));
    }

    @Test
    void getOrElse_defaultValueSupplier() {
        Var<String> v = newStringVar();

        assertEquals("qux", v.getOrElse(() -> "qux"));

        v.set("foo");
        assertEquals("foo", v.getOrElse(() -> "qux"));
    }

    @Test
    void hasValue_OK() {
        Var<String> v = newStringVar();

        assertFalse(v.hasValue());

        v.set("foo");
        assertTrue(v.hasValue());

        // test the default implementation
        Var<Integer> ivar = new Var<Integer>() {
            private Integer value = 1;

            @NonNull
            @Override
            public Integer get() {
                return value;
            }

            @Override
            public void set(@NonNull Integer value) {
                this.value = value;
            }
        };
        assertTrue(ivar.hasValue());

    }
}
