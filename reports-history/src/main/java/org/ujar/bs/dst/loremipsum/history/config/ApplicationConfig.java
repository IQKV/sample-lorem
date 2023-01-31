package org.ujar.bs.dst.loremipsum.history.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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
    ProjectInfoConfig.class,
    SwaggerConfig.class
})
@EnableJpaRepositories({"org.ujar.bs.dst.loremipsum.history.repository"})
@ComponentScan({"org.ujar.bs.dst.loremipsum.shared.*", "org.ujar.bs.dst.loremipsum.history.*"})
@EnableJpaAuditing
@EnableTransactionManagement
@EnableConfigurationProperties({
    KafkaErrorHandlingProperties.class,
    KafkaTopicDefinitionProperties.class
})
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
@EnableSpringDataWebSupport
class ApplicationConfig {

  @Bean
  SpringLiquibase liquibase(@Autowired DataSource dataSource) {
    final var liquibase = new SpringLiquibase();
    liquibase.setChangeLog("classpath:liquibase/master.xml");
    liquibase.setDataSource(dataSource);
    return liquibase;
  }
}
