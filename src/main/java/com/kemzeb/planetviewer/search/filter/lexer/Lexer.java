package com.kemzeb.planetviewer.search.filter.lexer;

import com.kemzeb.planetviewer.search.filter.lexer.Token.Type;
import java.util.Optional;

/**
 * Tokenizes a search endpoint filter. It does this by implementing a finite state machine.
 *
 * <p>The following is the subset of the BNF grammar rules that shows the available tokens.
 *
 * <p>operator ::= < | > | <= | >=
 *
 * <p>text ::= {@code <char>} {@code <text>} | {@code <char>}
 *
 * <p>number ::= {@code <decimal>} | {@code <integer>}
 */
public class Lexer {

  private record TransitionResult(Optional<Token> token, State nextState) {}

  private enum State {
    START,
    OPERATOR,
    NUMBER,
    INTEGER,
    DECIMAL,
    STRING,
    END,
    NONE
  }

  private State nextState;
  private StringBuilder stateBuffer;

  private String input;
  private int index;

  public Lexer(String input) {
    this.input = input;
    this.index = 0;
    this.nextState = State.START;
    this.stateBuffer = new StringBuilder();
  }

  /**
   * Grabs the next token. Returns an EOF token if the lexer has exhausted reading from the input
   * string.
   */
  public Token nextToken() {
    while (nextState != State.NONE) {
      TransitionResult result = transition(nextState);
      nextState = result.nextState();

      if (result.token.isPresent()) {
        return result.token.get();
      }
    }

    return Token.eof();
  }

  /** Perform state transition given {@link State} to transition to. */
  private TransitionResult transition(State state) {
    switch (state) {
      case START:
        return transitionToStart();
      case OPERATOR:
        return transitionToOperator();
      case DECIMAL:
        return transitionToDecimal();
      case INTEGER:
        return transitionToInteger();
      case STRING:
        return transitionToString();
      case END:
        return transitionToEnd();
      default:
        return new TransitionResult(Optional.empty(), State.NONE);
    }
  }

  /** Transition to the START state. */
  private TransitionResult transitionToStart() {
    if (isEof()) {
      Optional<Token> maybeToken = Token.text(stateBuffer);

      resetStateBuffer();

      return new TransitionResult(maybeToken, State.END);
    }

    if (isStartOfOperator(peek())) {
      // If there is anything in the buffer at this point, let's return it as TEXT.
      Optional<Token> maybeToken = Token.text(stateBuffer);

      resetStateBuffer();

      stateBuffer.append(consume());

      return new TransitionResult(maybeToken, State.OPERATOR);
    }

    if (Character.isWhitespace(peek())) {
      Optional<Token> maybeToken = Token.text(stateBuffer);

      resetStateBuffer();
      skip();

      return new TransitionResult(maybeToken, State.START);
    }

    // If it stars with a digit, or negative sign character, let's assume an integer first over a
    // decimal.
    if ((Character.isDigit(peek()) || peekIs('-')) && stateBuffer.isEmpty()) {
      stateBuffer.append(consume());

      return new TransitionResult(Optional.empty(), State.INTEGER);
    }

    // Are we dealing with a decimal e.g. .491 and the buffer doesn't hold anything (to avoid e.g.
    // "text.491")?
    if (peekIs('.') && stateBuffer.isEmpty()) {
      stateBuffer.append(consume());

      return new TransitionResult(Optional.empty(), State.DECIMAL);
    }

    if (peekIs('"')) {
      skip();

      return new TransitionResult(Optional.empty(), State.STRING);
    }

    // If none of the other cases are met, let's just treat this as text.
    stateBuffer.append(consume());

    return new TransitionResult(Optional.empty(), State.START);
  }

  /** Transition to the OPERATOR state. */
  private TransitionResult transitionToOperator() {
    if (isEof()) {
      Optional<Token> maybeToken = Token.operator(stateBuffer);
      if (maybeToken.isEmpty()) {
        // We may have been given a char(s) that starts as an operator but at EOF doesn't represent
        // one. Let's just return to START.
        return new TransitionResult(Optional.empty(), State.START);
      }

      resetStateBuffer();
      return new TransitionResult(maybeToken, State.END);
    }

    if (isStartOfOperator(stateBuffer, peek())) {
      stateBuffer.append(consume());

      return new TransitionResult(Optional.empty(), State.OPERATOR);
    }

    // peek() has not given us a part of an operator. Let's check if the buffer holds a
    // valid operator.
    Optional<Token> maybeToken = Token.operator(stateBuffer);
    if (maybeToken.isPresent()) {
      // This is an operator token. Since we already created the token, let's clear the buffer.
      resetStateBuffer();
    }

    // This is an operator token or it is not. Either way, we should transition to START.
    return new TransitionResult(maybeToken, State.START);
  }

  /** Transition to the INTEGER state. */
  private TransitionResult transitionToInteger() {
    if (Character.isDigit(peek())) {
      stateBuffer.append(consume());

      return new TransitionResult(Optional.empty(), State.INTEGER);
    }

    if (peekIs('.')) {
      // We're dealing with a decimal, so let's transition.
      stateBuffer.append(consume());

      return new TransitionResult(Optional.empty(), State.DECIMAL);
    }

    Token token = new Token(Type.INTEGER, stateBuffer.toString());

    resetStateBuffer();

    return new TransitionResult(Optional.of(token), State.START);
  }

  /**
   * Transition to the DECIMAL state. It assumes the caller has alredy consumed a decimal period.
   */
  private TransitionResult transitionToDecimal() {
    if (Character.isDigit(peek())) {
      stateBuffer.append(consume());

      return new TransitionResult(Optional.empty(), State.DECIMAL);
    }

    Token token = new Token(Type.DECIMAL, stateBuffer.toString());

    resetStateBuffer();

    return new TransitionResult(Optional.of(token), State.START);
  }

  /** Transition to the STRING state. */
  private TransitionResult transitionToString() {
    // FIXME: Handle EOF.

    if (peekIs('"')) {
      Token token = new Token(Type.STRING, stateBuffer.toString());

      resetStateBuffer();
      skip();

      return new TransitionResult(Optional.of(token), State.START);
    }

    stateBuffer.append(consume());
    return new TransitionResult(Optional.empty(), State.STRING);
  }

  /** Transition to the END state. */
  private TransitionResult transitionToEnd() {
    return new TransitionResult(Optional.of(Token.eof()), State.NONE);
  }

  private void resetStateBuffer() {
    stateBuffer.delete(0, stateBuffer.length());
  }

  private boolean isStartOfOperator(char ch) {
    return isStartOfOperator("", ch);
  }

  private boolean isStartOfOperator(CharSequence subSequence, char peekChar) {
    StringBuilder builder = new StringBuilder(subSequence);
    builder.append(peekChar);

    switch (builder.toString()) {
      case "<":
      case ">":
      case "<=":
      case ">=":
      case ":":
        return true;
      default:
        return false;
    }
  }

  private char peek() {
    return isEof() ? '\0' : input.charAt(index);
  }

  private boolean peekIs(char ch) {
    return peek() == ch;
  }

  private char consume() {
    if (isEof()) {
      return '\0';
    }

    return input.charAt(index++);
  }

  private void skip() {
    index++;
  }

  private boolean isEof() {
    return index >= input.length();
  }
}
