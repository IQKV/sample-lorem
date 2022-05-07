package org.ujar.loremipsum.reportshistory.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "loremipsum.kafka.topics")
public class KafkaTopics {
  private String rejectedMessagesTopic;
  private String wordsProcessedTopic;
  private int wordsProcessedPartitions;
  private String wordsProcessedRetentionMs;
}
