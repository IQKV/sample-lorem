package org.ujar.loremipsum.wordsstatistic.service;

import org.springframework.stereotype.Service;
import org.ujar.loremipsum.wordsstatistic.enums.LengthType;
import org.ujar.loremipsum.wordsstatistic.model.Report;

@Service
public class WordsAnalyser {
  public Report analyse(Integer paragraphsNum, LengthType lengthType) {
    return new Report("Lorem", 1,1,1);
  }
}
