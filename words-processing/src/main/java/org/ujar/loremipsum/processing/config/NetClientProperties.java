package org.ujar.loremipsum.processing.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "loremipsum.net-client")
public class NetClientProperties {
  private final String apiServerUrlTemplate;
  private final Integer connectTimeout;
  private final Integer requestTimeout;
}
