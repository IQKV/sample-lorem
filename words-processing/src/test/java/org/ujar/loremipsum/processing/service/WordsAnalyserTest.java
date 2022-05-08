package org.ujar.loremipsum.processing.service;

import static org.ujar.loremipsum.processing.TestUtils.getFileAsString;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.ujar.loremipsum.processing.model.Report;

class WordsAnalyserTest {
  private WordsAnalyser service;

  @BeforeEach
  void setUp() {
   service = new WordsAnalyser();
  }

  @ParameterizedTest
  @MethodSource("provideStringsForAnalyze")
  void analyze(String input, Report expected) {
    assertThat(service.analyze(input)).usingRecursiveComparison().isEqualTo(expected);
  }

  private static Stream<Arguments> provideStringsForAnalyze() {
    return Stream.of(
        Arguments.of(
            getFileAsString("response/1_short.html"),
            new Report("Lorem", (short) 1, (short) 1, (short) 1))
    );
  }
}
