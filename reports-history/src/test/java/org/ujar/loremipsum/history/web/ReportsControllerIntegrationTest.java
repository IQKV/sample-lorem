package org.ujar.loremipsum.history.web;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
record ReportsControllerIntegrationTest(MockMvc mockMvc) {
  ReportsControllerIntegrationTest(@Autowired MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @ParameterizedTest
  @MethodSource("provideReports")
  void findAll_Success(String apiUrl, String result) throws Exception {
    mockMvc.perform(get(apiUrl))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(result, false));
  }

  private static Stream<Arguments> provideReports() {
    return Stream.of(
        Arguments.of(
            "/api/v1/history?page=0&size=2",
            "[\n"
            + "  {\n"
            + "    \"freq_word\": \"et\",\n"
            + "    \"avg_paragraph_size\": 125,\n"
            + "    \"avg_paragraph_processing_time\": 295433,\n"
            + "    \"total_processing_time\": 2008500\n"
            + "  },\n"
            + "  {\n"
            + "    \"freq_word\": \"etiam\",\n"
            + "    \"avg_paragraph_size\": 65,\n"
            + "    \"avg_paragraph_processing_time\": 219700,\n"
            + "    \"total_processing_time\": 871600\n"
            + "  }\n"
            + "]"),
        Arguments.of(
            "/api/v1/history?page=2&size=2",
            "[\n"
            + "  {\n"
            + "    \"freq_word\": \"est\",\n"
            + "    \"avg_paragraph_size\": 31,\n"
            + "    \"avg_paragraph_processing_time\": 335800,\n"
            + "    \"total_processing_time\": 5872600\n"
            + "  }\n"
            + "]")

    );
  }
}
