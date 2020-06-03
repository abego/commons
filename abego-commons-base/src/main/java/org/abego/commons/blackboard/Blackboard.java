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

package org.abego.commons.blackboard;

import org.abego.commons.seq.Seq;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static org.abego.commons.blackboard.BlackboardDefault.newBlackboardDefault;

/**
 * A Blackboard holds a collection of items.
 *
 * <p> A Blackboard is thread-safe, i.e. can be used as a mean of communication
 * between multiple threads.</p>
 *
 * <p> E.g. a test method may start some asynchronous task and wait for that
 * task to add a certain entry to the Blackboard.</p>
 *
 * <p> It is also useful when testing user interface code, as typically two
 * threads are involved here, the main thread and the EventDispatchThread. </p>
 */
public interface Blackboard<T> {

    static <T> Blackboard<T> newBlackboard() {
        return newBlackboardDefault();
    }

    /**
     * Return all items of this Blackboard, in the order they were added.
     *
     * <p>The result is an unmodifiable (shallow) copy of the items in the
     * Blackboard. The result will not change when this Blackboard is modified.</p>
     */
    Seq<T> items();

    /**
     * Return the item in the Blackboard that matches the <code>condition</code>.
     *
     * <p> When several items in the Blackboard match the condition return the
     * last added one.</p>
     *
     * <p>When no item matches the condition throw an
     * {@link java.util.NoSuchElementException}.</p>
     */
    T itemWith(Predicate<T> condition);

    /**
     * Return the item in the Blackboard that matches the <code>condition</code>.
     *
     * <p> When several items in the Blackboard match the condition return the
     * last added one.</p>
     *
     * <p>When no item matches the condition return <code>null</code>.</p>
     */
    @Nullable
    T itemWithOrNull(Predicate<T> condition);

    /**
     * Return <code>true</code> when this Blackboard is empty (does not contain
     * any item), <code>false</code> otherwise.
     */
    boolean isEmpty();

    /**
     * Return <code>true</code> when the Blackboard contains an item that
     * matches the <code>condition</code>, <code>false</code> otherwise.
     */
    boolean containsItemWith(Predicate<T> condition);

    /**
     * Return <code>true</code> when the Blackboard contains the given
     * <code>item</code>, <code>false</code> otherwise.
     */
    boolean contains(T item);

    /**
     * Return the items of the Blackboard as text.
     *
     * <p>Process the items in the order they were added and concatenate the
     * text of each item. Separate each item's text with a newline.</p>
     * <p>
     * (The text of an item is defined by <code>item.toString()</code>).
     */
    String text();

    /**
     * Add the <code>item</code> to this Blackboard.
     *
     * @param item the item to add.
     */
    void add(T item);

    /**
     * Remove all items from this Blackboard.
     */
    void clear();
}
