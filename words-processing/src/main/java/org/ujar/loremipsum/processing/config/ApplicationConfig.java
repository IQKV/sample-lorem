package org.ujar.loremipsum.processing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.loremipsum.processing.config.logbook.LogbookConfiguration;
import org.ujar.loremipsum.processing.config.logbook.LogbookJsonBodyFilter;
import org.ujar.loremipsum.processing.config.logbook.LogbookResponseOnStatus;

@Configuration
@Import(value = {
    LogbookConfiguration.class,
    PrometheusConfiguration.class
})
@EnableConfigurationProperties({ProjectInfoProperties.class, NetClientProperties.class, KafkaTopicsProperties.class})
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
class ApplicationConfig {
}
