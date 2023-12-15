package dev.knowhowto.lorem.shared.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import dev.knowhowto.lorem.shared.config.errorhandling.Backoff;
import dev.knowhowto.lorem.shared.config.errorhandling.DeadLetter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lorem.kafka.error-handling")
public record KafkaErrorHandlingProperties(
    @NotNull @Valid DeadLetter deadLetter,
    @NotNull @Valid Backoff backoff) {

  public DeadLetter getDeadLetter() {
    return deadLetter();
  }

  public Backoff getBackoff() {
    return backoff();
  }
}

