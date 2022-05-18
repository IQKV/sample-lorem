package org.ujar.loremipsum.shared.util;

import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;

public class TestUtils {

  @SneakyThrows
  public static String getFileAsString(String fileName) {
    return Files.readString(Path.of(
        Thread.currentThread().getContextClassLoader().getResource(fileName).toURI()));
  }
}
