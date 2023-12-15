package dev.knowhowto.lorem.processing.config;

import dev.knowhowto.lorem.shared.config.BuildInfoConfig;
import dev.knowhowto.lorem.shared.config.KafkaErrorHandlingProperties;
import dev.knowhowto.lorem.shared.config.KafkaHealthIndicatorConfig;
import dev.knowhowto.lorem.shared.config.KafkaTopicDefinitionProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
@ComponentScan({"dev.knowhowto.lorem.shared.*", "dev.knowhowto.lorem.processing.*"})
@OpenAPIDefinition(info = @Info(title = "Words Counter", version = "23.0.0"))
class ApplicationConfig {
}
