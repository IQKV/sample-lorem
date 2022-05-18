package org.ujar.loremipsum.shared.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.ujar.loremipsum.shared.config.logbook.LogbookConfiguration;
import org.ujar.loremipsum.shared.config.logbook.LogbookJsonBodyFilter;
import org.ujar.loremipsum.shared.config.logbook.LogbookResponseOnStatus;

@Configuration
@Import(value = {
    LogbookConfiguration.class,
    PrometheusConfiguration.class
})
@EnableConfigurationProperties({ProjectInfoProperties.class})
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
class ApplicationConfig {
}
