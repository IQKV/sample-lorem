package dev.knowhowto.lorem.processing.service;

import dev.knowhowto.lorem.processing.model.Report;
import dev.knowhowto.lorem.processing.producer.ReportMessageProducer;
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
