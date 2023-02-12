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

package org.abego.commons.stringpool;


import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.abego.commons.lang.IterableUtil.asSortedLines;
import static org.abego.commons.stringpool.StringPoolBuilderDefault.newStringPoolBuilderDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class StringPoolTest {

    private static String newTestString(int size) {
        char[] chars = new char[size];
        for (int i = 0; i < size; i++) {
            chars[i] = (char) (65 + i % 13);
        }
        return new String(chars);
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    void getString() {
        StringPoolBuilder spb = newStringPoolBuilder();

        int idHello = spb.add("Hello");
        int idWorld = spb.add("world");
        int idHello2 = spb.add("Hello");
        int idDolly = spb.add("Dolly");
        int idNull = spb.add(null);

        assertEquals(0, idNull);
        assertEquals(idHello, idHello2);

        StringPool sp = spb.build();

        assertEquals("Hello", sp.getStringOrNull(idHello));
        assertEquals("world", sp.getStringOrNull(idWorld));
        assertEquals("Dolly", sp.getStringOrNull(idDolly));
        assertEquals(null, sp.getStringOrNull(0));
        assertThrows(NullPointerException.class, () -> sp.getString(0));
    }

    StringPoolBuilder newStringPoolBuilder() {
        return newStringPoolBuilderDefault();
    }

    @Test
    void getString_longStrings() {
        // the implementation uses VLQ encoding. So test strings around the
        // "critical" length of 127 and 127*127 (= 16129).

        String s126 = newTestString(126);
        String s127 = newTestString(127);
        String s128 = newTestString(128);
        String s129 = newTestString(129);

        String s16128 = newTestString(16128);
        String s16129 = newTestString(16129);
        String s16130 = newTestString(16130);
        String s16131 = newTestString(16131);

        StringPoolBuilder spb = newStringPoolBuilder();


        int s128ID = spb.add(s128);
        int s127ID = spb.add(s127);
        int s126ID = spb.add(s126);
        int s129ID = spb.add(s129);

        int s16128ID = spb.add(s16128);
        int s16129ID = spb.add(s16129);
        int s16130ID = spb.add(s16130);
        int s16131ID = spb.add(s16131);

        StringPool sp = spb.build();

        assertEquals(s128, sp.getStringOrNull(s128ID));
        assertEquals(s127, sp.getStringOrNull(s127ID));
        assertEquals(s126, sp.getStringOrNull(s126ID));
        assertEquals(s129, sp.getStringOrNull(s129ID));

        assertEquals(s16128, sp.getStringOrNull(s16128ID));
        assertEquals(s16129, sp.getStringOrNull(s16129ID));
        assertEquals(s16130, sp.getStringOrNull(s16130ID));
        assertEquals(s16131, sp.getStringOrNull(s16131ID));
    }

    @Test
    void allStrings() {
        StringPoolBuilder spb = newStringPoolBuilder();

        spb.add("Hello");
        spb.add("world");
        spb.add("Hello");
        spb.add("Dolly");
        spb.add(null);

        StringPool sp = spb.build();

        Iterator<String> iter = sp.allStrings().iterator();

        assertEquals("Hello", iter.next());
        assertEquals("world", iter.next());
        assertEquals("Dolly", iter.next());
        assertFalse(iter.hasNext());

        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    void allStringsOneItem() {
        StringPoolBuilder spb = newStringPoolBuilder();

        spb.add("Hello");

        StringPool sp = spb.build();

        Iterator<String> iter = sp.allStrings().iterator();

        assertEquals("Hello", iter.next());
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    void allStringAndIDs() {
        StringPoolBuilder spb = newStringPoolBuilder();

        int idHello = spb.add("Hello");
        int idWorld = spb.add("world");
        spb.add("Hello");
        int idDolly = spb.add("Dolly");
        spb.add(null);

        StringPool sp = spb.build();

        Iterator<StringPool.StringAndID> iter = sp.allStringAndIDs().iterator();

        StringPool.StringAndID item = iter.next();
        assertEquals("Hello", item.getString());
        assertEquals(idHello, item.getID());

        item = iter.next();
        assertEquals("world", item.getString());
        assertEquals(idWorld, item.getID());

        item = iter.next();
        assertEquals("Dolly", item.getString());
        assertEquals(idDolly, item.getID());

        assertFalse(iter.hasNext());

        assertThrows(NoSuchElementException.class, iter::next);

    }

    @Test
    void addJoined() {
        StringPoolBuilder spb = newStringPoolBuilder();

        int idFoobar = spb.addJoined("foo", "bar");
        // adding a string already added as a joined string should return the old one.
        int idFoobar2 = spb.add("foobar");

        // using 3 parts
        int idHelloWorld = spb.addJoined("Hello", " ", "World");

        // using 4 parts
        int idHelloDolly = spb.addJoined("Hello", " ", "Dolly", "!");
        // same as the line above but with extra null and empty values
        int idHelloDolly2 = spb.addJoined(null, "", "Hello", " ", null, "", "Dolly", "!");

        StringPool sp = spb.build();

        assertEquals(idFoobar, idFoobar2);
        assertEquals("foobar", sp.getString(idFoobar));

        assertEquals("Hello World", sp.getString(idHelloWorld));

        assertEquals(idHelloDolly, idHelloDolly2);
        assertEquals("Hello Dolly!", sp.getString(idHelloDolly));
    }

    @Test
    void addJoinedCheckForReuse() {
        StringPoolBuilder spb = newStringPoolBuilder();

        // add some common separators first to ensure they get small IDs
        // (to fit in one byte)
        spb.addJoined("com.example.myapp");
        spb.addJoined("com.example.myapp", ".", "ClassA");
        spb.addJoined("com.example.myapp", ".", "ClassB");
        spb.addJoined("com.example.myapp", ".", "ClassB", "#", "method1", "(", ")");
        spb.addJoined("com.example.myapp", ".", "ClassB", "#", "method2", "(", "int", ")");
        spb.addJoined("com.example.myapp", ".", "ClassB", "#", "method2", "(", "int", ",", "int", ")");
        spb.addJoined("com.example.myapp", ".", "ClassB", "#", "method2", "(", "String", ",", "int", ")");
        spb.addJoined("com.example.myapp", ".", "ClassB", "#", "method2(String,int)");

        StringPool sp = spb.build();

        // Note: these assertions may fail in the future when the implementation 
        // of StringPoolBuilderDefault changes. 
        // Either adapt test then or remove, as it tests implementation details.
        String expectedText = "#\n" +
                "(\n" +
                ")\n" +
                ",\n" +
                ".\n" +
                "ClassA\n" +
                "ClassB\n" +
                "String\n" +
                "com.example.myapp\n" +
                "com.example.myapp.ClassA\n" +
                "com.example.myapp.ClassB\n" +
                "com.example.myapp.ClassB#method1()\n" +
                "com.example.myapp.ClassB#method2(String,int)\n" +
                "com.example.myapp.ClassB#method2(int)\n" +
                "com.example.myapp.ClassB#method2(int,int)\n" +
                "int\n" +
                "method1\n" +
                "method2";
        assertEquals("" +
                expectedText, asSortedLines(sp.allStrings()));

        // as the implementation reuses strings the space required for
        // the "bytes" array is actually smaller than the total length of the 
        // 'all strings'.
        assertTrue(sp.getBytes().length < expectedText.length());
    }

    @Test
    void byteAccess() {
        StringPoolBuilder spb = newStringPoolBuilder();

        spb.add("Hello");

        StringPool sp = spb.build();

        // The current implementation supports byte access
        assertTrue(sp.isByteAccessSupported());
        assertNotNull(sp.getBytes());
    }

    @Test
    void byteAccessNotSupported() {
        StringPool sp = new StringPool() {
            @Override
            public @Nullable String getStringOrNull(int id) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterable<String> allStrings() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterable<StringAndID> allStringAndIDs() {
                throw new UnsupportedOperationException();
            }
        };

        // The default implementation does not support byte access
        assertFalse(sp.isByteAccessSupported());
        assertThrows(UnsupportedOperationException.class, sp::getBytes);
    }

    @Test
    void contains() {
        StringPoolBuilder spb = newStringPoolBuilder();

        spb.add("Hello");

        StringPool sp = spb.build();

        // The current implementation supports byte access
        assertTrue(spb.contains(null));
        assertTrue(spb.contains("Hello"));
        assertFalse(spb.contains("World"));
    }


}
