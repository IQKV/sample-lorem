package org.ujar.bs.dst.loremipsum.processing.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.bs.dst.loremipsum.processing.enums.LengthType;
import org.ujar.bs.dst.loremipsum.processing.model.Report;
import org.ujar.bs.dst.loremipsum.processing.service.HistoryNotifier;
import org.ujar.bs.dst.loremipsum.processing.service.LorIpsumNetClient;
import org.ujar.bs.dst.loremipsum.processing.service.WordsAnalyser;

@RestController
@Tag(name = "Words processing controller", description = "Retrieve statistic information.")
@RequestMapping("/api")
record WordsProcessingController(LorIpsumNetClient httpClient,
                                 WordsAnalyser wordsAnalyser,
                                 HistoryNotifier historyNotifier) {

  @GetMapping("/v1/text")
  @Operation(description = "Make http request to loripsum.net API, process response & generate report")
  ResponseEntity<Report> getReport(
      @RequestParam(name = "p")
      @Parameter(description = "indicates the max number of paragraphs")
      @Schema(minimum = "1", maximum = "5")
      @Min(1)
      @Max(5) Integer paragraphsNum,
      @RequestParam("l") @Parameter(description = "indicates length of each paragraph") final LengthType lengthType) {
    final var text = httpClient.getText(paragraphsNum, lengthType);
    final var report = wordsAnalyser.analyze(text);
    historyNotifier.notifyReport(report);
    return new ResponseEntity<>(report, HttpStatus.OK);
  }
}
