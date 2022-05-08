package org.ujar.loremipsum.processing.service;

import org.springframework.stereotype.Service;
import org.ujar.loremipsum.processing.model.Report;

@Service
public class WordsAnalyser {

  public Report analyze(String rawSource) {
    return new Report("Lorem", (short) 1, (short) 1, (short) 1);
  }
}
