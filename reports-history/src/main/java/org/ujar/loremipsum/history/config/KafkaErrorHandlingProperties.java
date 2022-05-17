package org.ujar.loremipsum.history.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConstructorBinding
@ConfigurationProperties(prefix = "loremipsum.kafka.error-handling")
@Validated
record KafkaErrorHandlingProperties(
    @NotNull @Valid DeadLetter deadLetter,
    @NotNull @Valid Backoff backoff) {
}

