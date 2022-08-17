package org.ujar.loremipsum.shared.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProgramBuildInfoProperties.class)
public class ProjectInfoConfig {
}
