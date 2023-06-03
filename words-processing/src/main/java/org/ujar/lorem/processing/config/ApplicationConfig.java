package org.ujar.lorem.processing.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.lorem.shared.config.BuildInfoConfig;
import org.ujar.lorem.shared.config.KafkaErrorHandlingProperties;
import org.ujar.lorem.shared.config.KafkaHealthIndicatorConfig;
import org.ujar.lorem.shared.config.KafkaTopicDefinitionProperties;

@Configuration
@Import(value = {
    BuildInfoConfig.class,
    KafkaHealthIndicatorConfig.class
})
@EnableConfigurationProperties(value = {
    NetClientProperties.class,
    KafkaErrorHandlingProperties.class,
    KafkaTopicDefinitionProperties.class
})
@ComponentScan({"org.ujar.lorem.shared.*", "org.ujar.lorem.processing.*"})
@OpenAPIDefinition(info = @Info(title = "Words Counter", version = "23.0.0"))
class ApplicationConfig {
}
