package org.ujar.loremipsum.wordsstatistic.config;

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
public class KafkaTopicsConfiguration {
  private final KafkaTopics topics;
  
  @Bean
  public NewTopic wordsProcessedKafkaTopic() {
    return TopicBuilder
        .name(topics.getWordsProcessedTopic())
        .partitions(topics.getWordsProcessedPartitions())
        .config(TopicConfig.RETENTION_MS_CONFIG, topics.getWordsProcessedRetentionMs())
        .build();
  }
}
