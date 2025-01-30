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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.iqkv.incubator.sample.mixlorem.processing.model.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WordsAnalyser {

  /**
   * Compute words frequency, avg paragraph size, measure execution time & generate report
   */
  public Report analyze(String rawSource) {
    final Report report = new Report();
    final ArrayList<Long> pElapsedTimes = new ArrayList<>();

    final Map<String, Integer> allWordsFrequency = new HashMap<>();
    final var totalProcessingTime = measureTime(() -> {
      final var paragraphs = getParagraphs(rawSource);

      final int[] paragraphSizes = new int[paragraphs.size()];
      AtomicInteger paragraphIndex = new AtomicInteger();

      for (String paragraph : paragraphs) {
        final var elapsedTime = measureTime(() -> {
          String[] words = paragraph.split("\\s+");

          final var paragraphWords = computeParagraphWordsUsage(words);

          paragraphWords.forEach((word, wordCount) -> allWordsFrequency
              .merge(word, wordCount.intValue(), Integer::sum));

          paragraphSizes[paragraphIndex.getAndIncrement()] = words.length;
        });
        pElapsedTimes.add(elapsedTime);
      }

      report.setMostFrequentWord(findMostFrequentWord(allWordsFrequency));
      report.setAvgParagraphSize((short) Arrays.stream(paragraphSizes).average().orElse(0.0));

      final var avgParagraphProcessingTime = (long) pElapsedTimes.stream()
          .mapToInt(Long::intValue).average().orElse(0.0);
      report.setAvgParagraphProcessingTime(avgParagraphProcessingTime);
    });

    report.setTotalProcessingTime(totalProcessingTime);
    return report;
  }

  private String findMostFrequentWord(Map<String, Integer> wordsFrequency) {
    return wordsFrequency.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue()
        ).orElseThrow().getKey();
  }

  private Map<String, Long> computeParagraphWordsUsage(String[] words) {
    return Arrays.stream(words).map(String::toLowerCase)
        .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()));
  }

  private List<String> getParagraphs(String rawSource) {
    rawSource = rawSource.replaceAll("[\\r\\n\\.\\,!\\?;:]+", "")
        .replaceAll("\\<\\/.\\>", "");
    return Arrays.stream(rawSource.split("(\\<p\\>)"))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .toList();
  }

  private long measureTime(Runnable task) {
    final var startTime = System.nanoTime();
    task.run();
    return System.nanoTime() - startTime;
  }
}
