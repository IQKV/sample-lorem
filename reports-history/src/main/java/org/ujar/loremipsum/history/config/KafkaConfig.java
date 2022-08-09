package org.ujar.loremipsum.history.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.ujar.loremipsum.history.consumer.dto.ReportDto;
import org.ujar.loremipsum.shared.config.KafkaErrorHandlingProperties;
import org.ujar.loremipsum.shared.config.errorhandling.Backoff;
import org.ujar.loremipsum.shared.exception.ConsumerRecordProcessingException;

@Configuration
@RequiredArgsConstructor
class KafkaConfig implements KafkaListenerConfigurer {
  private final LocalValidatorFactoryBean validator;

  @Bean
  ConsumerFactory<String, ReportDto> consumeReportConsumerFactory(KafkaProperties kafkaProperties) {
    final var consumerProperties = kafkaProperties.getConsumer().buildProperties();
    try (var serde = new JsonSerde<>(ReportDto.class, new ObjectMapper())) {
      return new DefaultKafkaConsumerFactory<>(consumerProperties,
          new ErrorHandlingDeserializer<>(new StringDeserializer()), new ErrorHandlingDeserializer<>(
          serde.deserializer()));
    }
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, ReportDto> processingReportKafkaListenerContainerFactory(
      ConsumerFactory<String, ReportDto> processingReportConsumerFactory,
      @Value("${loremipsum.kafka.consumer.threads:4}") int threads,
      DefaultErrorHandler errorHandler) {
    ConcurrentKafkaListenerContainerFactory<String, ReportDto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(processingReportConsumerFactory);
    factory.setConcurrency(threads);

    factory.setCommonErrorHandler(errorHandler);

    return factory;
  }

  @Bean
  DefaultErrorHandler kafkaDefaultErrorHandler(
      KafkaOperations<Object, Object> operations,
      KafkaErrorHandlingProperties properties) {
    // Publish to dead letter topic any messages dropped after retries with back off
    final var recoverer = new DeadLetterPublishingRecoverer(operations,
        // Always send to first/only partition of DLT suffixed topic
        (cr, e) -> new TopicPartition(cr.topic() + properties.deadLetter().suffix(), 0));

    // Spread out attempts over time, taking a little longer between each attempt
    // Set a max for retries below max.poll.interval.ms; default: 5m, as otherwise we trigger a consumer rebalance
    Backoff backoff = properties.backoff();
    final var exponentialBackOff = new ExponentialBackOffWithMaxRetries(backoff.maxRetries());
    exponentialBackOff.setInitialInterval(backoff.initialInterval().toMillis());
    exponentialBackOff.setMultiplier(backoff.multiplier());
    exponentialBackOff.setMaxInterval(backoff.maxInterval().toMillis());

    // Do not try to recover from validation exceptions when validation of orders failed
    final var errorHandler = new DefaultErrorHandler(recoverer, exponentialBackOff);
    errorHandler.addNotRetryableExceptions(javax.validation.ValidationException.class);
    errorHandler.addNotRetryableExceptions(ConsumerRecordProcessingException.class);
    return errorHandler;
  }

  @Override
  public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
    // https://docs.spring.io/spring-kafka/docs/2.8.1/reference/html/#kafka-validation
    registrar.setValidator(this.validator);
  }
}
