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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

class ScriptImpl<S> implements LineProcessing.Script {
    private final Supplier<S> stateProvider;
    private final List<Rule<S>> ruleList;
    private final BiConsumer<LineProcessing.Context, S> defaultAction;
    private final BiConsumer<LineProcessing.Context, S> endOfTextAction;

    ScriptImpl(
            Supplier<S> stateProvider,
            List<Rule<S>> ruleList,
            BiConsumer<LineProcessing.Context, S> defaultAction,
            BiConsumer<LineProcessing.Context, S> endOfTextAction) {

        this.stateProvider = stateProvider;
        this.ruleList = ruleList;
        this.defaultAction = defaultAction;
        this.endOfTextAction = endOfTextAction;
    }


    @Override
    public void process(String text) {
        try (BufferedReader reader = new BufferedReader(
                new StringReader(text))) {
            process(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void process(BufferedReader reader) {
        S state = stateProvider.get();
        ContextImpl context = new ContextImpl();
        int lineNumber = 0;
        String line;
        do {
            try {
                line = reader.readLine();
                if (line != null) {
                    lineNumber++;
                    context.setLine(line);
                    context.setLineNumber(lineNumber);
                    apply(context, state);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } while (line != null);
        context.setEndOfTextReached(true);
        apply(context, state);
    }

    @Override
    public void process(InputStream inputStream, Charset charset) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, charset))) {
            process(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void process(InputStream inputStream) {
        process(inputStream, StandardCharsets.UTF_8);
    }


    private void apply(ContextImpl context, S state) {
        if (context.isEndOfTextReached()) {
            endOfTextAction.accept(context, state);

        } else {
            boolean runDefaultAction = true;
            for (Rule<S> rule : ruleList) {
                context.setCheckMoreRules(false);
                if (rule.apply(context, state)) {
                    // found a matching rule, no need to run the default action
                    runDefaultAction = false;
                    // unless the rule called the "more()" method
                    // we are done.
                    if (!context.getCheckMoreRules()) {
                        return;
                    }
                }
            }
            if (runDefaultAction) {
                defaultAction.accept(context, state);
            }
        }
    }
}
