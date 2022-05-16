package org.ujar.loremipsum.processing.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "loremipsum.kafka")
@Validated
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class KafkaTopicsProperties {
  public static final String WORDS_PROCESSED = "words-processed";
  Map<String, TopicDefinition> topics;

  public TopicDefinition get(String key) {
    return topics.get(key);
  }
}

