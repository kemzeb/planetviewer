package com.kemzeb.planetviewer.search.filter.parser;

import com.kemzeb.planetviewer.exception.SearchFilterParseException;
import com.kemzeb.planetviewer.search.filter.lexer.Lexer;
import com.kemzeb.planetviewer.search.filter.lexer.Token;
import com.kemzeb.planetviewer.search.filter.parser.ParsedFilter.ParsedFilterBuilder;
import java.util.Collection;
import java.util.List;

/**
 * Parses a search endpoint filter. It utilizes the recursive descent approach to parsing to
 * accomplish this. Below represents the grammar rules:
 *
 * <p>filter := {@code <field>} ‘:’ {@code <operator>} {@code <literal>} | {@code <field>} ‘:’
 * {@code <literal>}
 *
 * <p>field ::= {@code <text>}
 *
 * <p>operator ::= < | > | <= | >=
 *
 * <p>literal ::= {@code <decimal>} | {@code <integer>}
 *
 * <p>text ::= {@code <char>} {@code <text>} | {@code <char>}
 */
public class Parser {

  private Lexer lexer;

  /** A primitive array containing 2 recent tokens fetched by the lexer. */
  private Token[] tokenBuffer;

  private int index;

  public Parser(String input) {
    this.lexer = new Lexer(input);
    this.tokenBuffer = new Token[2];

    fillTokenBuffer();
  }

  /** Parses the input provided during instantiation. */
  public ParsedFilter parse() {
    return parseFilter();
  }

  /**
   * BNF grammar is shown below.
   *
   * <p>filter := {@code <field>} ‘:’ {@code <operator>} {@code <literal>} | {@code <field>} ‘:’
   * {@code <literal>} | {@code <field>} ‘:’ {@code <text>}
   */
  private ParsedFilter parseFilter() {
    ParsedFilterBuilder builder = ParsedFilter.builder();

    if (!peekIs(Token.Type.TEXT)) {
      throw new SearchFilterParseException(
          String.format("Expected field to be TEXT, got \"%s\"", peek().type()));
    }

    builder.field(consume().value());

    if (!peekIs(Token.Type.COLON)) {
      throw new SearchFilterParseException(
          String.format("Expected \":\", got \"%s\"", peek().value()));
    }

    skip();

    Operator op = parseOperator();
    builder.operator(op);

    if (!peekIsOneOf(List.of(Token.Type.INTEGER, Token.Type.DECIMAL, Token.Type.STRING))) {
      throw new SearchFilterParseException(
          String.format("Expected an INTEGER, DECIMAL, or STRING, got \"%s\"", peek().type()));
    }

    builder.value(consume().value());

    return builder.build();
  }

  private Operator parseOperator() {
    if (isOperator(peek().type())) {
      return toOperatorEnum(consume().type());
    }

    return Operator.NONE;
  }

  private boolean isOperator(Token.Type type) {
    switch (type) {
      case GREATER_THAN:
      case GREATER_THAN_OR_EQUAL_TO:
      case LESS_THAN:
      case LESS_THAN_OR_EQUAL_TO:
        return true;
      default:
        return false;
    }
  }

  private Operator toOperatorEnum(Token.Type type) {
    switch (type) {
      case GREATER_THAN:
        return Operator.GREATER_THAN;
      case GREATER_THAN_OR_EQUAL_TO:
        return Operator.GREATER_THAN_OR_EQUAL_TO;
      case LESS_THAN:
        return Operator.LESS_THAN;
      case LESS_THAN_OR_EQUAL_TO:
        return Operator.LESS_THAN_OR_EQUAL_TO;
      default:
        return Operator.NONE;
    }
  }

  private Token peek() {
    if (index >= tokenBuffer.length) {
      fillTokenBuffer();
    }

    return tokenBuffer[index];
  }

  private boolean peekIs(Token.Type type) {
    return peek().type() == type;
  }

  private boolean peekIsOneOf(Collection<Token.Type> types) {
    return types.stream().anyMatch(this::peekIs);
  }

  private Token consume() {
    if (index >= tokenBuffer.length) {
      fillTokenBuffer();
    }

    if (peekIs(Token.Type.EOF)) {
      return tokenBuffer[index];
    }

    return tokenBuffer[index++];
  }

  private void skip() {
    index++;
  }

  private void fillTokenBuffer() {
    index = 0;
    tokenBuffer[0] = lexer.nextToken();
    tokenBuffer[1] = lexer.nextToken();
  }
}
