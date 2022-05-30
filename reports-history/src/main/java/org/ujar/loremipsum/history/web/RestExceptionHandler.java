package org.ujar.loremipsum.history.web;

import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.ujar.loremipsum.shared.web.dto.Error;
import org.ujar.loremipsum.shared.web.dto.ErrorResponse;

@Slf4j
@RestControllerAdvice
@ConditionalOnMissingBean(RestControllerAdvice.class)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @NonNull
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request
  ) {
    var errorResponse = new ErrorResponse(List.of(new Error(ex.getMessage())));
    return new ResponseEntity<>(errorResponse, headers, status);
  }
}
