package com.kemzeb.planetviewer.search.filter.lexer;

import java.util.Optional;

/** Represents a token created by the lexer when parsing a search endpoint filter. */
public record Token(Token.Type type, String value) {

  private static final Token EOF_TOKEN = new Token(Type.EOF, "");

  /** Represent's the token's type. */
  public enum Type {
    LESS_THAN,
    GREATER_THAN,
    LESS_THAN_OR_EQUAL_TO,
    GREATER_THAN_OR_EQUAL_TO,
    COLON,
    /** A set of characters. */
    TEXT,
    /** Just like TEXT, but was delimted by double quotes. */
    STRING,
    INTEGER,
    DECIMAL,
    EOF,
  }

  public static Token eof() {
    return EOF_TOKEN;
  }

  public static Optional<Token> text(CharSequence input) {
    return input.isEmpty()
        ? Optional.empty()
        : Optional.of(new Token(Type.TEXT, String.valueOf(input)));
  }

  public static Optional<Token> operator(CharSequence input) {
    Type type = Type.EOF;
    String inputString = String.valueOf(input);

    switch (inputString) {
      case "<":
        type = Type.LESS_THAN;
        break;
      case ">":
        type = Type.GREATER_THAN;
        break;
      case "<=":
        type = Type.LESS_THAN_OR_EQUAL_TO;
        break;
      case ">=":
        type = Type.GREATER_THAN_OR_EQUAL_TO;
        break;
      case ":":
        // NOTE: We treat a colon as an operator as it' just makes it easier to tokenize.
        type = Type.COLON;
        break;
      default:
        break;
    }

    if (type != Type.EOF) {
      return Optional.of(new Token(type, inputString));
    }

    return Optional.empty();
  }
}
