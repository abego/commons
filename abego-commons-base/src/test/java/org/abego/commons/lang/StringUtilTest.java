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
import org.abego.commons.util.ListUtil;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.abego.commons.lang.ObjectUtil.ignore;
import static org.abego.commons.lang.StringUtil.arrayNullable;
import static org.abego.commons.lang.StringUtil.camelCased;
import static org.abego.commons.lang.StringUtil.containsAnyOf;
import static org.abego.commons.lang.StringUtil.dashCased;
import static org.abego.commons.lang.StringUtil.escaped;
import static org.abego.commons.lang.StringUtil.firstChar;
import static org.abego.commons.lang.StringUtil.firstLine;
import static org.abego.commons.lang.StringUtil.hasText;
import static org.abego.commons.lang.StringUtil.isNullOrEmpty;
import static org.abego.commons.lang.StringUtil.join;
import static org.abego.commons.lang.StringUtil.joinWithEmptyStringForNull;
import static org.abego.commons.lang.StringUtil.lastChar;
import static org.abego.commons.lang.StringUtil.limitString;
import static org.abego.commons.lang.StringUtil.quoted;
import static org.abego.commons.lang.StringUtil.quoted2;
import static org.abego.commons.lang.StringUtil.repeat;
import static org.abego.commons.lang.StringUtil.singleQuoted;
import static org.abego.commons.lang.StringUtil.singleQuotedStringWithoutEscapes;
import static org.abego.commons.lang.StringUtil.snakeCased;
import static org.abego.commons.lang.StringUtil.snakeUpperCased;
import static org.abego.commons.lang.StringUtil.splitWhitespaceSeparatedString;
import static org.abego.commons.lang.StringUtil.string;
import static org.abego.commons.lang.StringUtil.stringOrDefault;
import static org.abego.commons.lang.StringUtil.substringSafe;
import static org.abego.commons.lang.StringUtil.toArray;
import static org.abego.commons.lang.StringUtil.toHtml;
import static org.abego.commons.lang.StringUtil.toLowerCaseSafe;
import static org.abego.commons.lang.StringUtil.toUpperCaseSafe;
import static org.abego.commons.lang.StringUtil.unescapeCharacters;
import static org.abego.commons.lang.StringUtil.withLineSeparatorsForNewlines;
import static org.abego.commons.range.IntRangeDefault.newIntRange;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilTest {

    @Test
    void constructor() {
        assertThrows(MustNotInstantiateException.class, StringUtil::new);
    }

    @Test
    void isNullOrEmpty_ok() {
        //noinspection ConstantConditions
        assertTrue(isNullOrEmpty(null));
        assertTrue(isNullOrEmpty(""));
        assertFalse(isNullOrEmpty("a"));
    }

    @Test
    void hasText_ok() {
        //noinspection ConstantConditions
        assertFalse(hasText(null));
        assertFalse(hasText(""));
        assertTrue(hasText("a"));
    }

    @Test
    void stringOrDefault_String() {
        assertEquals("b", stringOrDefault(null, "b"));
        assertEquals("b", stringOrDefault("", "b"));
        assertEquals("a", stringOrDefault("a", "b"));
    }

    @Test
    void stringOrDefault_Supplier() {
        assertEquals("b", stringOrDefault(null, () -> "b"));
        assertEquals("b", stringOrDefault("", () -> "b"));
        assertEquals("a", stringOrDefault("a", () -> "b"));
    }

    @Test
    void string_ok() {
        assertEquals("", string(null));
        assertEquals("", string(""));
        assertEquals("a", string("a"));
    }

    @Test
    void firstChar_ok() {
        assertEquals('a', firstChar("a"));
        assertEquals('a', firstChar("abc"));
        assertThrows(Exception.class, () -> ignore(firstChar("")));
    }

    @Test
    void lastChar_ok() {
        assertEquals('a', lastChar("a"));
        assertEquals('c', lastChar("abc"));
        assertThrows(Exception.class, () -> ignore(lastChar("")));
    }

    @Test
    void singleQuotedString_noEscapes_ok() {
        assertEquals("null", singleQuotedStringWithoutEscapes(null));
        assertEquals("'abc'", singleQuotedStringWithoutEscapes("abc"));
        assertEquals("'a\nb\\nc'", singleQuotedStringWithoutEscapes("a\nb\\nc"));
        assertEquals("''", singleQuotedStringWithoutEscapes(""));
        assertEquals("'3'", singleQuotedStringWithoutEscapes(3));
    }

    @Test
    void unescapeCharacters_ok() {
        assertEquals("abcnd", unescapeCharacters("a\\bc\\nd"));
        assertEquals("a\\.$d", unescapeCharacters("a\\\\\\.\\$d"));
        assertEquals("", unescapeCharacters(""));
    }

    @Test
    void join_javaDocSample() {
        String message = join("-", "Java", 3.14, null, true, '!');

        assertEquals("Java-3.14-null-true-!", message);
    }

    @Test
    void join_emptyNull_javaDocSample() {
        String message = joinWithEmptyStringForNull("-", "Java", 3.14, null, true, '!');

        assertEquals("Java-3.14--true-!", message);
    }


    @Test
    void quotedOk() {
        // null text
        assertEquals("Nil", quoted(null, "Nil"));
        assertEquals("null", quoted(null));

        // simple text
        assertEquals("\"abc\"", quoted("abc", "Nil"));
        assertEquals("\"abc\"", quoted("abc"));

        // text to escape
        String t = "a\b\f\n\r\t\\'\"\u0080\u0099 Tüt";
        assertEquals("\"a\\b\\f\\n\\r\\t\\\\'\\\"\\u0080\\u0099 T\\u00fct\"", quoted(t, "Nil"));
        assertEquals("\"a\\b\\f\\n\\r\\t\\\\'\\\"\\u0080\\u0099 T\\u00fct\"", quoted(t));
    }

    @Test
    void quoted2Ok() {
        // null text
        assertEquals("Nil", quoted(null, "Nil"));
        assertEquals("null", quoted(null));

        // simple text
        assertEquals("\"abc\"", quoted("abc", "Nil"));
        assertEquals("\"abc\"", quoted("abc"));

        // text to escape
        String t = "a\b\f\n\r\t\\'\"\u0080\u0099 Tüt";
        assertEquals("\"a\\b\\f\\n\\r\\t\\\\'\\\"\u0099 Tüt\"", quoted2(t, "Nil"));
        assertEquals("\"a\\b\\f\\n\\r\\t\\\\'\\\"\u0099 Tüt\"", quoted2(t));
    }

    @Test
    void camelCasedOk() {
        assertEquals("abcDef", camelCased("abcDef"));
        assertEquals("abcDef", camelCased("abc-def"));
        assertEquals("abcDef", camelCased("abc_def"));
        assertEquals("param0Disabled", camelCased("param0Disabled"));
        assertEquals("param0Disabled", camelCased("param0-disabled"));
        assertEquals("param0Disabled", camelCased("param0_disabled"));
        assertEquals("abcDefGhI", camelCased("abc \ndef -_ GhI"));
    }

    @Test
    void dashCasedOk() {
        assertEquals("abc-def", dashCased("abcDef"));
        assertEquals("abc-def", dashCased("abc-def"));
        assertEquals("abc-def", dashCased("abc_def"));
        assertEquals("param0-disabled", dashCased("param0Disabled"));
        assertEquals("param0-disabled", dashCased("param0-disabled"));
        assertEquals("param0-disabled", dashCased("param0_disabled"));
        assertEquals("abc-def-gh-i", dashCased("abc \ndef -_ GhI"));
    }

    @Test
    void snakeCasedOk() {
        assertEquals("abc_def", snakeCased("abcDef"));
        assertEquals("abc_def", snakeCased("abc-def"));
        assertEquals("abc_def", snakeCased("abc_def"));
        assertEquals("param0_disabled", snakeCased("param0Disabled"));
        assertEquals("param0_disabled", snakeCased("param0-disabled"));
        assertEquals("param0_disabled", snakeCased("param0_disabled"));
        assertEquals("abc_def_gh_i", snakeCased("abc \ndef -_ GhI"));
    }

    @Test
    void snakeUpperCasedOk() {
        assertEquals("ABC_DEF", snakeUpperCased("abcDef"));
        assertEquals("ABC_DEF", snakeUpperCased("abc-def"));
        assertEquals("ABC_DEF", snakeUpperCased("abc_def"));
        assertEquals("PARAM0_DISABLED", snakeUpperCased("param0Disabled"));
        assertEquals("PARAM0_DISABLED", snakeUpperCased("param0-disabled"));
        assertEquals("PARAM0_DISABLED", snakeUpperCased("param0_disabled"));
        assertEquals("ABC_DEF_GH_I", snakeUpperCased("abc \ndef -_ GhI"));
    }

    @Test
    void singleQuotedOk() {
        // null text
        assertEquals("Nil", singleQuoted(null, "Nil"));
        assertEquals("null", singleQuoted(null));

        // simple text
        assertEquals("'abc'", singleQuoted("abc", "Nil"));
        assertEquals("'abc'", singleQuoted("abc"));

        // text to escape
        String t = "a\b\f\n\r\t\\'\"\u0099 Tüt";
        assertEquals("'a\\b\\f\\n\\r\\t\\\\\\'\"\\u0099 T\\u00fct'", singleQuoted(t, "Nil"));
        assertEquals("'a\\b\\f\\n\\r\\t\\\\\\'\"\\u0099 T\\u00fct'", singleQuoted(t));
    }

    @Test
    void escapedOk() {
        // null text
        assertEquals("", escaped(null));

        // simple text
        assertEquals("abc", escaped("abc"));

        // text to escape
        String t = "a\b\f\n\r\t\\'\"\u001f\u0020\u0021...\u007d\u007e\u007f\u0080\u0081\u0099Z";
        assertEquals("a\\b\\f\\n\\r\\t\\\\'\\\"\\u001f !...}~\u007f\\u0080\\u0081\\u0099Z", escaped(t));
    }

    @Test
    void escapedCharOk() {
        assertEquals("a", escaped('a'));
        assertEquals("\\b", escaped('\b'));
        assertEquals("\\f", escaped('\f'));
        assertEquals("\\n", escaped('\n'));
        assertEquals("\\r", escaped('\r'));
        assertEquals("\\t", escaped('\t'));
        assertEquals("\\\\", escaped('\\'));
        assertEquals("'", escaped('\''));
        assertEquals("\\\"", escaped('"'));
        assertEquals("\\u0099", escaped('\u0099'));
        assertEquals("Z", escaped('Z'));
    }

    @Test
    void repeat_OK() {
        assertEquals("", repeat("foo", -1));
        assertEquals("", repeat("foo", 0));
        assertEquals("foo", repeat("foo", 1));
        assertEquals("foofoo", repeat("foo", 2));
        assertEquals("foofoofoo", repeat("foo", 3));
        assertEquals("", repeat("", 4));
        assertEquals("----", repeat("-", 4));
    }

    @Test
    void limitString_OK() {
        assertEquals("foobar", limitString("foobar", 7));
        assertEquals("foobar", limitString("foobar", 6));
        assertEquals("fo...", limitString("foobar", 5));
        assertEquals("f...", limitString("foobar", 4));
        assertEquals("...", limitString("foobar", 3));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> limitString("foobar", 2));
        assertEquals("truncatedStringSuffix.length must be <= maxLen, got 3 > 2",
                e.getMessage());
    }

    @Test
    void splitWhitespaceSeparatedString_OK() {
        Object[] items;

        // empty string
        items = splitWhitespaceSeparatedString("");
        assertEquals("", join(",", items));

        // single item string
        items = splitWhitespaceSeparatedString("foo");
        assertEquals("foo", join(",", items));

        // leading whitespace
        items = splitWhitespaceSeparatedString("\tfoo");
        assertEquals(",foo", join(",", items));

        // trailing whitespace
        items = splitWhitespaceSeparatedString("foo\t");
        assertEquals("foo,", join(",", items));

        // leading and trailing whitespace
        items = splitWhitespaceSeparatedString("\tfoo\t");
        assertEquals(",foo,", join(",", items));

        // multiple whitespace separator
        items = splitWhitespaceSeparatedString("\t\n\rfoo\t\n\r");
        assertEquals(",foo,", join(",", items));

        // multiple items
        items = splitWhitespaceSeparatedString("foo\tbar\rbaz\nqux quux");
        assertEquals("foo,bar,baz,qux,quux", join(",", items));
    }

    @Test
    void firstLine_OK() {
        assertEquals("foo", firstLine("foo\nbar\nbaz"));
        assertEquals("", firstLine(""));
        assertEquals("foo", firstLine("foo"));
    }

    @Test
    void toArray_OK() {
        Collection<String> collection = ListUtil.toList("foo", "bar", "baz");

        Object[] array = toArray(collection);

        assertEquals("foo,bar,baz", join(",", array));
    }

    @Test
    void toArray_emptyOK() {
        Collection<String> collection = ListUtil.toList();

        Object[] array = toArray(collection);

        assertEquals(0, array.length);
    }

    @Test
    void substringSafeOK() {
        assertEquals("fo", substringSafe("foo", -1, 2));
        assertEquals("fo", substringSafe("foo", 0, 2));
        assertEquals("o", substringSafe("foo", 1, 2));
        assertEquals("", substringSafe("foo", 2, 2));
        assertEquals("", substringSafe("foo", 3, 2));
        assertEquals("", substringSafe("foo", 4, 2));

        assertEquals("foo", substringSafe("foo", -1));
        assertEquals("foo", substringSafe("foo", 0));
        assertEquals("oo", substringSafe("foo", 1));
        assertEquals("o", substringSafe("foo", 2));
        assertEquals("", substringSafe("foo", 3));
        assertEquals("", substringSafe("foo", 4));
    }

    @Test
    void withLineSeparatorsForNewlinesOK() {
        String sep = SystemUtil.getLineSeparator();
        String s = "foo\nbar\nbaz\n";
        String expected = "foo" + sep + "bar" + sep + "baz" + sep;
        String actual = withLineSeparatorsForNewlines(s);

        assertEquals(expected, actual);
    }

    @Test
    void toHtmlOK() {
        String s = "foo&bar<1>-\"-'-baz";
        String expected = "foo&amp;bar&lt;1&gt;-&quot;-&#039;-baz";

        assertEquals(expected, toHtml(s));
    }

    @Test
    void toHtml_empty() {
        assertEquals("", toHtml(""));
    }

    @Test
    void toHtml_null() {
        assertNull(toHtml(null));
    }

    @Test
    void toLowerCaseSafe_OK() {
        assertEquals("null", toLowerCaseSafe(null));
        assertEquals("foo", toLowerCaseSafe("FOO"));
        assertEquals("foo", toLowerCaseSafe("fOO"));
        assertEquals("foo", toLowerCaseSafe("foo"));
        assertEquals("bäz", toLowerCaseSafe("BÄZ"));
        assertEquals("bäz", toLowerCaseSafe("Bäz"));
        assertEquals("bäz", toLowerCaseSafe("bäz"));
    }

    @Test
    void toUpperCaseSafe_OK() {
        assertEquals("null", toUpperCaseSafe(null));
        assertEquals("FOO", toUpperCaseSafe("FOO"));
        assertEquals("FOO", toUpperCaseSafe("fOO"));
        assertEquals("FOO", toUpperCaseSafe("foo"));
        assertEquals("BÄZ", toUpperCaseSafe("BÄZ"));
        assertEquals("BÄZ", toUpperCaseSafe("Bäz"));
        assertEquals("BÄZ", toUpperCaseSafe("bäz"));
    }

    @Test
    void arrayNullableOK() {

        assertEquals(0, arrayNullable().length);

        @Nullable String[] arr = arrayNullable("foo", null, "bar");
        assertEquals(3, arr.length);
        assertEquals("foo", arr[0]);
        assertNull(arr[1]);
        assertEquals("bar", arr[2]);

    }

    @Test
    void replaceRange() {
        // range empty, at start
        assertEquals("bar foo",
                StringUtil.replaceRange("foo", newIntRange(0, 0), "bar "));
        // range empty, in middle
        assertEquals("fodo",
                StringUtil.replaceRange("foo", newIntRange(2, 2), "d"));
        // range empty, at end
        assertEquals("foo bar",
                StringUtil.replaceRange("foo", newIntRange(3, 3), " bar"));

        // full range
        assertEquals("bar",
                StringUtil.replaceRange("foo", newIntRange(0, 3), "bar"));

        // sub range, start
        assertEquals("baroo",
                StringUtil.replaceRange("foo", newIntRange(0, 1), "bar"));
        // sub range, start
        assertEquals("fobar",
                StringUtil.replaceRange("foo", newIntRange(2, 3), "bar"));
        // sub range, middle
        assertEquals("fbaro",
                StringUtil.replaceRange("foo", newIntRange(1, 2), "bar"));
    }

    @Test
    void prefix() {
        String s = "foobar";

        assertEquals("", StringUtil.prefix(s, 0));
        assertEquals("f", StringUtil.prefix(s, 1));
        assertEquals("fo", StringUtil.prefix(s, 2));
        assertEquals("foo", StringUtil.prefix(s, 3));
        assertEquals("foob", StringUtil.prefix(s, 4));
        assertEquals("fooba", StringUtil.prefix(s, 5));
        assertEquals("foobar", StringUtil.prefix(s, 6));
        assertEquals("foobar", StringUtil.prefix(s, 7));
        assertEquals("fooba", StringUtil.prefix(s, -1));
        assertEquals("foob", StringUtil.prefix(s, -2));
        assertEquals("foo", StringUtil.prefix(s, -3));
        assertEquals("fo", StringUtil.prefix(s, -4));
        assertEquals("f", StringUtil.prefix(s, -5));
        assertEquals("", StringUtil.prefix(s, -6));
        assertEquals("", StringUtil.prefix(s, -7));
    }

    @Test
    void newIncludesStringPredicate_sample1() {
        String linesWithOptions = "" +
                "foo\n" +
                "bar*\n" +
                "baz";

        Predicate<String> predicate = StringUtil.newIncludesStringPredicate(linesWithOptions);

        assertFalse(predicate.test(""));
        assertTrue(predicate.test("foo"));
        assertFalse(predicate.test("foofoo"));
        assertTrue(predicate.test("bar"));
        assertTrue(predicate.test("bars"));
        assertTrue(predicate.test("baz"));
        assertFalse(predicate.test("doo"));
    }

    @Test
    void newIncludesStringPredicate_empty() {
        Predicate<String> predicate =
                StringUtil.newIncludesStringPredicate("");

        assertFalse(predicate.test(""));
        assertFalse(predicate.test("foo"));
    }

    @Test
    void newIncludesStringPredicate_anyString() {
        Predicate<String> predicate =
                StringUtil.newIncludesStringPredicate("*");

        assertTrue(predicate.test(""));
        assertTrue(predicate.test("foo"));
    }

    @Test
    void containsAnyOf_smoketest() {
        // first and only matches
        assertTrue(containsAnyOf("foobarbaz", "z"));
        // first of many matches
        assertTrue(containsAnyOf("foobarbaz", "z", "y", "x"));
        // last of many matches
        assertTrue(containsAnyOf("foobarbaz", "x", "y", "z"));
        // middle of many matches
        assertTrue(containsAnyOf("foobarbaz", "y", "z", "x"));
        // full text matches
        assertTrue(containsAnyOf("foobarbaz", "y", "foobarbaz"));

        // empty string always matches
        assertTrue(containsAnyOf("foobarbaz", "x", ""));

        //empty list -> no match
        assertFalse(containsAnyOf("foobarbaz"));
        // first and only does not match
        assertFalse(containsAnyOf("foobarbaz", "x"));
        // non of many not match
        assertFalse(containsAnyOf("foobarbaz", "x", "y", "y2"));
    }

    @Test
    void linecount() {
        assertEquals(1, StringUtil.lineCount(""));
        assertEquals(1, StringUtil.lineCount("a"));
        assertEquals(2, StringUtil.lineCount("a\nb"));
        assertEquals(2, StringUtil.lineCount("a\r\nb"));
        assertEquals(3, StringUtil.lineCount("a\n\nb"));
        assertEquals(3, StringUtil.lineCount("a\r\n\r\nb"));
        assertEquals(4, StringUtil.lineCount("a\n\nb\n"));
    }

    @Test
    void compareToIgnoreCaseStable() {

        // Notice: when comparing case-sensitive: "A" < "B" < "a" < "b"

        assertTrue(StringUtil.compareToIgnoreCaseStable("A", "B") < 0);
        assertTrue(StringUtil.compareToIgnoreCaseStable("a", "B") < 0);

        assertEquals(0, StringUtil.compareToIgnoreCaseStable("A", "A"));
        assertTrue(StringUtil.compareToIgnoreCaseStable("a", "A") > 0);
    }

    @Test
    void endsWithIgnoreCase() {
        assertTrue(StringUtil.endsWithIgnoreCase("foo.txt", ".txt"));
        assertTrue(StringUtil.endsWithIgnoreCase("foo.TXT", ".txt"));
        assertTrue(StringUtil.endsWithIgnoreCase("foo.txt", ".TxT"));
        assertTrue(StringUtil.endsWithIgnoreCase("foo.TXT", ".TxT"));

        assertFalse(StringUtil.endsWithIgnoreCase("foo", ".TxT"));
        assertFalse(StringUtil.endsWithIgnoreCase("", ".TxT"));
    }

    @Test
    void newIncludesStringPredicate() {
        String[] options = {"foo", "bar*"};
        Predicate<String> predicate = StringUtil.newIncludesStringPredicate(options);

        // without wildcard
        assertTrue(predicate.test("foo"));
        assertFalse(predicate.test("fo"));
        assertFalse(predicate.test("fooo"));

        // with wildcard
        assertTrue(predicate.test("bar"));
        assertFalse(predicate.test("ba"));
        assertTrue(predicate.test("barr"));
        assertFalse(predicate.test(""));
    }

    @Test
    void indent() {
        StringBuilder out = new StringBuilder();
        Consumer<String> addLine = s -> {
            out.append(s);
            out.append("\n");
        };
        Consumer<String> indender = StringUtil.indent(addLine);

        addLine.accept("foo");
        indender.accept("bar");
        indender.accept("baz");
        addLine.accept("doo");

        assertEquals("foo\n" +
                "\tbar\n" +
                "\tbaz\n" +
                "doo\n", out.toString());
    }

    @Test
    void removePrefix() {
        assertEquals("oo", StringUtil.removePrefix("foo", "f"));
        assertEquals("foo", StringUtil.removePrefix("foo", "x"));
        assertEquals("foo", StringUtil.removePrefix("foo", ""));
        assertEquals("", StringUtil.removePrefix("", "bar"));
    }

    @Test
    void unixString() {
        assertEquals("", StringUtil.unixString(""));
        assertEquals("a\nb\n", StringUtil.unixString("a\nb\n"));
        assertEquals("a\nb\n", StringUtil.unixString("a\r\nb\r\n"));
        assertEquals("a\nb\n", StringUtil.unixString("a\n\rb\n\r"));
    }

    @Test
    void sortedUnixLines() {
        assertEquals("a\nb\nc\nd", StringUtil.sortedUnixLines("b\na\r\nd\n\rc\n"));
    }

    @Test
    void slashesToDots() {
        assertEquals(".a.b..cd.", StringUtil.slashesToDots("/a/b//cd/"));
        assertEquals("ab.cd", StringUtil.slashesToDots("ab/cd"));
        assertEquals("abc", StringUtil.slashesToDots("abc"));
        assertEquals("", StringUtil.slashesToDots(""));
    }

    @Test
    void d() {
        assertEquals("foo", StringUtil.prefixBefore("foobar", "bar"));
        assertEquals("foo", StringUtil.prefixBefore("foobarbar", "bar"));
        assertEquals("foobar", StringUtil.prefixBefore("foobar", "baz"));
        assertEquals("", StringUtil.prefixBefore("foobar", "foo"));
    }
}
