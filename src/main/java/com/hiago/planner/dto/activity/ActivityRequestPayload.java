package com.hiago.planner.dto.activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActivityRequestPayload(
        @NotBlank
        @NotNull
        String title,
        @NotBlank
        @NotNull
        String occurs_at) {
}
