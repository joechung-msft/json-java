package com.github.jsonjava;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Unit tests for StringParser and JSON string parsing. */
class StringParserTest {

  /** Tests parsing an empty string. */
  @Test
  void testEmptyString() {
    Token token = JSONParser.parse("\"\"");
    assertTrue(token instanceof StringToken);
    assertEquals(2, token.skip);
    assertEquals("", ((StringToken) token).value);
  }

  /** Tests parsing a normal string. */
  @Test
  void testNormalString() {
    Token token = JSONParser.parse("\"hello\"");
    assertTrue(token instanceof StringToken);
    assertEquals(7, token.skip);
    assertEquals("hello", ((StringToken) token).value);
  }

  /** Tests parsing a string with an escaped quote. */
  @Test
  void testStringWithEscapedQuote() {
    Token token = JSONParser.parse("\"he\\\"llo\"");
    assertTrue(token instanceof StringToken);
    assertEquals(9, token.skip);
    assertEquals("he\"llo", ((StringToken) token).value);
  }

  /** Tests parsing a string with an escaped backslash. */
  @Test
  void testStringWithEscapedBackslash() {
    Token token = JSONParser.parse("\"a\\\\b\"");
    assertTrue(token instanceof StringToken);
    assertEquals(6, token.skip);
    assertEquals("a\\b", ((StringToken) token).value);
  }

  /** Tests parsing a string with an escaped slash. */
  @Test
  void testStringWithEscapedSlash() {
    Token token = JSONParser.parse("\"a\\/b\"");
    assertTrue(token instanceof StringToken);
    assertEquals(6, token.skip);
    assertEquals("a/b", ((StringToken) token).value);
  }

  /** Tests parsing a string with escaped control characters. */
  @Test
  void testStringWithEscapedControlChars() {
    Token token = JSONParser.parse("\"a\\nb\\tb\"");
    assertTrue(token instanceof StringToken);
    assertEquals(9, token.skip);
    assertEquals("a\nb\tb", ((StringToken) token).value);
  }

  /** Tests parsing a string with Unicode escape sequences. */
  @Test
  void testStringWithUnicode() {
    StringToken token = StringParser.parse("\"\\u0041\"");
    assertEquals(8, token.skip);
    assertEquals("\\u0041", token.value);
  }

  /** Tests parsing a string with surrounding whitespace. */
  @Test
  void testStringWithWhitespace() {
    Token token = JSONParser.parse("  \"abc\"  ");
    assertTrue(token instanceof StringToken);
    assertEquals(7, token.skip);
    assertEquals("abc", ((StringToken) token).value);
  }

  /** Tests parsing a malformed string missing a closing quote. */
  @Test
  void testMalformedStringMissingQuote() {
    Exception ex = assertThrows(RuntimeException.class, () -> JSONParser.parse("\"abc"));
    assertTrue(ex.getMessage().contains("Incomplete string expression"));
  }

  /** Tests parsing a malformed string with an unexpected character. */
  @Test
  void testMalformedStringUnexpectedChar() {
    Exception ex = assertThrows(RuntimeException.class, () -> JSONParser.parse("\"ab\nc\""));
    assertTrue(ex.getMessage().contains("Unexpected character"));
  }

  /** Tests parsing a malformed string with a bad escape sequence. */
  @Test
  void testMalformedStringBadEscape() {
    Exception ex = assertThrows(RuntimeException.class, () -> JSONParser.parse("\"a\\x\""));
    assertTrue(ex.getMessage().contains("Unexpected escape character"));
  }

  /** Tests parsing a malformed string with a bad Unicode escape sequence. */
  @Test
  void testMalformedStringBadUnicode() {
    Exception ex = assertThrows(RuntimeException.class, () -> JSONParser.parse("\"\\u00G1\""));
    assertTrue(ex.getMessage().contains("Unexpected Unicode code"));
  }

  /** Tests parsing a string with a backspace escape. */
  @Test
  void testStringWithBackspaceEscape() {
    Token token = JSONParser.parse("\"a\\bb\"");
    assertTrue(token instanceof StringToken);
    assertEquals(6, token.skip);
    assertEquals("a\bb", ((StringToken) token).value);
  }

  /** Tests parsing a string with a form feed escape. */
  @Test
  void testStringWithFormFeedEscape() {
    Token token = JSONParser.parse("\"a\\fb\"");
    assertTrue(token instanceof StringToken);
    assertEquals(6, token.skip);
    assertEquals("a\fb", ((StringToken) token).value);
  }

  /** Tests parsing a string with a carriage return escape. */
  @Test
  void testStringWithCarriageReturnEscape() {
    Token token = JSONParser.parse("\"a\\rb\"");
    assertTrue(token instanceof StringToken);
    assertEquals(6, token.skip);
    assertEquals("a\rb", ((StringToken) token).value);
  }

  /** Tests parsing a string with null Unicode escape. */
  @Test
  void testStringWithNullUnicode() {
    StringToken token = StringParser.parse("\"\\u0000\"");
    assertEquals(8, token.skip);
    assertEquals("\\u0000", token.value);
  }

  /** Tests parsing a string with maximum Unicode escape. */
  @Test
  void testStringWithMaxUnicode() {
    StringToken token = StringParser.parse("\"\\uFFFF\"");
    assertEquals(8, token.skip);
    assertEquals("\\uFFFF", token.value);
  }

  /** Tests parsing a string with multiple different escapes. */
  @Test
  void testStringWithMultipleEscapes() {
    Token token = JSONParser.parse("\"a\\b\\n\\u0041\\t\"");
    assertTrue(token instanceof StringToken);
    assertEquals(15, token.skip);
    assertEquals("a\b\n\\u0041\t", ((StringToken) token).value);
  }

  /** Tests parsing a string with multiple Unicode escapes. */
  @Test
  void testStringWithMultipleUnicode() {
    StringToken token = StringParser.parse("\"\\u0041\\u0042\"");
    assertEquals(14, token.skip);
    assertEquals("\\u0041\\u0042", token.value);
  }

  /** Tests parsing a malformed string with incomplete Unicode. */
  @Test
  void testMalformedStringIncompleteUnicode() {
    assertThrows(RuntimeException.class, () -> JSONParser.parse("\"\\u00\""));
  }

  /** Tests parsing a malformed string with short Unicode. */
  @Test
  void testMalformedStringShortUnicode() {
    assertThrows(RuntimeException.class, () -> JSONParser.parse("\"\\u\""));
  }
}
