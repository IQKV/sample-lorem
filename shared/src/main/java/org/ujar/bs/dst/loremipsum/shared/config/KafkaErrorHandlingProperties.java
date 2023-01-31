package org.ujar.bs.dst.loremipsum.shared.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;
import org.ujar.bs.dst.loremipsum.shared.config.errorhandling.Backoff;
import org.ujar.bs.dst.loremipsum.shared.config.errorhandling.DeadLetter;

@ConstructorBinding
@ConfigurationProperties(prefix = "loremipsum.kafka.error-handling")
@Validated
public record KafkaErrorHandlingProperties(
    @NotNull @Valid DeadLetter deadLetter,
    @NotNull @Valid Backoff backoff) {
}

