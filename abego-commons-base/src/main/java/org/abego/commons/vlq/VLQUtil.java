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
import org.abego.commons.util.function.ByteConsumer;
import org.abego.commons.util.function.ByteSupplier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * A collection of VLQ-related methods.
 *
 * <p>A variable-length quantity (VLQ) is a universal code that uses an arbitrary number of binary octets (eight-bit
 * bytes) to represent an arbitrarily large integer.
 * (<a href="https://en.wikipedia.org/wiki/Variable-length_quantity">https://en.wikipedia.org/wiki/Variable-length_quantity</a>) </p>
 */
public final class VLQUtil {

    static final String VALUE_MUST_NOT_BE_NEGATIVE_MESSAGE = "value must not be negative"; //NON-NLS
    static final String VLQ_ENCODED_NUMBER_TO_LARGE_FOR_UINT_MESSAGE = "VLQ encoded number too large to fit into an unsigned int. Try to read it in a long."; //NON-NLS
    static final String VLQ_ENCODED_NUMBER_OUT_OF_RANGE_FOR_SIGNED_INT_MESSAGE = "VLQ encoded number out of range for unsigned int."; //NON-NLS
    static final String VLQ_ENCODED_NUMBER_TO_LARGE_FOR_ULONG_MESSAGE = "VLQ encoded number too large to fit into an unsigned long."; //NON-NLS
    private static final int MAX_7BIT_INT_VALUE = 127;
    private static final int UNSIGNED_INT_BYTE_MASK = 0x7f;
    private static final long UNSIGNED_LONG_BYTE_MASK = 0x7f;
    private static final long SIGN_BYTE_MASK = 0x80;

    VLQUtil() {
        throw new MustNotInstantiateException();
    }

    public static void encodeUnsignedIntAsVLQ(int value, ByteConsumer byteConsumer) {
        encodeUnsignedLongAsVLQ(value, byteConsumer);
    }

    public static void encodeSignedIntAsVLQ(int value, ByteConsumer byteConsumer) {
        boolean isNegative = value < 0;

        long unsignedValue = isNegative
                ? ((long) (-value - 1) << 1) ^ 1
                : (long) value << 1;
        encodeUnsignedLongAsVLQ(unsignedValue, byteConsumer);
    }

    public static void encodeUnsignedLongAsVLQ(long value, ByteConsumer byteConsumer) {
        if (value < 0) {
            throw new IllegalArgumentException(VALUE_MUST_NOT_BE_NEGATIVE_MESSAGE);
        }

        long v = value;

        boolean endReached;
        do {
            endReached = v <= MAX_7BIT_INT_VALUE;

            long byteToWrite = (v & UNSIGNED_INT_BYTE_MASK) | (endReached ? SIGN_BYTE_MASK : 0);
            v >>= 7;

            byteConsumer.accept((byte) byteToWrite);

        } while (!endReached);
    }

    public static int decodeUnsignedIntFromVLQ(ByteSupplier byteSupplier) {
        int value = 0;
        int shift = 0;

        do {
            byte byteRead = byteSupplier.get();
            value = value | ((byteRead & UNSIGNED_INT_BYTE_MASK) << shift);
            if (byteRead < 0) { // negative -> most significant bit set -> end reached

                // Make sure only to use 31 bits for an unsigned (4 byte) int.
                // VLQ Byte 1-4 have 7 significant bits (28) and the 5th byte
                // only 3. I.e. if the last byte has other bits sets than 0x87
                // (0x80 for the negative sign) we have an overrun.
                // (The last byte is the one with the 28 bit shift).
                if ((shift == 28) && ((byteRead & 0x78) != 0))
                    break; // overrun

                return value;
            }

            shift += 7;

        } while (shift <= 28);

        throw new IllegalStateException(VLQ_ENCODED_NUMBER_TO_LARGE_FOR_UINT_MESSAGE);
    }

    public static int decodeSignedIntFromVLQ(ByteSupplier byteSupplier) {
        long unsignedValue = decodeUnsignedLongFromVLQ(byteSupplier);
        long l = unsignedValue >> 1;
        if ((unsignedValue & 1) == 0) {
            // >= 0
            if (l > Integer.MAX_VALUE) {
                throw new IllegalStateException(VLQ_ENCODED_NUMBER_OUT_OF_RANGE_FOR_SIGNED_INT_MESSAGE);
            }
            return (int) l;
        } else {
            // negative
            long result = -l - 1;
            if (result < Integer.MIN_VALUE) {
                throw new IllegalStateException(VLQ_ENCODED_NUMBER_OUT_OF_RANGE_FOR_SIGNED_INT_MESSAGE);
            }
            return (int) result;
        }
    }

    public static long decodeUnsignedLongFromVLQ(ByteSupplier byteSupplier) {
        long value = 0;
        int shift = 0;

        do {
            byte byteRead = byteSupplier.get();
            value = value | ((byteRead & UNSIGNED_LONG_BYTE_MASK) << shift);
            if (byteRead < 0) { // negative -> most significant bit set -> end reached

                // Make sure only to use 63 bits for an unsigned (8 byte) long.
                // This is easier than the int (4 byte) case as the 63 bits
                // fit exactly into VLQ Byte 1-9, each having 7 significant bits
                // (9*7 = 63).
                return value;
            }

            shift += 7;

        } while (shift < 63);

        throw new IllegalStateException(VLQ_ENCODED_NUMBER_TO_LARGE_FOR_ULONG_MESSAGE);
    }

    public static void writeVLQInt(int value, ByteArrayOutputStream outputStream) {
        VLQUtil.encodeUnsignedIntAsVLQ(value, outputStream::write);
    }

    public static int readVLQInt(ByteArrayInputStream inputStream) {
        return VLQUtil.decodeUnsignedIntFromVLQ(() -> (byte) inputStream.read());
    }

    public static void writeSignedVLQInt(int value, ByteArrayOutputStream outputStream) {
        encodeSignedIntAsVLQ(value, outputStream::write);
    }

    public static int readSignedVLQInt(ByteArrayInputStream inputStream) {
        return VLQUtil.decodeSignedIntFromVLQ(() -> (byte) inputStream.read());
    }

    public static int[] readVLQIntArray(ByteArrayInputStream inputStream) {
        int size = readVLQInt(inputStream);
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = readVLQInt(inputStream);
        }
        return result;
    }

}
