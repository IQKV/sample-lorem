package org.ujar.loremipsum.history.consumer;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.ujar.loremipsum.history.consumer.dto.ReportDto;
import org.ujar.loremipsum.history.consumer.exception.ConsumerRecordProcessingException;
import org.ujar.loremipsum.history.entity.Report;
import org.ujar.loremipsum.history.repository.ReportRepository;

@Component
@RequiredArgsConstructor
@Slf4j
class WordsProcessingReportConsumer {

  private final ReportRepository repository;

  @KafkaListener(containerFactory = "processingReportKafkaListenerContainerFactory",
                 topics = "${loremipsum.kafka.topics.words-processed.name}",
                 groupId = "${spring.kafka.consumer.group-id}")
  @Transactional
  public void consume(ConsumerRecord<String, ReportDto> consumerRecord) {
    try {
      log.info("( {} ) Received report, key: {}, value: {}",
          Thread.currentThread().getName(), consumerRecord.key(), consumerRecord.value());
      var dto = consumerRecord.value();
      var report = new Report(dto.mostFrequentWord(),
          dto.avgParagraphSize(),
          (int) dto.avgParagraphProcessingTime(),
          (int) dto.totalProcessingTime());
      repository.saveAndFlush(report);
    } catch (Exception e) {
      throw new ConsumerRecordProcessingException("Error processing report data.", e);
    }
  }
}
