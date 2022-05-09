package org.ujar.loremipsum.history.web.dto;

import java.util.List;
import lombok.Value;

@Value
public class ErrorResponse {

  List<Error> errors;

  public static ErrorResponse singleError(String message) {
    return new ErrorResponse(List.of(new Error(message)));
  }

  @Value
  public static class Error {
    String message;
  }
}
