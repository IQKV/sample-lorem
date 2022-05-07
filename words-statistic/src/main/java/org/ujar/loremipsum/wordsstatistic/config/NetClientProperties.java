package org.ujar.loremipsum.wordsstatistic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "loremipsum.net-client")
public class NetClientProperties {
  private String apiServerUrlTemplate;
  private int connectTimeout = 1;
  private int requestTimeout = 2;
}
