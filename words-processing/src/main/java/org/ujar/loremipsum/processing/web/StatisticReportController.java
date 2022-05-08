package org.ujar.loremipsum.processing.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.loremipsum.processing.service.HistoryNotifier;
import org.ujar.loremipsum.processing.enums.LengthType;
import org.ujar.loremipsum.processing.model.Report;
import org.ujar.loremipsum.processing.service.LoremIpsumNetClient;
import org.ujar.loremipsum.processing.service.WordsAnalyser;

@RestController
@Tag(name = "Statistic report controller", description = "Retrieve statistic information.")
@RequestMapping("/v1/betvictor/text")
@RequiredArgsConstructor
public class StatisticReportController {

  private final LoremIpsumNetClient httpClient;
  private final WordsAnalyser wordsAnalyser;
  private final HistoryNotifier historyNotifier;

  @GetMapping
  @Operation(description = "Make http request to loremipsum.net, process response & generate report")
  public ResponseEntity<Report> getReport(
      @RequestParam(name = "p") @Parameter(description = "indicates the max number of paragraphs")
      @Positive Integer paragraphsNum,
      @RequestParam("l") @Parameter(description = "indicates length of each paragraph") LengthType lengthType) {
    var text = httpClient.getText(paragraphsNum, lengthType);
    var report = wordsAnalyser.analyze(text);
    historyNotifier.notifyReport(report);
    return new ResponseEntity<>(report, HttpStatus.OK);
  }
}
