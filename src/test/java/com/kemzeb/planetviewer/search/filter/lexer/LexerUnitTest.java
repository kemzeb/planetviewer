package com.kemzeb.planetviewer.search.filter.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kemzeb.planetviewer.search.filter.lexer.Token.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class LexerUnitTest {

  @Test
  public void givenOnlyText_whenCallingNextToken_thenReturnTextToken() {
    // Given
    Lexer lexer = new Lexer("field");

    // When
    Token token = lexer.nextToken();

    // Then
    assertEquals(Type.TEXT, token.type());
    assertEquals("field", token.value());
  }

  @Test
  public void givenString_whenCallingNextToken_thenReturnStringToken() {
    // Given
    Lexer lexer = new Lexer("\"this is a string\"");

    // When
    Token token = lexer.nextToken();

    // Then
    assertEquals(Type.STRING, token.type());
    assertEquals("this is a string", token.value());
  }

  @ParameterizedTest
  @MethodSource("validOperatorsProvider")
  public void givenValidOperators_whenCallingNextToken_thenExpectTheyAreValid(
      String operator, Type expectedType) {

    // Given
    Lexer lexer = new Lexer(operator);

    // When
    Token token = lexer.nextToken();

    // Then
    assertEquals(expectedType, token.type());
    assertEquals(operator, token.value());
  }

  static Stream<Object> validOperatorsProvider() {
    return Stream.of(
        new Object[] {"<", Type.LESS_THAN},
        new Object[] {">", Type.GREATER_THAN},
        new Object[] {"<=", Type.LESS_THAN_OR_EQUAL_TO},
        new Object[] {">=", Type.GREATER_THAN_OR_EQUAL_TO});
  }

  @Test
  public void givenWhitespaceAfterText_whenCallingNextToken_thenReturnTextToken() {
    // Given
    Lexer lexer = new Lexer("field      ");

    // When
    Token token = lexer.nextToken();

    // Then
    assertEquals(Type.TEXT, token.type());
    assertEquals("field", token.value());
  }

  @Test
  public void givenEmptyInput_whenCallingNextToken_thenReturnEof() {
    // Given
    Lexer lexer = new Lexer("");

    // When
    Token token = lexer.nextToken();

    // Then
    assertEquals(Type.EOF, token.type());
  }

  @ParameterizedTest
  @ValueSource(strings = {"field:>=92", "field : >=   92   "})
  public void givenNormalScenarios_whenCallingNextToken_thenExpectValid(String input) {
    // Given
    Lexer lexer = new Lexer(input);

    // When
    List<Token> tokens = new ArrayList<>();
    Token token = lexer.nextToken();

    while (token.type() != Type.EOF) {
      tokens.add(token);

      token = lexer.nextToken();
    }

    // Then
    assertEquals(4, tokens.size());
    assertEquals(Type.TEXT, tokens.get(0).type());
    assertEquals("field", tokens.get(0).value());
    assertEquals(Type.COLON, tokens.get(1).type());
    assertEquals(":", tokens.get(1).value());
    assertEquals(Type.GREATER_THAN_OR_EQUAL_TO, tokens.get(2).type());
    assertEquals(">=", tokens.get(2).value());
    assertEquals(Type.INTEGER, tokens.get(3).type());
    assertEquals("92", tokens.get(3).value());
  }

  @Test
  public void givenNormalScenarioWithoutOperator_whenCallingNextToken_thenExpectNormal() {
    // Given
    Lexer lexer = new Lexer("field:2231.001");

    // When
    List<Token> tokens = new ArrayList<>();
    Token token = lexer.nextToken();

    while (token.type() != Type.EOF) {
      tokens.add(token);

      token = lexer.nextToken();
    }

    // Then
    assertEquals(3, tokens.size());
    assertEquals(Type.TEXT, tokens.get(0).type());
    assertEquals("field", tokens.get(0).value());
    assertEquals(Type.COLON, tokens.get(1).type());
    assertEquals(":", tokens.get(1).value());
    assertEquals(Type.DECIMAL, tokens.get(2).type());
    assertEquals("2231.001", tokens.get(2).value());
  }

  @Test
  public void givenNegativeInteger_whenCallingNextToken_thenExpectIntegerToken() {
    // Given
    Lexer lexer = new Lexer("-8");

    // When
    Token token = lexer.nextToken();

    // Then
    assertEquals(Type.INTEGER, token.type());
    assertEquals("-8", token.value());
  }

  @Test
  public void givenNegativeDecimal_whenCallingNextToken_thenExpectIntegerToken() {
    // Given
    Lexer lexer = new Lexer("-8.21");

    // When
    Token token = lexer.nextToken();

    // Then
    assertEquals(Type.DECIMAL, token.type());
    assertEquals("-8.21", token.value());
  }

  @ParameterizedTest
  @ValueSource(strings = {".2", "-.21"})
  public void givenDecimalStartingWithPeriod_whenCallingNextToken_thenExpectIntegerToken(
      String decimal) {
    // Given
    Lexer lexer = new Lexer(decimal);

    // When
    Token token = lexer.nextToken();

    // Then
    assertEquals(Type.DECIMAL, token.type());
    assertEquals(decimal, token.value());
  }

  @Test
  public void givenNumberAndTextTogether_whenCallingNextToken_thenExpectTwoTokens() {
    // Given
    Lexer lexer = new Lexer("-891abc");

    // When
    Token numberToken = lexer.nextToken();
    Token textToken = lexer.nextToken();

    // Then
    assertEquals(Type.INTEGER, numberToken.type());
    assertEquals(Type.TEXT, textToken.type());
  }
}
