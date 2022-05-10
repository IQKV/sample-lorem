package org.ujar.loremipsum.history.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.ujar.loremipsum.history.kafka.RejectedMessageHandler;
import org.ujar.loremipsum.history.kafka.dto.ReportDto;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {
  private final KafkaTopics topics;

  @Bean
  public ProducerFactory<String, String> rejectedMessageProducerFactory(KafkaProperties kafkaProperties) {
    var producerProperties = kafkaProperties.getProducer().buildProperties();
    return new DefaultKafkaProducerFactory<>(producerProperties, new StringSerializer(), new StringSerializer());
  }

  @Bean
  public KafkaTemplate<String, String> rejectedMessageKafkaTemplate(
      ProducerFactory<String, String> rejectedMessageProducerFactory) {
    return new KafkaTemplate<>(rejectedMessageProducerFactory);
  }

  @Bean
  public ConsumerFactory<String, ReportDto> processingReportConsumerFactory(KafkaProperties kafkaProperties) {
    var consumerProperties = kafkaProperties.getConsumer().buildProperties();
    try (var serde = new JsonSerde<>(ReportDto.class, new ObjectMapper())) {
      return new DefaultKafkaConsumerFactory<>(consumerProperties,
          new ErrorHandlingDeserializer<>(new StringDeserializer()), new ErrorHandlingDeserializer<>(
          serde.deserializer()));
    }
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ReportDto> processingReportKafkaListenerContainerFactory(
      ConsumerFactory<String, ReportDto> processingReportConsumerFactory,
      KafkaTemplate<String, String> rejectedMessageKafkaTemplate,
      @Value("${loremipsum.kafka.consumer.threads:4}") int threads) {
    ConcurrentKafkaListenerContainerFactory<String, ReportDto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(processingReportConsumerFactory);
    factory.setErrorHandler(new RejectedMessageHandler(
        topics.getRejectedMessagesTopic(),
        rejectedMessageKafkaTemplate));
    factory.setConcurrency(threads);
    return factory;
  }
}
