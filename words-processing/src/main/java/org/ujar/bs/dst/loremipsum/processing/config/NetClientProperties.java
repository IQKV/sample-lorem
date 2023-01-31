package org.ujar.bs.dst.loremipsum.processing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "loripsum.net-client")
public record NetClientProperties(String apiServerUrlTemplate, Integer connectTimeout, Integer requestTimeout) {
  public String getApiServerUrlTemplate() {
    return apiServerUrlTemplate();
  }

  public Integer getConnectTimeout() {
    return connectTimeout();
  }

  public Integer getRequestTimeout() {
    return requestTimeout();
  }
}
