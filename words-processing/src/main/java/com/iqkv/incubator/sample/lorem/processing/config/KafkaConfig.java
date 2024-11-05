package com.iqkv.incubator.sample.lorem.processing.config;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqkv.incubator.sample.lorem.processing.model.Report;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
class KafkaConfig {
  @Bean
  ProducerFactory<String, Report> reportMessageProducerFactory(KafkaProperties kafkaProperties, SslBundles sslBundles) {
    try (var serde = new JsonSerde<>(Report.class, new ObjectMapper())) {
      final var producerProperties = kafkaProperties.getProducer().buildProperties(sslBundles);
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
