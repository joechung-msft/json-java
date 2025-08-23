package com.github.jsonjava;

import java.util.regex.Pattern;

public class PairParser {
  public enum Mode {
    Scanning, Key, Colon, Value, End
  };

  public static PairToken parse(String pair) {
    Mode mode = Mode.Scanning;
    int pos = 0;
    StringToken key = null;
    Token value = null;

    while (pos < pair.length()) {
      char ch = pair.charAt(pos);
      String slice;

      switch (mode) {
        case Scanning:
          if (Pattern.matches("[ \\n\\r\\t]", Character.toString(ch))) {
            pos++;
          } else {
            mode = Mode.Key;
          }
          break;

        case Key:
          slice = pair.substring(pos);
          key = StringParser.parse(slice);
          pos += key.skip;
          mode = Mode.Colon;
          break;

        case Colon:
          if (Pattern.matches("[ \\n\\r\\t]", Character.toString(ch))) {
            pos++;
          } else if (ch == ':') {
            pos++;
            mode = Mode.Value;
          } else {
            throw new RuntimeException(String.format("Expected ':', actual '%s'", ch));
          }
          break;

        case Value:
          slice = pair.substring(pos);
          value = ValueParser.parse(slice, "[ \\n\\r\\t\\},]");
          pos += value.skip;
          mode = Mode.End;
          break;

        case End:
          return new PairToken(pos, key, value);

        default:
          throw new RuntimeException(String.format("Unexpected mode %s", mode));
      }
    }

    throw new RuntimeException("Incomplete pair expression");
  }
}
