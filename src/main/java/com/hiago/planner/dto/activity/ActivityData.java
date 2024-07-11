package com.hiago.planner.dto.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityData(UUID activityId, String title, LocalDateTime occurs_at) {
}
