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

import org.eclipse.jdt.annotation.Nullable;

import java.util.regex.Matcher;

class ContextImpl implements LineProcessing.Context {
    private final LineProcessing.Context readonlyContext =
            new ReadOnlyContext(this);
    private String line = "";
    private int lineNumber;
    private @Nullable Matcher m;
    private boolean endOfTextReached = false;
    private boolean checkMoreRules = false;

    @Override
    public String line() {
        return line;
    }

    @Override
    public int lineNumber() {
        return lineNumber;
    }

    @Override
    public boolean isPatternMatchingRule() {
        return m != null;
    }

    @Override
    public Matcher m() {
        if (m == null) {
            throw new UndefinedMatcherException();
        }
        return m;
    }

    @Override
    public void more() {
        setCheckMoreRules(true);
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setMatcher(@Nullable Matcher m) {
        this.m = m;
    }

    public boolean getCheckMoreRules() {
        return checkMoreRules;
    }

    public void setCheckMoreRules(boolean value) {
        checkMoreRules = value;
    }

    public boolean isEndOfTextReached() {
        return endOfTextReached;
    }

    public void setEndOfTextReached(boolean endOfTextReached) {
        this.endOfTextReached = endOfTextReached;
    }

    public LineProcessing.Context asReadOnlyContext() {
        return readonlyContext;
    }
}
