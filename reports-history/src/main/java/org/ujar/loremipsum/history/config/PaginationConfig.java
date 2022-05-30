package org.ujar.loremipsum.history.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PaginationConfig {
  @Bean
  PageableHandlerMethodArgumentResolverCustomizer pageableResolverCustomizer() {
    return pageableResolver -> pageableResolver.setSizeParameterName("limit");
  }
}
