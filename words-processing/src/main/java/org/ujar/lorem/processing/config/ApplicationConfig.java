package org.ujar.lorem.processing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.lorem.shared.config.BuildInfoConfig;
import org.ujar.lorem.shared.config.KafkaErrorHandlingProperties;
import org.ujar.lorem.shared.config.KafkaHealthIndicatorConfig;
import org.ujar.lorem.shared.config.KafkaTopicDefinitionProperties;
import org.ujar.lorem.shared.config.SwaggerConfig;

@Configuration
@Import(value = {
    SwaggerConfig.class,
    BuildInfoConfig.class,
    KafkaHealthIndicatorConfig.class
})
@EnableConfigurationProperties(value = {
    NetClientProperties.class,
    KafkaErrorHandlingProperties.class,
    KafkaTopicDefinitionProperties.class
})
@ComponentScan({"org.ujar.lorem.shared.*", "org.ujar.lorem.processing.*"})
class ApplicationConfig {
}
