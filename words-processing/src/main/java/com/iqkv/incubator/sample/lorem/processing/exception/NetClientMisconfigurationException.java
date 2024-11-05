package com.iqkv.incubator.sample.lorem.processing.exception;

public class NetClientMisconfigurationException extends IllegalStateException {
  public NetClientMisconfigurationException(Throwable cause) {
    super("Invalid configuration in lorem.net-client properties.", cause);
  }
}
