package org.ujar.bs.dst.loremipsum.processing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.bs.dst.loremipsum.shared.config.KafkaErrorHandlingProperties;
import org.ujar.bs.dst.loremipsum.shared.config.KafkaTopicDefinitionProperties;
import org.ujar.bs.dst.loremipsum.shared.config.ProjectInfoConfig;
import org.ujar.bs.dst.loremipsum.shared.config.PrometheusConfig;
import org.ujar.bs.dst.loremipsum.shared.config.SwaggerConfig;
import org.ujar.bs.dst.loremipsum.shared.config.logbook.LogbookConfig;
import org.ujar.bs.dst.loremipsum.shared.config.logbook.LogbookJsonBodyFilter;
import org.ujar.bs.dst.loremipsum.shared.config.logbook.LogbookResponseOnStatus;

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
@ComponentScan({"org.ujar.bs.dst.loremipsum.shared.*", "org.ujar.bs.dst.loremipsum.processing.*"})
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
class ApplicationConfig {
}
