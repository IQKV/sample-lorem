package org.ujar.loremipsum.history.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ujar.loremipsum.history.entity.Report;

public interface ReportRepository
    extends PagingAndSortingRepository<Report, Long>, JpaRepository<Report, Long> {
  Page<Report> findAllByOrderByIdDesc(Pageable pageRequest);
}
