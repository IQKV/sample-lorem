package org.ujar.loremipsum.reportshistory.config;

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
import org.ujar.loremipsum.reportshistory.kafka.RejectedMessageHandler;
import org.ujar.loremipsum.reportshistory.model.Report;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {
  private final KafkaTopics kafkaTopics;

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
  public ConsumerFactory<String, Report> processingReportConsumerFactory(KafkaProperties kafkaProperties) {
    var consumerProperties = kafkaProperties.getConsumer().buildProperties();
    return new DefaultKafkaConsumerFactory<>(consumerProperties,
        new ErrorHandlingDeserializer<>(new StringDeserializer()), new ErrorHandlingDeserializer<>(
        new JsonSerde<>(Report.class, new ObjectMapper()).deserializer()));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Report> processingReportKafkaListenerContainerFactory(
      ConsumerFactory<String, Report> processingReportConsumerFactory,
      KafkaTemplate<String, String> rejectedMessageKafkaTemplate,
      @Value("${spring.kafka.consumer.threads:4}") int threads) {
    ConcurrentKafkaListenerContainerFactory<String, Report> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(processingReportConsumerFactory);
    factory.setErrorHandler(new RejectedMessageHandler(
        kafkaTopics.getRejectedMessagesTopic(),
        rejectedMessageKafkaTemplate));
    factory.setConcurrency(threads);
    return factory;
  }
}
