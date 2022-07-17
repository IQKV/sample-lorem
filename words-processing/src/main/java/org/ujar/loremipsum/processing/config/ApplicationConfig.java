package org.ujar.loremipsum.processing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.loremipsum.shared.config.KafkaErrorHandlingProperties;
import org.ujar.loremipsum.shared.config.KafkaTopicDefinitionProperties;
import org.ujar.loremipsum.shared.config.ProjectInfoConfig;
import org.ujar.loremipsum.shared.config.PrometheusConfig;
import org.ujar.loremipsum.shared.config.SwaggerConfig;
import org.ujar.loremipsum.shared.config.logbook.LogbookConfig;
import org.ujar.loremipsum.shared.config.logbook.LogbookJsonBodyFilter;
import org.ujar.loremipsum.shared.config.logbook.LogbookResponseOnStatus;

@Configuration
@Import(value = {
    LogbookConfig.class,
    PrometheusConfig.class,
    SwaggerConfig.class,
    ProjectInfoConfig.class
})
@EnableConfigurationProperties({NetClientProperties.class,
    KafkaErrorHandlingProperties.class,
    KafkaTopicDefinitionProperties.class})
@ComponentScan({"org.ujar.loremipsum.shared.*", "org.ujar.loremipsum.processing.*"})
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
class ApplicationConfig {
}
