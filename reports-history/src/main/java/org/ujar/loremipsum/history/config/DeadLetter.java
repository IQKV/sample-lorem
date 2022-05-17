package org.ujar.loremipsum.history.config;

import java.time.Duration;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

record DeadLetter(
    @NotNull Duration retention,
    @Nullable String suffix) {
}
