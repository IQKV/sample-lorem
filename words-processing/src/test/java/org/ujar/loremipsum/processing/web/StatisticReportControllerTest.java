package org.ujar.loremipsum.processing.web;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.ujar.loremipsum.processing.TestUtils.getFileAsString;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
record StatisticReportControllerTest(MockMvc mockMvc) {
  StatisticReportControllerTest(@Autowired MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @BeforeEach
  void setUp() {
    WireMock.reset();
  }

  @Test
  void getReport_Success() throws Exception {
    stubFor(WireMock.get("/api/1/short").willReturn(
            aResponse()
                .withStatus(HttpStatus.OK.value())
                .withBody(getFileAsString("response/1_short.html"))
                .withHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE)
        )
    );
    mockMvc.perform(get("/v1/betvictor/text?p=1&l=short"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\n"
            + "  \"freq_word\":\"Lorem\",\n"
            + "  \"avg_paragraph_size\":1,\n"
            + "  \"avg_paragraph_processing_time\":1,\n"
            + "  \"total_processing_time\":1\n"
            + "}"
        ));
  }
}
