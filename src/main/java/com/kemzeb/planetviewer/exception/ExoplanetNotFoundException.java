package com.kemzeb.planetviewer.exception;

public class ExoplanetNotFoundException extends RuntimeException {

  public ExoplanetNotFoundException(String name) {
    super("Exoplanet name \"" + name + "\" not found.");
  }
}
