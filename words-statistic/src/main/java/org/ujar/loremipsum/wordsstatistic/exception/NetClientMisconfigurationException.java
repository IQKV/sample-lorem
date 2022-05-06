package org.ujar.loremipsum.wordsstatistic.exception;

public class NetClientMisconfigurationException extends IllegalStateException {
  public NetClientMisconfigurationException(Throwable cause) {
    super("Invalid configuration in loremipsum.net-client properties.", cause);
  }
}
