package org.ujar.bs.dst.loremipsum.processing.producer;

import static org.ujar.bs.dst.loremipsum.shared.config.Constants.TOPIC_DEFINITION_WORDS_PROCESSED;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.ujar.bs.dst.loremipsum.processing.model.Report;
import org.ujar.bs.dst.loremipsum.shared.config.KafkaTopicDefinitionProperties;

@Component
@Slf4j
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP2")
public class ReportMessageProducer {
  private final KafkaTemplate<String, Report> kafkaTemplate;
  private final KafkaTopicDefinitionProperties topics;

  /**
   * Send message to Kafka broker with avoiding transaction-aware configuration environment
   */
  public void send(Report report) {
    final var key = UUID.randomUUID().toString();
    log.info("( {} ) Send report message, key: {}, value: {}", Thread.currentThread().getName(), key, report);
    kafkaTemplate.executeInTransaction(op -> op.usingCompletableFuture()
        .send(topics.get(TOPIC_DEFINITION_WORDS_PROCESSED).name(), key, report)
    );
  }
}
