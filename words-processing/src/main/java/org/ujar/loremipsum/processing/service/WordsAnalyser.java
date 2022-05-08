package org.ujar.loremipsum.processing.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ujar.loremipsum.processing.model.Report;

@Service
@Slf4j
public class WordsAnalyser {

  public Report analyze(String rawSource) {
    log.info("Source: " + rawSource);
    return new Report("Lorem", (short) 1, (short) 1, (short) 1);
  }
}
