/*
 * MIT License
 *
 * Copyright (c) 2018 Udo Borkowski, (ub@abego.org)
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

package org.abego.commons;

public class TestData {
    // --- Literal Strings/chars

    public static final String EMPTY_TEXT = ""; // NON-NLS
    public static final String SAMPLE_TEXT = "aäñ"; // NON-NLS
    public static final String SAMPLE_TEXT_SUBSTRING_1_2 = "äñ"; // NON-NLS
    public static final char SAMPLE_TEXT_CHAR_0 = 'a'; // NON-NLS
    public static final char SAMPLE_TEXT_CHAR_1 = 'ä'; // NON-NLS
    public static final char SAMPLE_TEXT_CHAR_2 = 'ñ'; // NON-NLS
    public static final String SAMPLE_TEXT_2 = "foo"; // NON-NLS
    public static final String SAMPLE_TEXT_3 = "bar"; // NON-NLS

    // --- Resources

    public static final String EMPTY_TXT_RESOURCE_NAME = "empty.txt"; // NON-NLS
    public static final String EMPTY_TXT_TEXT = ""; // NON-NLS

    public static final String SAMPLE_TXT_RESOURCE_NAME = "sample.txt"; // NON-NLS
    public static final String SAMPLE_TXT_TEXT = "aäñ"; // NON-NLS

    public static final String SAMPLE_ISO_8859_1_TXT_RESOURCE_NAME = "sample-ISO-8859-1.txt"; // NON-NLS
    public static final String SAMPLE_ISO_8859_1_TXT_TEXT = "aäñ"; // NON-NLS

    public static final String MISSING_RESOURCE_NAME = "missing.txt";  // NON-NLS // no such resource

    public static final String SAMPLE_PROPERTIES_RESOURCE_NAME = "Sample.properties"; // NON-NLS
    public static final String SAMPLE_PROPERTIES_KEY_1 = "key1"; // NON-NLS
    public static final String SAMPLE_PROPERTIES_VALUE_1 = "value1"; // NON-NLS
    public static final String SAMPLE_PROPERTIES_KEY_2 = "key2"; // NON-NLS
    public static final String SAMPLE_PROPERTIES_VALUE_2 = "value2"; // NON-NLS

}
