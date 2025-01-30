/*
 * Copyright 2025 IQKV Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.incubator.sample.mixlorem.history.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReportDto(@JsonProperty("freq_word") String mostFrequentWord,
                        @JsonProperty("avg_paragraph_size") int avgParagraphSize,
                        @JsonProperty("avg_paragraph_processing_time") long avgParagraphProcessingTime,
                        @JsonProperty("total_processing_time") long totalProcessingTime) {
}
