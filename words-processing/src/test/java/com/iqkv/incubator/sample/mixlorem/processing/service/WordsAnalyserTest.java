/*
 * Copyright 2025 IQKV Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.incubator.sample.mixlorem.processing.service;

import static com.iqkv.incubator.sample.mixlorem.processing.util.TestUtils.getFileAsString;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import com.iqkv.incubator.sample.mixlorem.processing.model.Report;
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
