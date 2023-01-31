package org.ujar.bs.dst.loremipsum.processing.exception;

public class NetClientMisconfigurationException extends IllegalStateException {
  public NetClientMisconfigurationException(Throwable cause) {
    super("Invalid configuration in loripsum.net-client properties.", cause);
  }
}
