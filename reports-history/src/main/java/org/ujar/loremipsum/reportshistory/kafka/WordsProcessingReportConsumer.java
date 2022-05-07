package org.ujar.loremipsum.reportshistory.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.ujar.loremipsum.reportshistory.model.Report;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordsProcessingReportConsumer {

  @KafkaListener(containerFactory = "processingReportKafkaListenerContainerFactory",
                 topics = "${loremipsum.kafka.topics.words-processed-topic}",
                 groupId = "${spring.kafka.consumer.group-id}")
  public void consume(ConsumerRecord<String, Report> consumerRecord) {
    try {
      log.info("Received word processing report, key: {}, value: {}", consumerRecord.key(), consumerRecord.value());
    } catch (Exception e) {
      throw new ConsumerRecordProcessingException("Error processing words report.", e);
    }
  }
}
