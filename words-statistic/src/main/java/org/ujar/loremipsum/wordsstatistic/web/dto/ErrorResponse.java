package org.ujar.loremipsum.wordsstatistic.web.dto;

import java.util.List;
import lombok.Value;

@Value
public class ErrorResponse {

  List<Error> errors;

  @Value
  public static class Error {
    String message;
  }

  public static ErrorResponse singleError(String message) {
    return new ErrorResponse(List.of(new Error(message)));
  }
}
