package org.ujar.loremipsum.wordsstatistic.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.loremipsum.wordsstatistic.enums.LengthType;
import org.ujar.loremipsum.wordsstatistic.model.Report;
import org.ujar.loremipsum.wordsstatistic.service.HistoryNotifier;
import org.ujar.loremipsum.wordsstatistic.service.WordsAnalyser;

@RestController
@Tag(name = "Statistic report controller", description = "Retrieve statistic information.")
@RequestMapping("/v1/betvictor/text")
@RequiredArgsConstructor
public class StatisticReportController {

  private final WordsAnalyser wordsAnalyser;
  private final HistoryNotifier historyNotifier;

  @GetMapping
  public ResponseEntity<Report> getReport(@Positive @RequestParam("p") Integer paragraphsNum,
                                          @RequestParam("l") LengthType lengthType) {
    var report = wordsAnalyser.analyse(paragraphsNum, lengthType);
    historyNotifier.send(report);
    return new ResponseEntity<>(report, HttpStatus.OK);
  }
}
