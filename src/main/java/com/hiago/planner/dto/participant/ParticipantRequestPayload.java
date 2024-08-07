package com.hiago.planner.dto.participant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantRequestPayload(
        @NotBlank
        @NotNull
        String name,
        @NotBlank
        @Email
        String email) {

}
