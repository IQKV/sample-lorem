package org.ujar.loremipsum.history.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.ujar.loremipsum.history.consumer.dto.ReportDto;

@Configuration
@RequiredArgsConstructor
class KafkaConfig implements KafkaListenerConfigurer {
  private final LocalValidatorFactoryBean validator;

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
    var recoverer = new DeadLetterPublishingRecoverer(operations,
        // Always send to first/only partition of DLT suffixed topic
        (cr, e) -> new TopicPartition(cr.topic() + properties.deadLetter().suffix(), 0));

    // Spread out attempts over time, taking a little longer between each attempt
    // Set a max for retries below max.poll.interval.ms; default: 5m, as otherwise we trigger a consumer rebalance
    Backoff backoff = properties.backoff();
    var exponentialBackOff = new ExponentialBackOffWithMaxRetries(backoff.maxRetries());
    exponentialBackOff.setInitialInterval(backoff.initialInterval().toMillis());
    exponentialBackOff.setMultiplier(backoff.multiplier());
    exponentialBackOff.setMaxInterval(backoff.maxInterval().toMillis());

    // Do not try to recover from validation exceptions when validation of orders failed
    var errorHandler = new DefaultErrorHandler(recoverer, exponentialBackOff);
    errorHandler.addNotRetryableExceptions(javax.validation.ValidationException.class);
    return errorHandler;
  }

  @Override
  public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
    // https://docs.spring.io/spring-kafka/docs/2.8.1/reference/html/#kafka-validation
    registrar.setValidator(this.validator);
  }
}
