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

package org.abego.commons.seq;

/**
 * Same as {@link SeqNonEmpty} but also includes an {@link #appended(Object[])} method
 * that returns a new Seq consisting of the "old" items plus extra items.
 */
public interface SeqNonEmptyWithAppended<T> extends SeqNonEmpty<T> {

    /**
     * Return a new {SeqNonEmptyWithAppended with the same items as
     * <code>this</code> Seq plus the given <code>items</code> added (at the end).
     */
    @SuppressWarnings("unchecked")
    SeqNonEmptyWithAppended<T> appended(T... items);

    /**
     * Return a new {SeqNonEmptyWithAppended with the same items as
     * <code>this</code> Seq plus the given <code>items</code> added (at the end).
     */
    SeqNonEmptyWithAppended<T> appended(Iterable<T> items);
}
