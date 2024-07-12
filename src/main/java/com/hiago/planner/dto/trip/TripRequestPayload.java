package com.hiago.planner.dto.trip;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TripRequestPayload(
                @NotBlank
                String destination,
                @NotNull
                String starts_at,
                @NotNull
                String ends_at,
                List<String> emails_to_invite,
                @Email
                @NotNull
                String owner_email,
                @NotNull
                @NotBlank
                String owner_name
) {
}
