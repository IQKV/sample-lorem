package org.ujar.loremipsum.history.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "loremipsum.kafka.topics")
public class KafkaTopics {
  String rejectedMessagesTopic;
  String wordsProcessedTopic;
  int wordsProcessedPartitions;
  String wordsProcessedRetentionMs;
}
