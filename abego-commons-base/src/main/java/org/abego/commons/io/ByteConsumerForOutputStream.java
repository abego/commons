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

package org.abego.commons.io;

import org.abego.commons.util.function.ByteConsumer;

import java.io.OutputStream;

import static org.abego.commons.io.FileUtil.runIOCode;

/**
 * A ByteConsumer that writes the consumed byte to an OutputStream.
 */
@SuppressWarnings("WeakerAccess")
public class ByteConsumerForOutputStream implements ByteConsumer {
    private final OutputStream outputStream;
    private final byte[] oneByte = new byte[1];

    private ByteConsumerForOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public static ByteConsumerForOutputStream newByteConsumerForOutputStream(OutputStream outputStream) {
        return new ByteConsumerForOutputStream(outputStream);
    }

    @Override
    public void accept(byte b) {
        runIOCode(() -> {
            oneByte[0] = b;
            outputStream.write(oneByte);
        });
    }
}
