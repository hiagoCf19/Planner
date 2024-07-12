package com.hiago.planner.dto.participant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantConfirmRequestPayload(@NotBlank @NotNull String name) {
}
