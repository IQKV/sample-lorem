/*
 * Copyright 2025 IQKV Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.incubator.sample.mixlorem.history.web;

import java.util.List;

import com.iqkv.incubator.sample.mixlorem.history.entity.Report;
import com.iqkv.incubator.sample.mixlorem.history.repository.ReportRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "History of words processing reports", description = "Retrieve pageable statistic information.")
@RequestMapping("/api")
record ReportsResource(ReportRepository reportRepository) {

  @GetMapping("/v1/history")
  ResponseEntity<List<Report>> findAll(@ParameterObject final Pageable pageable) {
    return new ResponseEntity<>(reportRepository.findAllByOrderByIdDesc(pageable), HttpStatus.OK);
  }
}
