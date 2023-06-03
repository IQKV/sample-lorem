package org.ujar.lorem.history.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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

@Configuration
@Import(value = {
    BuildInfoConfig.class,
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
@OpenAPIDefinition(info = @Info(title = "Reports", version = "23.0.0"))
class ApplicationConfig {

}
