package org.ujar.loremipsum.reportshistory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Report(@JsonProperty("freq_word") String freqWord,
                     @JsonProperty("avg_paragraph_size") int avgParagraphSize,
                     @JsonProperty("avg_paragraph_processing_time") double avgParagraphProcessingTime,
                     @JsonProperty("total_processing_time") double totalProcessingTime) {
}
