package org.ujar.loremipsum.shared.web.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import lombok.Value;

@Value
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
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
