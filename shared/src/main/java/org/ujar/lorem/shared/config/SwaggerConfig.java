package org.ujar.lorem.shared.config;

import static java.util.Objects.requireNonNullElse;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnMissingBean(OpenAPI.class)
@Import(value = {
    SwaggerConfig.OpenApiServerConfigurator.class,
})
@Slf4j
public class SwaggerConfig {

  @Configuration
  public static class OpenApiServerConfigurator {
    @Bean
    public OpenAPI openApi(
        @Value("${spring.application.name}") String applicationName,
        ApplicationBuildInfoProperties applicationBuildInfoProperties
    ) {
      final var api = new OpenAPI().info(
          new Info()
              .title(applicationName)
              .version(applicationBuildInfoProperties.getVersion())
              .description(applicationBuildInfoProperties.getDescription())
      );
      final var server = new Server();
      log.info("Project info properties: {}", applicationBuildInfoProperties);
      server.setUrl(requireNonNullElse(applicationBuildInfoProperties.getRelativePath(), ""));
      api.setServers(List.of(server));
      return api;
    }
  }
}
