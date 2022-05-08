package org.ujar.loremipsum.processing.exception;

import java.io.IOException;

public class NetClientCommunicationException extends RuntimeException {
  public NetClientCommunicationException(Throwable cause) {
    super(cause);
  }
}
