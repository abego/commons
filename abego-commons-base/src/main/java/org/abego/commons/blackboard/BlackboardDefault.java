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

import org.abego.commons.lang.IterableUtil;
import org.abego.commons.seq.Seq;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;


final class BlackboardDefault<T> implements Blackboard<T> {
    private final List<T> itemList = new ArrayList<>();

    private BlackboardDefault() {
    }

    public static <T> BlackboardDefault<T> newBlackboardDefault() {
        return new BlackboardDefault<>();
    }

    @Override
    public boolean isEmpty() {
        synchronized (itemList) {
            return itemList.isEmpty();
        }
    }

    @Override
    public Seq<T> items() {
        synchronized (itemList) {
            return Seq.newSeq(new ArrayList<>(itemList));
        }
    }

    @Override
    @Nullable
    public T itemWithOrNull(Predicate<T> condition) {
        synchronized (itemList) {
            for (int i = itemList.size() - 1; i >= 0; i--) {
                T o = itemList.get(i);
                if (condition.test(o)) {
                    return o;
                }
            }
            return null;
        }
    }

    @Override
    public T itemWith(Predicate<T> condition) {
        T result = itemWithOrNull(condition);
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean containsItemWith(Predicate<T> condition) {
        return itemWithOrNull(condition) != null;
    }

    @Override
    public boolean contains(T item) {
        return itemWithOrNull(i -> i.equals(item)) != null;
    }

    @Override
    public String text() {
        synchronized (itemList) {
            return IterableUtil.textOf(itemList, "\n");
        }
    }

    @Override
    public void add(T item) {
        synchronized (itemList) {
            itemList.add(item);
        }
    }

    @Override
    public void clear() {
        synchronized (itemList) {
            itemList.clear();
        }
    }

    public String toString() {
        String origMessage = super.toString();
        return MessageFormat.format("{0}. Text:\n{1}", origMessage, text());  //NON-NLS-1
    }
}
