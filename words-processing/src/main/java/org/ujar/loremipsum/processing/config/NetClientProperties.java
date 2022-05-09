package org.ujar.loremipsum.processing.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConstructorBinding
@ConfigurationProperties(prefix = "loremipsum.net-client")
public class NetClientProperties {
  String apiServerUrlTemplate;
  int connectTimeout = 1;
  int requestTimeout = 2;
}
