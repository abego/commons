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

package org.abego.commons.lang;

import org.abego.commons.io.PrintStreamToBuffer;
import org.abego.commons.lang.exception.MustNotInstantiateException;

import static org.abego.commons.io.FileUtil.runIOCode;
import static org.abego.commons.io.PrintStreamToBuffer.newPrintStreamToBuffer;
import static org.abego.commons.io.PrintStreamUtil.appendLines;

public final class RuntimeUtil {

    RuntimeUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Execute to the specified command and arguments in a separate process and
     * return the output (both "normal" and "error" output).
     */
    public static String execAndReturnOutAndErr(String... commandAndArguments) {
        return runIOCode(() -> {
            Process proc = Runtime.getRuntime().exec(commandAndArguments);


            PrintStreamToBuffer output = newPrintStreamToBuffer();
            appendLines(output, proc.getInputStream());
            appendLines(output, proc.getErrorStream());

            return output.text();

        });
    }


}
