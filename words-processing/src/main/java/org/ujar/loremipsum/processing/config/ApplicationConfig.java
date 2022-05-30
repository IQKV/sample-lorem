package org.ujar.loremipsum.processing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.loremipsum.shared.config.PrometheusConfig;
import org.ujar.loremipsum.shared.config.logbook.LogbookConfig;
import org.ujar.loremipsum.shared.config.logbook.LogbookJsonBodyFilter;
import org.ujar.loremipsum.shared.config.logbook.LogbookResponseOnStatus;

@Configuration
@Import(value = {
    LogbookConfig.class,
    PrometheusConfig.class
})
@EnableConfigurationProperties({NetClientProperties.class, KafkaTopicsProperties.class})
@ComponentScan({"org.ujar.loremipsum.shared.*", "org.ujar.loremipsum.processing.*"})
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
class ApplicationConfig {
}
