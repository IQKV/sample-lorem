package org.ujar.loremipsum.processing.config;

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
class KafkaCreateTopicsConfig {
  private final KafkaTopicsProperties topics;

  @Bean
  public NewTopic wordsProcessedKafkaTopic() {
    var definition = topics.get(KafkaTopicsProperties.WORDS_PROCESSED);
    return TopicBuilder
        .name(definition.name())
        .partitions(definition.partitions())
        .config(TopicConfig.RETENTION_MS_CONFIG, "" + definition.retention().toMillis())
        .build();
  }
}
