package org.ujar.loremipsum.shared.config;

import static java.util.Objects.requireNonNullElse;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnMissingBean(OpenAPI.class)
@Import(value = {
    SwaggerConfig.K8sSwaggerConfiguration.class,
})
@Slf4j
public class SwaggerConfig {

  @Configuration
  public static class K8sSwaggerConfiguration {
    @Bean
    public OpenAPI openApi(
        @Value("${spring.application.name}") String applicationName,
        ProjectInfoProperties projectInfoProperties
    ) {
      final var api = new OpenAPI().info(
          new Info()
              .title(applicationName)
              .version(projectInfoProperties.getVersion())
              .description(projectInfoProperties.getDescription())
      );
      final var server = new Server();
      log.info("Project info properties: {}", projectInfoProperties);
      server.setUrl(requireNonNullElse(projectInfoProperties.getRelativePath(), ""));
      api.setServers(List.of(server));
      return api;
    }
  }
}
