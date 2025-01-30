/*
 * Copyright 2025 IQKV Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.incubator.sample.mixlorem.history.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqkv.incubator.sample.mixlorem.history.consumer.dto.ReportDto;
import com.iqkv.incubator.sample.mixlorem.shared.config.KafkaErrorHandlingProperties;
import com.iqkv.incubator.sample.mixlorem.shared.config.errorhandling.Backoff;
import com.iqkv.incubator.sample.mixlorem.shared.exception.ConsumerRecordProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
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

@Configuration
@RequiredArgsConstructor
class KafkaConfig implements KafkaListenerConfigurer {
  private final LocalValidatorFactoryBean validator;

  @Bean
  ConsumerFactory<String, ReportDto> consumeReportConsumerFactory(KafkaProperties kafkaProperties, SslBundles sslBundles) {
    final var consumerProperties = kafkaProperties.getConsumer().buildProperties(sslBundles);
    try (var serde = new JsonSerde<>(ReportDto.class, new ObjectMapper())) {
      return new DefaultKafkaConsumerFactory<>(consumerProperties,
          new ErrorHandlingDeserializer<>(new StringDeserializer()), new ErrorHandlingDeserializer<>(
          serde.deserializer()));
    }
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, ReportDto> processingReportKafkaListenerContainerFactory(
      ConsumerFactory<String, ReportDto> processingReportConsumerFactory,
      @Value("${lorem.kafka.consumer.threads:4}") int threads,
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
        (cr, e) -> new TopicPartition(cr.topic() + properties.getDeadLetter().getSuffix(), 0));

    // Spread out attempts over time, taking a little longer between each attempt
    // Set a max for retries below max.poll.interval.ms; default: 5m, as otherwise we trigger a consumer rebalance
    Backoff backoff = properties.getBackoff();
    final var exponentialBackOff = new ExponentialBackOffWithMaxRetries(backoff.getMaxRetries());
    exponentialBackOff.setInitialInterval(backoff.getInitialInterval().toMillis());
    exponentialBackOff.setMultiplier(backoff.getMultiplier());
    exponentialBackOff.setMaxInterval(backoff.getMaxInterval().toMillis());

    // Do not try to recover from validation exceptions when validation of orders failed
    final var errorHandler = new DefaultErrorHandler(recoverer, exponentialBackOff);
    errorHandler.addNotRetryableExceptions(jakarta.validation.ValidationException.class);
    errorHandler.addNotRetryableExceptions(ConsumerRecordProcessingException.class);
    return errorHandler;
  }

  @Override
  public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
    registrar.setValidator(this.validator);
  }
}
