package com.kemzeb.planetviewer.exception;

import lombok.Getter;

/**
 * Thrown when dealing with a paginated output that is given a page number that is out of bounds.
 */
@Getter
public class PageNumberOutOfBoundsException extends RuntimeException {

  private String redirectUrl;

  public PageNumberOutOfBoundsException(String redirectUrl) {
    super();

    this.redirectUrl = redirectUrl;
  }
}
