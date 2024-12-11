package com.iqkv.incubator.sample.mixlorem.history.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReportDto(@JsonProperty("freq_word") String mostFrequentWord,
                        @JsonProperty("avg_paragraph_size") int avgParagraphSize,
                        @JsonProperty("avg_paragraph_processing_time") long avgParagraphProcessingTime,
                        @JsonProperty("total_processing_time") long totalProcessingTime) {
}
