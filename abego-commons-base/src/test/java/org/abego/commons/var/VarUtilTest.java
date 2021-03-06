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

import org.abego.commons.io.FileUtil;
import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.abego.commons.var.VarUtil.newVar;
import static org.abego.commons.var.VarUtil.newVarNotEditable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VarUtilTest extends VarTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, VarUtil::new);
    }


    @Test
    void newTextFileVar(@TempDir File tempDir) {
        File file = new File(tempDir, "t.txt");

        FileUtil.writeText(file, "foo");

        Var<String> v = VarUtil.newTextFileVar(file);

        assertEquals("foo", v.get());

        v.set("bar");

        assertEquals("bar", v.get());
        assertEquals("bar", FileUtil.textOf(file));
    }

    @Override
    Var<String> newStringVar() {
        return newVar();
    }

    @Override
    Var<String> newStringVarNotEditable(String value) {
        return newVarNotEditable(value);
    }
}