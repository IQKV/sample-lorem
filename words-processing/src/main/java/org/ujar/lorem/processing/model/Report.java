package org.ujar.lorem.processing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
  @JsonProperty("freq_word")
  String mostFrequentWord;
  @JsonProperty("avg_paragraph_size")
  short avgParagraphSize = 0;
  @JsonProperty("avg_paragraph_processing_time")
  long avgParagraphProcessingTime = 0;
  @JsonProperty("total_processing_time")
  long totalProcessingTime = 0;
}
