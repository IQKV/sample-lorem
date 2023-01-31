package org.ujar.bs.dst.loremipsum.shared.config;

import static org.ujar.bs.dst.loremipsum.shared.config.Constants.TOPIC_DEFINITION_WORDS_PROCESSED;

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
public class KafkaAdminConfig {
  private final KafkaTopicDefinitionProperties topicDefinitions;
  private final KafkaErrorHandlingProperties errorHandlingProperties;

  @Bean
  NewTopic wordsProcessedKafkaTopic() {
    final var definition = topicDefinitions.get(TOPIC_DEFINITION_WORDS_PROCESSED);
    return TopicBuilder
        .name(definition.name())
        .partitions(definition.partitions())
        .config(TopicConfig.RETENTION_MS_CONFIG, "" + definition.retention().toMillis())
        .build();
  }

  @Bean
  NewTopic wordsProcessedKafkaDeadLetterTopic() {
    return TopicBuilder
        .name(topicDefinitions.get(TOPIC_DEFINITION_WORDS_PROCESSED).name() + errorHandlingProperties.deadLetter()
            .suffix())
        .partitions(1)
        .config(TopicConfig.RETENTION_MS_CONFIG, "" + errorHandlingProperties.deadLetter().retention().toMillis())
        .build();
  }
}
