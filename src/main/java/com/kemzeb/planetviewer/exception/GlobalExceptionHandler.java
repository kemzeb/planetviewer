package com.kemzeb.planetviewer.exception;

import jakarta.validation.ValidationException;
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
  String handleEntityNotFoundException(Exception ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(PageNumberOutOfBoundsException.class)
  ResponseEntity<Void> handlePageNotFoundException(PageNumberOutOfBoundsException ex) {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(ex.getRedirectUrl()));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String handleBadRequest(ValidationException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String handleServerError(RuntimeException ex) {
    return ex.getMessage();
  }
}
