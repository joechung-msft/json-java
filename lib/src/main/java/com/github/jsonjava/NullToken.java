package com.github.jsonjava;

/**
 * Represents a parsed JSON null token.
 */
public class NullToken extends Token {
  private final Object value = null;

  /**
   * Constructs a NullToken with the given skip value.
   *
   * @param skip Number of characters to skip after parsing.
   */
  public NullToken(int skip) {
    super(skip);
  }

  /**
   * Returns the value of this token.
   *
   * @return null
   */
  public Object getValue() {
    return value;
  }

  /**
   * Returns the string representation of the JSON null token.
   *
   * @return "null"
   */
  @Override
  public String toString() {
    return "null";
  }
}
