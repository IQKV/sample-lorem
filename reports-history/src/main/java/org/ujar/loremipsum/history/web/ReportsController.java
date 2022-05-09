package org.ujar.loremipsum.history.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ujar.loremipsum.history.entity.Report;
import org.ujar.loremipsum.history.repository.ReportRepository;
import org.ujar.loremipsum.history.web.dto.PageRequestDto;

@RestController
@Tag(name = "History of words processing report controller", description = "Retrieve pageable statistic information.")
@RequestMapping("/betvictor/history")
@RequiredArgsConstructor
public class ReportsController {
  private final ReportRepository reportRepository;

  @GetMapping
  public ResponseEntity<Page<Report>> findAll(@ParameterObject @Valid PageRequestDto request) {
    var pageRequest = PageRequest.of(request.getPage(), request.getSize());
    pageRequest.withSort(Sort.Direction.DESC, "id");
    return new ResponseEntity<>(reportRepository.findAll(pageRequest), HttpStatus.OK);
  }
}
