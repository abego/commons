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

package org.abego.commons.vlq;

import org.abego.commons.lang.exception.MustNotInstantiateException;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static org.abego.commons.vlq.VLQUtil.VALUE_MUST_NOT_BE_NEGATIVE_MESSAGE;
import static org.abego.commons.vlq.VLQUtil.VLQ_ENCODED_NUMBER_TO_LARGE_FOR_UINT_MESSAGE;
import static org.abego.commons.vlq.VLQUtil.VLQ_ENCODED_NUMBER_TO_LARGE_FOR_ULONG_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VLQUtilTest {

    private static Integer[] asVLQIntegerArray(int value) {
        List<Integer> result = new ArrayList<>();

        VLQUtil.encodeUnsignedIntAsVLQ(value, b -> result.add((int) b));
        return result.toArray(new Integer[0]);
    }

    private static Integer[] asVLQIntegerArray(long value) {
        List<Integer> result = new ArrayList<>();

        VLQUtil.encodeUnsignedLongAsVLQ(value, b -> result.add((int) b));
        return result.toArray(new Integer[0]);
    }

    private static int unsignedIntFromVLQ(@NonNull Integer[] integers) {
        Deque<Integer> deque = new ArrayDeque<>(Arrays.asList(integers));
        return VLQUtil.decodeUnsignedIntFromVLQ(() -> deque.removeFirst().byteValue());
    }

    private static long unsignedLongFromVLQ(@NonNull Integer[] integers) {
        Deque<Integer> deque = new ArrayDeque<>(Arrays.asList(integers));
        return VLQUtil.decodeUnsignedLongFromVLQ(() -> deque.removeFirst().byteValue());
    }

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, VLQUtil::new);
    }

    @Test
    void encodeUnsignedIntAsVLQ_negative() {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                VLQUtil.encodeUnsignedIntAsVLQ(-1, b -> {/*do nothing*/}));
        assertEquals(VALUE_MUST_NOT_BE_NEGATIVE_MESSAGE, e.getMessage());
    }

    @Test
    void encodeUnsignedIntAsVLQ_OK() {

        assertArrayEquals(new Integer[]{-128}, asVLQIntegerArray(0));
        assertArrayEquals(new Integer[]{-127}, asVLQIntegerArray(1));

        assertArrayEquals(new Integer[]{-2}, asVLQIntegerArray(126));
        assertArrayEquals(new Integer[]{-1}, asVLQIntegerArray(127));
        assertArrayEquals(new Integer[]{0, -127}, asVLQIntegerArray(128));
        assertArrayEquals(new Integer[]{1, -127}, asVLQIntegerArray(129));
        assertArrayEquals(new Integer[]{2, -127}, asVLQIntegerArray(130));

        assertArrayEquals(new Integer[]{126, -1}, asVLQIntegerArray(16382));
        assertArrayEquals(new Integer[]{127, -1}, asVLQIntegerArray(16383));
        assertArrayEquals(new Integer[]{0, 0, -127}, asVLQIntegerArray(16384));
        assertArrayEquals(new Integer[]{1, 0, -127}, asVLQIntegerArray(16385));
        assertArrayEquals(new Integer[]{2, 0, -127}, asVLQIntegerArray(16386));

        assertArrayEquals(new Integer[]{126, 127, 127, 127, -121}, asVLQIntegerArray(Integer.MAX_VALUE - 1));
        assertArrayEquals(new Integer[]{127, 127, 127, 127, -121}, asVLQIntegerArray(Integer.MAX_VALUE));
    }

    @Test
    void encodeUnsignedLongAsVLQ_OK() {

        assertArrayEquals(new Integer[]{-128}, asVLQIntegerArray(0L));
        assertArrayEquals(new Integer[]{-127}, asVLQIntegerArray(1L));

        assertArrayEquals(new Integer[]{-2}, asVLQIntegerArray(126L));
        assertArrayEquals(new Integer[]{-1}, asVLQIntegerArray(127L));
        assertArrayEquals(new Integer[]{0, -127}, asVLQIntegerArray(128L));
        assertArrayEquals(new Integer[]{1, -127}, asVLQIntegerArray(129L));
        assertArrayEquals(new Integer[]{2, -127}, asVLQIntegerArray(130L));

        assertArrayEquals(new Integer[]{126, -1}, asVLQIntegerArray(16382L));
        assertArrayEquals(new Integer[]{127, -1}, asVLQIntegerArray(16383L));
        assertArrayEquals(new Integer[]{0, 0, -127}, asVLQIntegerArray(16384L));
        assertArrayEquals(new Integer[]{1, 0, -127}, asVLQIntegerArray(16385L));
        assertArrayEquals(new Integer[]{2, 0, -127}, asVLQIntegerArray(16386L));

        assertArrayEquals(new Integer[]{126, 127, 127, 127, 127, 127, 127, 127, -1}, asVLQIntegerArray(Long.MAX_VALUE - 1));
        assertArrayEquals(new Integer[]{127, 127, 127, 127, 127, 127, 127, 127, -1}, asVLQIntegerArray(Long.MAX_VALUE));
    }


    @Test
    void decodeUnsignedIntFromVLQ_OK() {
        assertEquals(0, unsignedIntFromVLQ(new @NonNull Integer[]{-128}));
        assertEquals(1, unsignedIntFromVLQ(new @NonNull Integer[]{-127}));

        assertEquals(126, unsignedIntFromVLQ(new @NonNull Integer[]{-2}));
        assertEquals(127, unsignedIntFromVLQ(new @NonNull Integer[]{-1}));
        assertEquals(128, unsignedIntFromVLQ(new @NonNull Integer[]{0, -127}));
        assertEquals(129, unsignedIntFromVLQ(new @NonNull Integer[]{1, -127}));
        assertEquals(130, unsignedIntFromVLQ(new @NonNull Integer[]{2, -127}));

        assertEquals(16382, unsignedIntFromVLQ(new @NonNull Integer[]{126, -1}));
        assertEquals(16383, unsignedIntFromVLQ(new @NonNull Integer[]{127, -1}));
        assertEquals(16384, unsignedIntFromVLQ(new @NonNull Integer[]{0, 0, -127}));
        assertEquals(16385, unsignedIntFromVLQ(new @NonNull Integer[]{1, 0, -127}));
        assertEquals(16386, unsignedIntFromVLQ(new @NonNull Integer[]{2, 0, -127}));

        assertEquals(Integer.MAX_VALUE - 1, unsignedIntFromVLQ(new @NonNull Integer[]{126, 127, 127, 127, -121}));
        assertEquals(Integer.MAX_VALUE, unsignedIntFromVLQ(new @NonNull Integer[]{127, 127, 127, 127, -121}));
    }

    @Test
    void decodeUnsignedLongFromVLQ_OK() {
        assertEquals(0, unsignedLongFromVLQ(new @NonNull Integer[]{-128}));
        assertEquals(1, unsignedLongFromVLQ(new @NonNull Integer[]{-127}));

        assertEquals(126, unsignedLongFromVLQ(new @NonNull Integer[]{-2}));
        assertEquals(127, unsignedLongFromVLQ(new @NonNull Integer[]{-1}));
        assertEquals(128, unsignedLongFromVLQ(new @NonNull Integer[]{0, -127}));
        assertEquals(129, unsignedLongFromVLQ(new @NonNull Integer[]{1, -127}));
        assertEquals(130, unsignedLongFromVLQ(new @NonNull Integer[]{2, -127}));

        assertEquals(16382, unsignedLongFromVLQ(new @NonNull Integer[]{126, -1}));
        assertEquals(16383, unsignedLongFromVLQ(new @NonNull Integer[]{127, -1}));
        assertEquals(16384, unsignedLongFromVLQ(new @NonNull Integer[]{0, 0, -127}));
        assertEquals(16385, unsignedLongFromVLQ(new @NonNull Integer[]{1, 0, -127}));
        assertEquals(16386, unsignedLongFromVLQ(new @NonNull Integer[]{2, 0, -127}));

        assertEquals(Long.MAX_VALUE - 1, unsignedLongFromVLQ(new @NonNull Integer[]{126, 127, 127, 127, 127, 127, 127, 127, -1}));
        assertEquals(Long.MAX_VALUE, unsignedLongFromVLQ(new @NonNull Integer[]{127, 127, 127, 127, 127, 127, 127, 127, -1}));
    }

    @Test
    void decodeUnsignedIntFromVLQ_tooLarge() {

        // Check the smallest "too large" encoding
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> unsignedIntFromVLQ(new @NonNull Integer[]{0, 0, 0, 0, -120}));
        assertEquals(VLQ_ENCODED_NUMBER_TO_LARGE_FOR_UINT_MESSAGE, e.getMessage());

        // Check for 6 bytes. An unsigned int never needs more than 5 bytes to encode as VLQ
        e = assertThrows(IllegalStateException.class, () -> unsignedIntFromVLQ(new @NonNull Integer[]{0, 0, 0, 0, 0, 0}));
        assertEquals(VLQ_ENCODED_NUMBER_TO_LARGE_FOR_UINT_MESSAGE, e.getMessage());
    }

    @Test
    void decodeUnsignedLongFromVLQ_tooLarge() {

        // Check the smallest "too large" encoding
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> unsignedLongFromVLQ(new @NonNull Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -127}));
        assertEquals(VLQ_ENCODED_NUMBER_TO_LARGE_FOR_ULONG_MESSAGE, e.getMessage());

        // Check for 10 bytes. An unsigned long never needs more than 9 bytes to encode as VLQ
        e = assertThrows(IllegalStateException.class, () -> unsignedLongFromVLQ(new @NonNull Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        assertEquals(VLQ_ENCODED_NUMBER_TO_LARGE_FOR_ULONG_MESSAGE, e.getMessage());
    }

    @Test
    void writeSignedVLQInt_readSignedVLQInt() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // around 0, both sides
        VLQUtil.writeSignedVLQInt(0, outputStream);
        VLQUtil.writeSignedVLQInt(-1, outputStream);
        VLQUtil.writeSignedVLQInt(1, outputStream);
        VLQUtil.writeSignedVLQInt(-2, outputStream);
        VLQUtil.writeSignedVLQInt(2, outputStream);

        // around the switch from 1 to 2 bytes, both sides
        VLQUtil.writeSignedVLQInt(-62, outputStream);
        VLQUtil.writeSignedVLQInt(62, outputStream);
        VLQUtil.writeSignedVLQInt(-63, outputStream);
        VLQUtil.writeSignedVLQInt(63, outputStream);
        VLQUtil.writeSignedVLQInt(-64, outputStream);
        VLQUtil.writeSignedVLQInt(64, outputStream);
        VLQUtil.writeSignedVLQInt(-65, outputStream);
        VLQUtil.writeSignedVLQInt(65, outputStream);
        VLQUtil.writeSignedVLQInt(-66, outputStream);
        VLQUtil.writeSignedVLQInt(66, outputStream);

        // around the switch from 2 to 3 bytes, both sides
        VLQUtil.writeSignedVLQInt(-8190, outputStream);
        VLQUtil.writeSignedVLQInt(8190, outputStream);
        VLQUtil.writeSignedVLQInt(-8191, outputStream);
        VLQUtil.writeSignedVLQInt(8191, outputStream);
        VLQUtil.writeSignedVLQInt(-8192, outputStream);
        VLQUtil.writeSignedVLQInt(8192, outputStream);
        VLQUtil.writeSignedVLQInt(-8193, outputStream);
        VLQUtil.writeSignedVLQInt(8193, outputStream);
        VLQUtil.writeSignedVLQInt(-8194, outputStream);
        VLQUtil.writeSignedVLQInt(8194, outputStream);

        // at both ends of the signed int range
        VLQUtil.writeSignedVLQInt(Integer.MAX_VALUE - 1, outputStream);
        VLQUtil.writeSignedVLQInt(Integer.MAX_VALUE, outputStream);
        VLQUtil.writeSignedVLQInt(Integer.MIN_VALUE + 1, outputStream);
        VLQUtil.writeSignedVLQInt(Integer.MIN_VALUE, outputStream);

        // now try to read the signed numbers
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        assertEquals(0, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-1, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(1, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-2, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(2, VLQUtil.readSignedVLQInt(inputStream));

        // around the switch from 1 to 2 bytes, both sides
        assertEquals(-62, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(62, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-63, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(63, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-64, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(64, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-65, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(65, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-66, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(66, VLQUtil.readSignedVLQInt(inputStream));

        // around the switch from 2 to 3 bytes, both sides
        assertEquals(-8190, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(8190, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-8191, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(8191, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-8192, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(8192, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-8193, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(8193, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(-8194, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(8194, VLQUtil.readSignedVLQInt(inputStream));

        // at both ends of the signed int range
        assertEquals(Integer.MAX_VALUE - 1, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(Integer.MAX_VALUE, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(Integer.MIN_VALUE + 1, VLQUtil.readSignedVLQInt(inputStream));
        assertEquals(Integer.MIN_VALUE, VLQUtil.readSignedVLQInt(inputStream));
    }

}
