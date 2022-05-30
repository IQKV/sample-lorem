package org.ujar.loremipsum.processing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ujar.loremipsum.processing.model.Report;
import org.ujar.loremipsum.processing.producer.ReportMessageProducer;

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
