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

package com.iqkv.incubator.sample.mixlorem.shared.config;

import static com.iqkv.incubator.sample.mixlorem.shared.config.Constants.TOPIC_DEFINITION_WORDS_PROCESSED;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = "lorem.kafka.admin.create-topics", havingValue = "true")
public class KafkaAdminConfig {
  private final KafkaTopicDefinitionProperties topicDefinitions;
  private final KafkaErrorHandlingProperties errorHandlingProperties;

  @Bean
  NewTopic wordsProcessedKafkaTopic() {
    final var definition = topicDefinitions.get(TOPIC_DEFINITION_WORDS_PROCESSED);
    return TopicBuilder
        .name(definition.getName())
        .partitions(definition.getPartitions())
        .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(definition.getRetention().toMillis()))
        .build();
  }

  @Bean
  NewTopic wordsProcessedKafkaDeadLetterTopic() {
    return TopicBuilder
        .name(topicDefinitions.get(TOPIC_DEFINITION_WORDS_PROCESSED).getName() + errorHandlingProperties.getDeadLetter()
            .getSuffix())
        .partitions(1)
        .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(errorHandlingProperties.getDeadLetter()
            .getRetention().toMillis()))
        .build();
  }
}
