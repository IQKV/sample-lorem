package org.ujar.lorem.processing.exception;

import java.util.List;

public class IllegalEnumParameterException extends IllegalArgumentException {
  public IllegalEnumParameterException(String param, List<String> values) {
    super(String.format("Param %s must be any of %s", param, values));
  }
}
