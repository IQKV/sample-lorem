package org.ujar.loremipsum.history.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReportDto(@JsonProperty("freq_word") String freqWord,
                        @JsonProperty("avg_paragraph_size") int avgParagraphSize,
                        @JsonProperty("avg_paragraph_processing_time") int avgParagraphProcessingTime,
                        @JsonProperty("total_processing_time") int totalProcessingTime) {
}
