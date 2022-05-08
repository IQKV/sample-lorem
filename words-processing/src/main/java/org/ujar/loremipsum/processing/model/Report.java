package org.ujar.loremipsum.processing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Report(@JsonProperty("freq_word") String freqWord,
                     @JsonProperty("avg_paragraph_size") short avgParagraphSize,
                     @JsonProperty("avg_paragraph_processing_time") short avgParagraphProcessingTime,
                     @JsonProperty("total_processing_time") short totalProcessingTime) {
}
