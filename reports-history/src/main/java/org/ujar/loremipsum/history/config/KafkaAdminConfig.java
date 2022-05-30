package org.ujar.loremipsum.history.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = "loremipsum.kafka.create-topics-on-startup", havingValue = "true")
class KafkaAdminConfig {
  private final KafkaTopicsProperties topics;
  private final KafkaErrorHandlingProperties errorHandling;

  @Bean
  NewTopic wordsProcessedKafkaTopic() {
    var definition = topics.get(KafkaTopicsProperties.WORDS_PROCESSED);
    return TopicBuilder
        .name(definition.name())
        .partitions(definition.partitions())
        .config(TopicConfig.RETENTION_MS_CONFIG, "" + definition.retention().toMillis())
        .build();
  }

  @Bean
  NewTopic wordsProcessedKafkaDeadLetterTopic() {
    return TopicBuilder
        .name(topics.get(KafkaTopicsProperties.WORDS_PROCESSED).name() + errorHandling.deadLetter().suffix())
        .partitions(1)
        .config(TopicConfig.RETENTION_MS_CONFIG,  "" + errorHandling.deadLetter().retention().toMillis())
        .build();
  }
}
