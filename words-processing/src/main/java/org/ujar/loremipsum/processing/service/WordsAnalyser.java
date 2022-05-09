package org.ujar.loremipsum.processing.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ujar.loremipsum.processing.model.Report;

@Service
@Slf4j
public class WordsAnalyser {

  public Report analyze(String rawSource) {
    final Report report = new Report();
    final ArrayList<Long> pTimes = new ArrayList<>();
    var totalProcessingTime = measureTime(() -> {
      var paragraphs = getParagraphs(rawSource);
      for (String paragraph : paragraphs) {
        var pTime = measureTime(() -> {

        });
        pTimes.add(pTime);
      }
    });
    report.setTotalProcessingTime(totalProcessingTime);

    return new Report("Lorem", (short) 1,1,  1);
  }

  private List<String> getParagraphs(String rawSource) {
    rawSource = rawSource.replaceAll("[\\r\\n]+", "")
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
