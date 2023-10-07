/*
 * MIT License
 *
 * Copyright (c) 2022 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.lineprocessing;

import java.util.regex.Matcher;

class ReadOnlyContext implements LineProcessing.Context {
    private final LineProcessing.Context originalContext;

    ReadOnlyContext(LineProcessing.Context originalContext) {
        this.originalContext = originalContext;
    }

    @Override
    public String line() {
        return originalContext.line();
    }

    @Override
    public int lineNumber() {
        return originalContext.lineNumber();
    }

    @Override
    public boolean isPatternMatchingRule() {
        return originalContext.isPatternMatchingRule();
    }

    @Override
    public Matcher m() {
        return originalContext.m();
    }

    @Override
    public void more() {
        throw new UnsupportedOperationException("Must not call `more()`"); //NON-NLS
    }
}
