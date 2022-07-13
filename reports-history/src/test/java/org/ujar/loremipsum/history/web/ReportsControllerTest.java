package org.ujar.loremipsum.history.web;


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
record ReportsControllerTest(MockMvc mockMvc) {
  ReportsControllerTest(@Autowired MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

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
