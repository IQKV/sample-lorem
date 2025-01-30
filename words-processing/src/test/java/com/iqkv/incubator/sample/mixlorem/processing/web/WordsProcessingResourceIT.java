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

package com.iqkv.incubator.sample.mixlorem.processing.web;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.iqkv.incubator.sample.mixlorem.processing.util.TestUtils.getFileAsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
record WordsProcessingResourceIT(@Autowired MockMvc mockMvc) {

  private static Stream<Arguments> provideStringsForAnalysis() {
    return Stream.of(
        Arguments.of(
            "/api/1/short",
            "/api/v1/text?p=1&l=short",
            "1_short",
            """
                {  "freq_word":"vacuitate",
                  "avg_paragraph_size":27
                }"""),
        Arguments.of(
            "/api/10/verylong",
            "/api/v1/text?p=10&l=verylong",
            "10_verylong",
            """
                {  "freq_word":"et",
                  "avg_paragraph_size":215
                }""")

    );
  }

  @BeforeEach
  void setUp() {
    WireMock.reset();
  }

  @SneakyThrows
  @ParameterizedTest
  @MethodSource("provideStringsForAnalysis")
  void shouldProcessTextAndGenerateReport(String externalUrl, String apiUrl, String file, String result) {
    stubFor(WireMock.get(externalUrl).willReturn(
            aResponse()
                .withStatus(HttpStatus.OK.value())
                .withBody(getFileAsString("response/" + file + ".html"))
                .withHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)
        )
    );
    mockMvc.perform(get(apiUrl))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(result, false));
  }
}
