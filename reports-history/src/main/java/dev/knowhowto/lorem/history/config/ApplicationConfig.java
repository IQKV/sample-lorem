package dev.knowhowto.lorem.history.config;

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
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import(value = {
    BuildInfoConfig.class,
    KafkaHealthIndicatorConfig.class
})
@EnableJpaRepositories({"dev.knowhowto.lorem.history.repository"})
@ComponentScan({"dev.knowhowto.lorem.shared.*", "dev.knowhowto.lorem.history.*"})
@EnableJpaAuditing
@EnableTransactionManagement
@EnableConfigurationProperties({
    KafkaErrorHandlingProperties.class,
    KafkaTopicDefinitionProperties.class
})
@EnableSpringDataWebSupport
@OpenAPIDefinition(info = @Info(title = "Reports", version = "24.0.0"))
class ApplicationConfig {

}
