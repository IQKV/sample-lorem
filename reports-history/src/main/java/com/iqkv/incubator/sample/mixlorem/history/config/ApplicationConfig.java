package com.iqkv.incubator.sample.mixlorem.history.config;

import com.iqkv.incubator.sample.mixlorem.shared.config.BuildInfoConfig;
import com.iqkv.incubator.sample.mixlorem.shared.config.KafkaErrorHandlingProperties;
import com.iqkv.incubator.sample.mixlorem.shared.config.KafkaHealthIndicatorConfig;
import com.iqkv.incubator.sample.mixlorem.shared.config.KafkaTopicDefinitionProperties;
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
@EnableJpaRepositories({"com.iqkv.incubator.sample.mixlorem.history.repository"})
@ComponentScan({"com.iqkv.incubator.sample.mixlorem.shared.*", "com.iqkv.incubator.sample.mixlorem.history.*"})
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
