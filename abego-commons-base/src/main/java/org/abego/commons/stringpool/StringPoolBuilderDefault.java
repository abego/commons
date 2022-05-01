/*
 * MIT License
 *
 * Copyright (c) 2021 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons.stringpool;


import org.abego.commons.vlq.VLQUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.abego.commons.io.FileUtil.runIOCode;
import static org.abego.commons.io.PrintStreamUtil.newPrintStream;
import static org.abego.commons.stringpool.StringPoolDefault.CHARSET_FOR_STRING_TEXT;


final class StringPoolBuilderDefault implements StringPoolBuilder {
    private final Map<String, Integer> stringToIDMap = new HashMap<>();
    private final ByteArrayOutputStream allStrings = new ByteArrayOutputStream();
    private final PrintStream allStringsPrintStream = newPrintStream(allStrings, CHARSET_FOR_STRING_TEXT);
    private final byte[] oneByte = new byte[1];

    private StringPoolBuilderDefault() {
        // ID == 0 represents "no string"/"null".
        // As we use the offset into the byte array as the ID the 0
        // refers to the first (0-th) byte in the array.
        // So add one char to make the real strings start at 1 (!= 0)
        allStringsPrintStream.write(0);
    }

    public static StringPoolBuilderDefault newStringPoolBuilderDefault() {
        return new StringPoolBuilderDefault();
    }

    @Override
    public int add(@Nullable String string) {
        // check for null.
        if (string == null) {
            return 0;
        }

        @NonNull String s = string; // We need this extra variable s to please Eclipse's Nullable checker

        @Nullable Integer id = stringToIDMap.get(s);

        // When already in pool, return id, otherwise add the String.
        //
        // The String is stored in the byte array as its length (number of bytes,
        // in VLQ encoding) immediately followed by the bytes of text of the string in UTF-8.
        // The offset of the first length byte is also used as the String's ID.

        return id != null ? id : addStringWithId(s, allStrings.size());

    }

    private int addStringWithId(String s, Integer id) {
        int byteCount = s.getBytes(CHARSET_FOR_STRING_TEXT).length;
        VLQUtil.encodeUnsignedIntAsVLQ(byteCount, this::writeByte);
        allStringsPrintStream.print(s);

        // make sure the String can be found and reused in a future "add" call.
        stringToIDMap.put(s, id);

        return id;
    }

    private void writeByte(byte b) {
        oneByte[0] = b;
        runIOCode(() -> allStringsPrintStream.write(oneByte));
    }

    @Override
    public StringPool build() {
        return StringPoolDefault.newStringPoolDefault(allStrings.toByteArray());
    }

}
