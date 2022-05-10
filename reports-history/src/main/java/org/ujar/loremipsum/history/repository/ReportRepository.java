package org.ujar.loremipsum.history.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ujar.loremipsum.history.entity.Report;

public interface ReportRepository
    extends PagingAndSortingRepository<Report, Long>, JpaRepository<Report, Long> {
  List<Report> findAllByOrderByIdDesc(Pageable pageRequest);
}
