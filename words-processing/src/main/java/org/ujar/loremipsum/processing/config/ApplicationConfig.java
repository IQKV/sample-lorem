package org.ujar.loremipsum.processing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({NetClientProperties.class, KafkaTopicsProperties.class})
public class ApplicationConfig {
}
