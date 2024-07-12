package com.hiago.planner.dto.participant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantInvitePayload(@NotNull @NotBlank @Email String email) {
}
