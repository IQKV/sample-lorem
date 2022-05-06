package org.ujar.loremipsum.wordsstatistic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Report {
  @JsonProperty("freq_word")
  private final String freqWord;
  @JsonProperty("avg_paragraph_size")
  private final int avgParagraphSize;
  @JsonProperty("avg_paragraph_processing_time")
  private final double avgParagraphProcessingTime;
  @JsonProperty("total_processing_time")
  private final double totalProcessingTime;
}
