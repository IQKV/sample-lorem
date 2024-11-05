package com.iqkv.incubator.sample.lorem.processing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqkv.incubator.sample.lorem.processing.model.Report;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@RequiredArgsConstructor
public class KafkaTestConfig {
  @Bean
  ConsumerFactory<String, Report> processingReportConsumerFactory(KafkaProperties kafkaProperties, SslBundles sslBundles) {
    final var consumerProperties = kafkaProperties.getConsumer().buildProperties(sslBundles);
    try (var serde = new JsonSerde<>(Report.class, new ObjectMapper())) {
      return new DefaultKafkaConsumerFactory<>(consumerProperties,
          new ErrorHandlingDeserializer<>(new StringDeserializer()), new ErrorHandlingDeserializer<>(
          serde.deserializer()));
    }
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, Report> processingReportKafkaListenerContainerFactory(
      ConsumerFactory<String, Report> processingReportConsumerFactory,
      @Value("${lorem.kafka.consumer.threads:2}") int threads) {
    ConcurrentKafkaListenerContainerFactory<String, Report> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(processingReportConsumerFactory);
    factory.setConcurrency(threads);
    return factory;
  }
}
