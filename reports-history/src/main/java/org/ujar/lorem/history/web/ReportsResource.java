package org.ujar.lorem.history.web;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.lorem.history.entity.Report;
import org.ujar.lorem.history.repository.ReportRepository;

@RestController
@Tag(name = "History of words processing reports", description = "Retrieve pageable statistic information.")
@RequestMapping("/api")
record ReportsResource(ReportRepository reportRepository) {

  @GetMapping("/v1/history")
  ResponseEntity<List<Report>> findAll(@ParameterObject final Pageable pageable) {
    return new ResponseEntity<>(reportRepository.findAllByOrderByIdDesc(pageable), HttpStatus.OK);
  }
}
