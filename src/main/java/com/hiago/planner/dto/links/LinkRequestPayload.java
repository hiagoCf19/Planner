package com.hiago.planner.dto.links;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LinkRequestPayload(@NotBlank
                                 @NotNull
                                 String title,
                                 @NotBlank
                                 @NotNull
                                 String url) {
}
