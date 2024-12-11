package com.iqkv.incubator.sample.mixlorem.shared.config;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

import com.iqkv.incubator.sample.mixlorem.shared.config.topic.TopicDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lorem.kafka")
public record KafkaTopicDefinitionProperties(@NotNull Map<String, TopicDefinition> topics) {

  public Map<String, TopicDefinition> getTopics() {
    return topics();
  }

  public TopicDefinition get(String key) {
    return topics.get(key);
  }
}
