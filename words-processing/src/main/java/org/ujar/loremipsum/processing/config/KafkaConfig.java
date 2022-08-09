package org.ujar.loremipsum.processing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.ujar.loremipsum.processing.model.Report;

@Configuration
class KafkaConfig {
  @Bean
  ProducerFactory<String, Report> reportMessageProducerFactory(KafkaProperties kafkaProperties) {
    try (var serde = new JsonSerde<>(Report.class, new ObjectMapper())) {
      final var producerProperties = kafkaProperties.getProducer().buildProperties();
      final var producerFactory = new DefaultKafkaProducerFactory<>(producerProperties,
          new StringSerializer(),
          serde.serializer());
      producerFactory.setTransactionIdPrefix(getTransactionPrefix());
      return producerFactory;
    }
  }

  @Bean
  KafkaTemplate<String, Report> reportMessageKafkaTemplate(
      ProducerFactory<String, Report> reportMessageProducerFactory) {
    return new KafkaTemplate<>(reportMessageProducerFactory);
  }

  private String getTransactionPrefix() {
    return "tx-" + UUID.randomUUID() + "-";
  }
}
