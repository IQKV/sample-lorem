package org.ujar.loremipsum.processing.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ujar.loremipsum.processing.model.Report;

@Service
@Slf4j
public class WordsAnalyser {

  /**
   * Compute words frequency, avg paragraph size, measure execution time & generate report
   */
  public Report analyze(String rawSource) {
    final Report report = new Report();
    final ArrayList<Long> pElapsedTimes = new ArrayList<>();

    final Map<String, Integer> wordsFrequency = new HashMap<>();
    var totalProcessingTime = measureTime(() -> {
      var paragraphs = getParagraphs(rawSource);

      final int[] paragraphSizes = new int[paragraphs.size()];
      AtomicInteger paragraphIndex = new AtomicInteger();

      for (String paragraph : paragraphs) {
        var elapsedTime = measureTime(() -> {
          var words = computeParagraphWordsUsage(paragraph, wordsFrequency);
          paragraphSizes[paragraphIndex.getAndIncrement()] = words.length;
        });
        pElapsedTimes.add(elapsedTime);
      }

      report.setMostFrequentWord(findMostFrequentWord(wordsFrequency));
      report.setAvgParagraphSize((short) Arrays.stream(paragraphSizes).average().orElse(0.0));

      var avgParagraphProcessingTime = (long) pElapsedTimes.stream().mapToInt(Long::intValue).average().orElse(0.0);
      report.setAvgParagraphProcessingTime(avgParagraphProcessingTime);
    });

    report.setTotalProcessingTime(totalProcessingTime);
    return report;
  }

  private String findMostFrequentWord(Map<String, Integer> wordsFrequency) {
    String mostFrequentWord = null;
    var maxQty = 0;
    for(final String word : wordsFrequency.keySet()) {
      if(wordsFrequency.get(word) > maxQty) {
        maxQty = wordsFrequency.get(word);
        mostFrequentWord = word;
      }
    }
    return mostFrequentWord;
  }

  private String[] computeParagraphWordsUsage(String paragraph, Map<String, Integer> wordsUsage) {
    String[] words = paragraph.split("\\s+");
    for (final String word : words) {
      Integer qty = wordsUsage.get(word.toLowerCase());
      if (qty == null) {
        qty = 1;
      } else {
        qty = qty + 1;
      }
      wordsUsage.put(word.toLowerCase(), qty);
    }
    return words;
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
    var startTime = System.nanoTime();
    task.run();
    return System.nanoTime() - startTime;
  }
}
