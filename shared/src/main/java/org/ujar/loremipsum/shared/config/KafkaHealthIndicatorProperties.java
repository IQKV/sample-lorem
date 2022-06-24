package org.ujar.loremipsum.shared.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "management.health.kafka")
public class KafkaHealthIndicatorProperties {
  private int responseTimeout = 500;
}
