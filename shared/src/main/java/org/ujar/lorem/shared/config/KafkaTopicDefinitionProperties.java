package org.ujar.lorem.shared.config;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.ujar.lorem.shared.config.topic.TopicDefinition;

@ConfigurationProperties(prefix = "lorem.kafka")
public record KafkaTopicDefinitionProperties(@NotNull Map<String, TopicDefinition> topics) {

  public Map<String, TopicDefinition> getTopics() {
    return topics();
  }

  public TopicDefinition get(String key) {
    return topics.get(key);
  }
}
