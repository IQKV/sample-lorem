package com.iqkv.incubator.sample.mixlorem.processing.consumer;

import com.iqkv.incubator.sample.mixlorem.processing.model.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordsProcessingReportTestConsumer {

  @KafkaListener(containerFactory = "processingReportKafkaListenerContainerFactory",
                 topics = "${lorem.kafka.topics.words-processed.name}",
                 groupId = "${spring.kafka.consumer.group-id}")
  public void consume(ConsumerRecord<String, Report> consumerRecord) {
    log.info("( {} ) Received report, key: {}, value: {}",
        Thread.currentThread().getName(), consumerRecord.key(), consumerRecord.value());
  }
}
