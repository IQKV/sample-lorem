package org.ujar.bs.dst.loremipsum.shared.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;
import org.ujar.bs.dst.loremipsum.shared.config.topic.TopicDefinition;

@ConstructorBinding
@ConfigurationProperties(prefix = "loremipsum.kafka")
@Validated
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public record KafkaTopicDefinitionProperties(@NonNull Map<String, TopicDefinition> topics) {

  public Map<String, TopicDefinition> getTopics() {
    return topics();
  }

  public TopicDefinition get(String key) {
    return topics.get(key);
  }
}
