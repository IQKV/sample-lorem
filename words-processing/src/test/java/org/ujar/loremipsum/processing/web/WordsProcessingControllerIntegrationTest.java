package org.ujar.loremipsum.processing.web;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.ujar.loremipsum.processing.util.TestUtils.getFileAsString;

import com.github.tomakehurst.wiremock.client.WireMock;
import java.util.stream.Stream;
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
record WordsProcessingControllerIntegrationTest(MockMvc mockMvc) {
  WordsProcessingControllerIntegrationTest(@Autowired MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  private static Stream<Arguments> provideStringsForAnalysis() {
    return Stream.of(
        Arguments.of(
            "/api/1/short",
            "/api/v1/text?p=1&l=short",
            "1_short",
            "{"
            + "  \"freq_word\":\"vacuitate\",\n"
            + "  \"avg_paragraph_size\":27\n"
            + "}"),
        Arguments.of(
            "/api/10/verylong",
            "/api/v1/text?p=10&l=verylong",
            "10_verylong",
            "{"
            + "  \"freq_word\":\"et\",\n"
            + "  \"avg_paragraph_size\":215\n"
            + "}")

    );
  }

  @BeforeEach
  void setUp() {
    WireMock.reset();
  }

  @ParameterizedTest
  @MethodSource("provideStringsForAnalysis")
  void getReport_Success(String externalUrl, String apiUrl, String file, String result) throws Exception {
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
