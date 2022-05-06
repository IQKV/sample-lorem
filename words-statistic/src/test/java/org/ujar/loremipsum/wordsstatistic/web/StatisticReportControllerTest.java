package org.ujar.loremipsum.wordsstatistic.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
record StatisticReportControllerTest(MockMvc mockMvc) {
  StatisticReportControllerTest(@Autowired MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void getReport_Success() throws Exception {
    mockMvc.perform(
            get("/v1/betvictor/text?p=1&l=short"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\n"
            + "  \"freq_word\":\"Lorem\",\n"
            + "  \"avg_paragraph_size\":1,\n"
            + "  \"avg_paragraph_processing_time\":1.0,\n"
            + "  \"total_processing_time\":1.0\n"
            + "}"
        ));
  }
}
