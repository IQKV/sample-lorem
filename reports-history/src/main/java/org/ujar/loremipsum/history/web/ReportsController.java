package org.ujar.loremipsum.history.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.loremipsum.history.entity.Report;
import org.ujar.loremipsum.history.repository.ReportRepository;

@RestController
@Tag(name = "History of words processing reports controller", description = "Retrieve pageable statistic information.")
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class ReportsController {
  private final ReportRepository reportRepository;

  @GetMapping
  public ResponseEntity<List<Report>> findAll(@ParameterObject Pageable pageable) {
    return new ResponseEntity<>(reportRepository.findAllByOrderByIdDesc(pageable), HttpStatus.OK);
  }
}
