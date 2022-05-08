package org.ujar.loremipsum.processing.kafka;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.ujar.loremipsum.processing.config.KafkaTopics;
import org.ujar.loremipsum.processing.model.Report;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReportMessageProducer {
   private final KafkaTemplate<String, Report> kafkaTemplate;
   private final KafkaTopics topics;

  /**
   * Send message to Kafka broker with avoiding transaction-aware configuration environment
   */
   public void send(Report report) {
    var key = UUID.randomUUID().toString();
    log.info("Send report message, key: {}, value: {}", key, report);
    kafkaTemplate.executeInTransaction((t) -> t.send(topics.getWordsProcessedTopic(), key, report));
  }
}
