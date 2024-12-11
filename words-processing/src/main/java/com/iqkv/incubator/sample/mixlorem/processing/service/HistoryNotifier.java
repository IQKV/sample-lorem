package com.iqkv.incubator.sample.mixlorem.processing.service;

import com.iqkv.incubator.sample.mixlorem.processing.model.Report;
import com.iqkv.incubator.sample.mixlorem.processing.producer.ReportMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryNotifier {

  private final ReportMessageProducer messageProducer;

  /**
   * Delegate report to message producer
   */
  public void notifyReport(Report report) {
    messageProducer.send(report);
  }
}
