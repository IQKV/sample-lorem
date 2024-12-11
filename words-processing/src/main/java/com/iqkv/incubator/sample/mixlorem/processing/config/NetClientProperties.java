package com.iqkv.incubator.sample.mixlorem.processing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lorem.net-client")
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
