package org.ujar.loremipsum.history.config;

import java.time.Duration;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

record Backoff(
    @NotNull Duration initialInterval,
    @NotNull Duration maxInterval,
    @Positive int maxRetries,
    @Positive double multiplier) {
}
