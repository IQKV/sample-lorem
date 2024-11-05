package com.iqkv.incubator.sample.lorem.processing.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import com.iqkv.incubator.sample.lorem.processing.enums.LengthType;
import com.iqkv.incubator.sample.lorem.processing.model.Report;
import com.iqkv.incubator.sample.lorem.processing.service.HistoryNotifier;
import com.iqkv.incubator.sample.lorem.processing.service.LorIpsumNetClient;
import com.iqkv.incubator.sample.lorem.processing.service.WordsAnalyser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Words processing", description = "Retrieve statistic information.")
@RequestMapping("/api")
record WordsProcessingResource(LorIpsumNetClient httpClient,
                               WordsAnalyser wordsAnalyser,
                               HistoryNotifier historyNotifier) {

  @GetMapping("/v1/text")
  @Operation(description = "Make http request to loripsum.net API, process response & generate report")
  ResponseEntity<Report> getReport(
      @RequestParam(name = "p")
      @Parameter(description = "indicates the max number of paragraphs")
      @Schema(minimum = "1", maximum = "10")
      @Min(1)
      @Max(10) Integer paragraphsNum,
      @RequestParam("l") @Parameter(description = "indicates length of each paragraph") final LengthType lengthType) {
    final var text = httpClient.getText(paragraphsNum, lengthType);
    final var report = wordsAnalyser.analyze(text);
    historyNotifier.notifyReport(report);
    return new ResponseEntity<>(report, HttpStatus.OK);
  }
}
