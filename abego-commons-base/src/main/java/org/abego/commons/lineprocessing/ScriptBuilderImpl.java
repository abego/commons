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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

class ScriptBuilderImpl<S> implements LineProcessing.ScriptBuilder<S> {

    private final Supplier<S> stateProvider;
    private final List<Rule<S>> ruleList = new ArrayList<>();
    private BiConsumer<LineProcessing.Context, S> defaultAction = (c, s) -> {};
    private BiConsumer<LineProcessing.Context, S> endOfTextAction = (c, s) -> {};

    ScriptBuilderImpl(Supplier<S> stateProvider) {
        this.stateProvider = stateProvider;
    }

    public void onMatch(BiPredicate<LineProcessing.Context, S> condition, BiConsumer<LineProcessing.Context, S> action) {
        ruleList.add(new PredicateRule<>(condition, action));
    }

    public void onMatch(String regex, BiConsumer<LineProcessing.Context, S> action) {
        ruleList.add(new PatternMatchingRule<>(Pattern.compile(regex), action));
    }

    public void onDefault(BiConsumer<LineProcessing.Context, S> action) {
        defaultAction = action;
    }

    public void onEndOfText(BiConsumer<LineProcessing.Context, S> action) {
        endOfTextAction = action;
    }

    public LineProcessing.Script build() {
        return new ScriptImpl<>(
                stateProvider,
                new ArrayList<>(ruleList),
                defaultAction,
                endOfTextAction);
    }
}
