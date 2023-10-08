/*
 * MIT License
 *
 * Copyright (c) 2023 Udo Borkowski, (ub@abego.org)
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

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public final class ThrowableUtil {

    public static final String DEFAULT_EXCEPTION_MESSAGE_SEPARATOR = ":\n";

    ThrowableUtil() {
        throw new MustNotInstantiateException();
    }

    /**
     * Return the message of the <code>throwable</code> if it has one, otherwise
     * the name of the class of the throwable.
     */
    public static String messageOrClassName(Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null || message.isEmpty()) {
            message = throwable.getClass().getName();
        }
        return message;
    }

    public static String messageOrToString(Throwable throwable) {
        String message = throwable.getMessage();
        return message != null ? StringUtil.quoted2(message) : throwable.toString();
    }

    public static String allMessages(Throwable throwable,
                                     String messageSeparator) {
        StringBuilder result = new StringBuilder();
        appendAllMessages(throwable, result, messageSeparator);
        return result.toString();
    }

    public static void appendAllMessages(
            @Nullable Throwable throwable, StringBuilder stringBuilder,
            String messageSeparator) {
        @Nullable Throwable ex = throwable;
        while (ex != null) {

            String message = ex.getMessage();
            if (StringUtil.hasText(message)) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(messageSeparator);
                }
                stringBuilder.append(message);
            }

            ex = ex.getCause();
        }
    }

    public static String allMessagesOrClassName(Throwable throwable) {
        return allMessagesOrClassName(throwable,
                DEFAULT_EXCEPTION_MESSAGE_SEPARATOR);
    }

    public static String allMessagesOrClassName(Throwable throwable,
                                                String messageSeparator) {
        String result = allMessages(throwable, messageSeparator);

        return !result.isEmpty() ? result : throwable.getClass().getName();
    }

}
