package com.kemzeb.planetviewer.search.filter.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kemzeb.planetviewer.exception.SearchFilterParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ParserUnitTest {

  @Test
  public void givenNormalScenario_whenParsing_thenExpectValid() {
    // Given
    String input = "field:<29";
    Parser parser = new Parser(input);

    // When
    ParsedFilter result = parser.parse();

    // Then
    assertEquals("field", result.field());
    assertEquals(Operator.LESS_THAN, result.operator());
    assertEquals("29", result.value());
  }

  @Test
  public void givenNoOperator_whenParsing_thenExpectValid() {
    // Given
    String input = "field : 29.22";
    Parser parser = new Parser(input);

    // When
    ParsedFilter result = parser.parse();

    // Then
    assertEquals("field", result.field());
    assertEquals(Operator.NONE, result.operator());
    assertEquals("29.22", result.value());
  }

  @Test
  public void givenFieldAndText_whenParsing_thenExpectValid() {
    // Given
    String input = "field : \"What is a    Lombax?\"";
    Parser parser = new Parser(input);

    // When
    ParsedFilter result = parser.parse();

    // Then
    assertEquals("field", result.field());
    assertEquals(Operator.NONE, result.operator());
    assertEquals("What is a    Lombax?", result.value());
  }

  @ParameterizedTest
  @ValueSource(strings = {": 64", "field 64", "field : <=", ""})
  public void givenSyntaxErrors_whenParsing_thenExpectValid(String input) {
    // Given
    Parser parser = new Parser(input);

    // When
    // Then
    assertThrows(SearchFilterParseException.class, () -> parser.parse());
  }
}
