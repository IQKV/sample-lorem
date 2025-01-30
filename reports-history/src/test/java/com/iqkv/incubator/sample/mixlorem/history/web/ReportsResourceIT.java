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

package com.iqkv.incubator.sample.mixlorem.history.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
record ReportsResourceIT(@Autowired MockMvc mockMvc) {


  @SneakyThrows
  @ParameterizedTest
  @MethodSource("provideReports")
  void shouldReceiveAllReports(String apiUrl, String result) {
    mockMvc.perform(get(apiUrl))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(result, false));
  }

  private static Stream<Arguments> provideReports() {
    return Stream.of(
        Arguments.of(
            "/api/v1/history?size=2",
            """
                [
                  {
                    "freq_word": "et",
                    "avg_paragraph_size": 125,
                    "avg_paragraph_processing_time": 295433,
                    "total_processing_time": 2008500
                  },
                  {
                    "freq_word": "etiam",
                    "avg_paragraph_size": 65,
                    "avg_paragraph_processing_time": 219700,
                    "total_processing_time": 871600
                  }
                ]"""),
        Arguments.of(
            "/api/v1/history?page=2&size=2",
            """
                [
                  {
                    "freq_word": "est",
                    "avg_paragraph_size": 31,
                    "avg_paragraph_processing_time": 335800,
                    "total_processing_time": 5872600
                  }
                ]""")

    );
  }
}
