package org.ujar.loremipsum.wordsstatistic.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({NetClientProperties.class, KafkaTopics.class})
public class ApplicationConfig {
}
