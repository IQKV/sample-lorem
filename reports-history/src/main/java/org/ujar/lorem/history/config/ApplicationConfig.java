package org.ujar.lorem.history.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.ujar.lorem.shared.config.BuildInfoConfig;
import org.ujar.lorem.shared.config.KafkaErrorHandlingProperties;
import org.ujar.lorem.shared.config.KafkaHealthIndicatorConfig;
import org.ujar.lorem.shared.config.KafkaTopicDefinitionProperties;
import org.ujar.lorem.shared.config.SwaggerConfig;

@Configuration
@Import(value = {
    BuildInfoConfig.class,
    SwaggerConfig.class,
    KafkaHealthIndicatorConfig.class
})
@EnableJpaRepositories({"org.ujar.lorem.history.repository"})
@ComponentScan({"org.ujar.lorem.shared.*", "org.ujar.lorem.history.*"})
@EnableJpaAuditing
@EnableTransactionManagement
@EnableConfigurationProperties({
    KafkaErrorHandlingProperties.class,
    KafkaTopicDefinitionProperties.class
})
@EnableSpringDataWebSupport
class ApplicationConfig {

}
