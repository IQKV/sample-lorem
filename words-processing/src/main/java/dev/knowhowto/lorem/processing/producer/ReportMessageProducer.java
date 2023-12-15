package dev.knowhowto.lorem.processing.producer;

import static dev.knowhowto.lorem.shared.config.Constants.TOPIC_DEFINITION_WORDS_PROCESSED;

import java.util.UUID;

import dev.knowhowto.lorem.processing.model.Report;
import dev.knowhowto.lorem.shared.config.KafkaTopicDefinitionProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReportMessageProducer {
  private final KafkaTemplate<String, Report> kafkaTemplate;
  private final KafkaTopicDefinitionProperties topics;

  /**
   * Send message to Kafka broker with avoiding transaction-aware configuration environment
   */
  public void send(Report report) {
    final var key = UUID.randomUUID().toString();
    log.info("( {} ) Send report message, key: {}, value: {}", Thread.currentThread().getName(), key, report);
    kafkaTemplate.executeInTransaction(op -> op.send(topics.get(TOPIC_DEFINITION_WORDS_PROCESSED).getName(), key, report)
    );
  }
}
