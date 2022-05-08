package org.ujar.loremipsum.processing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.ujar.loremipsum.processing.model.Report;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordsProcessingReportTestConsumer {

  @KafkaListener(containerFactory = "processingReportKafkaListenerContainerFactory",
                 topics = "${loremipsum.kafka.topics.words-processed-topic}",
                 groupId = "${spring.kafka.consumer.group-id}")
  public void consume(ConsumerRecord<String, Report> consumerRecord) {
      log.info("Received report, key: {}, value: {}", consumerRecord.key(), consumerRecord.value());
  }
}
