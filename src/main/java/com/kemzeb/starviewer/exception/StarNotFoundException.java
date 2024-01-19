package com.kemzeb.starviewer.exception;

public class StarNotFoundException extends RuntimeException {

  public StarNotFoundException(String name) {
    super("Stellar name \"" + name + "\" not found.");
  }
}
