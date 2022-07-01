package org.ujar.loremipsum.shared.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "management.health.kafka")
public class KafkaHealthIndicatorProperties {
  int responseTimeout = 500;
}
