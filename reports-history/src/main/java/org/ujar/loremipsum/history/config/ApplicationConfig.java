package org.ujar.loremipsum.history.config;

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
import org.ujar.loremipsum.shared.config.PrometheusConfig;
import org.ujar.loremipsum.shared.config.logbook.LogbookConfig;
import org.ujar.loremipsum.shared.config.logbook.LogbookJsonBodyFilter;
import org.ujar.loremipsum.shared.config.logbook.LogbookResponseOnStatus;

@Configuration
@Import(value = {
    LogbookConfig.class,
    PrometheusConfig.class
})
@EnableJpaRepositories({"org.ujar.loremipsum.history.repository"})
@ComponentScan({"org.ujar.loremipsum.shared.*", "org.ujar.loremipsum.history.*"})
@EnableJpaAuditing
@EnableTransactionManagement
@EnableConfigurationProperties({
    KafkaErrorHandlingProperties.class,
    KafkaTopicsProperties.class})
@LogbookResponseOnStatus
@LogbookJsonBodyFilter
@EnableSpringDataWebSupport
class ApplicationConfig {

  @Bean
  SpringLiquibase liquibase(@Autowired DataSource dataSource) {
    var liquibase = new SpringLiquibase();
    liquibase.setChangeLog("classpath:liquibase/master.xml");
    liquibase.setDataSource(dataSource);
    return liquibase;
  }
}
