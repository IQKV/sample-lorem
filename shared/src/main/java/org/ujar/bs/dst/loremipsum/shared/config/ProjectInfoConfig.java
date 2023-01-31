package org.ujar.bs.dst.loremipsum.shared.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProgramBuildInfoProperties.class)
public class ProjectInfoConfig {
}
