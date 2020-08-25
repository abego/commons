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
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class VarUtil {

    VarUtil() {
        throw new MustNotInstantiateException();
    }

    public static <T> Var<T> newVar() {
        return new Variable<>();
    }

    public static <T> Var<T> newVarNotEditable(@NonNull T value) {
        return new VariableNotEditable<>(value);
    }

    public static Var<String> newTextFileVar(File file, Charset charset) {
        return new TextFileVar(file, charset);
    }

    public static Var<String> newTextFileVar(File file) {
        return newTextFileVar(file, StandardCharsets.UTF_8);
    }

    private static class Variable<T> implements Var<T> {
        private @Nullable T value;

        Variable() {
        }

        Variable(@NonNull T value) {
            this.value = value;
        }

        @Override
        public @NonNull T get() {
            @Nullable
            T v = value;
            if (v == null) {
                throw new IllegalStateException("Var has no value"); //NON-NLS
            }
            return v;
        }

        @Override
        public void set(@NonNull T value) {
            this.value = value;
        }

        @Override
        public boolean hasValue() {
            return value != null;
        }
    }

    private static class VariableNotEditable<T> extends Variable<T> {
        VariableNotEditable(@NonNull T value) {
            super(value);
        }

        @Override
        public void set(@NonNull T value) {
            throw new UnsupportedOperationException("Var not editable"); //NON-NLS
        }

        @Override
        public boolean isEditable() {
            return false;
        }
    }

    private static class TextFileVar implements Var<String> {

        private final File file;
        private final Charset charset;

        public TextFileVar(File file, Charset charset) {
            this.file = file;
            this.charset = charset;
        }

        @Override
        public String get() {
            return FileUtil.textOf(file, charset);
        }

        @Override
        public void set(String value) {
            FileUtil.writeText(file, value, charset);
        }
    }
}
