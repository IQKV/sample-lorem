package com.iqkv.incubator.sample.lorem.processing.service;

import static com.iqkv.incubator.sample.lorem.processing.util.TestUtils.getFileAsString;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import com.iqkv.incubator.sample.lorem.processing.model.Report;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class WordsAnalyserTest {
  private WordsAnalyser service;

  private static Stream<Arguments> provideStringsForAnalysis() {
    return Stream.of(
        Arguments.of("1_short", fakeReport("vacuitate", 27)),
        Arguments.of("2_short", fakeReport("de", 31)),
        Arguments.of("2_medium", fakeReport("est", 45)),
        Arguments.of("3_verylong", fakeReport("ut", 223)),
        Arguments.of("10_verylong", fakeReport("et", 215))
    );
  }

  private static Report fakeReport(String frequentWord, int avgParagraphSize) {
    return new Report(frequentWord, (short) avgParagraphSize, 0, 0);
  }

  @BeforeEach
  void setUp() {
    service = new WordsAnalyser();
  }

  @ParameterizedTest
  @MethodSource("provideStringsForAnalysis")
  void shouldAnalyzeAndGenerateReport(String fileName, Report expected) {
    final var input = getFileAsString("response/" + fileName + ".html");
    final var result = service.analyze(input);
    assertThat(result.getMostFrequentWord()).isEqualTo(expected.getMostFrequentWord());
    assertThat(result.getAvgParagraphSize()).isEqualTo(expected.getAvgParagraphSize());
  }
}
