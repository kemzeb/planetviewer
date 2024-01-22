package com.kemzeb.starviewer.exception;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({StarNotFoundException.class, ExoplanetNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String handleNotFoundException(Exception ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(PageNumberOutOfBoundsException.class)
  ResponseEntity<Void> handlePageNotFoundException(PageNumberOutOfBoundsException ex) {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(ex.getRedirectUrl()));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }
}
