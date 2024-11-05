package com.iqkv.incubator.sample.lorem.processing.config;

import com.iqkv.incubator.sample.lorem.shared.config.BuildInfoConfig;
import com.iqkv.incubator.sample.lorem.shared.config.KafkaErrorHandlingProperties;
import com.iqkv.incubator.sample.lorem.shared.config.KafkaHealthIndicatorConfig;
import com.iqkv.incubator.sample.lorem.shared.config.KafkaTopicDefinitionProperties;
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
@ComponentScan({"com.iqkv.incubator.sample.lorem.shared.*", "com.iqkv.incubator.sample.lorem.processing.*"})
@OpenAPIDefinition(info = @Info(title = "Words Counter", version = "24.0.0"))
class ApplicationConfig {
}
