package org.ujar.bs.dst.loremipsum.processing.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.ujar.bs.dst.loremipsum.processing.model.Report;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordsProcessingReportTestConsumer {

  @KafkaListener(containerFactory = "processingReportKafkaListenerContainerFactory",
                 topics = "${loremipsum.kafka.topics.words-processed.name}",
                 groupId = "${spring.kafka.consumer.group-id}")
  public void consume(ConsumerRecord<String, Report> consumerRecord) {
    log.info("( {} ) Received report, key: {}, value: {}",
        Thread.currentThread().getName(), consumerRecord.key(), consumerRecord.value());
  }
}
