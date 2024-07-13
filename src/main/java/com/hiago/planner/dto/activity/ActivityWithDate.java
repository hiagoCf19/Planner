package com.hiago.planner.dto.activity;

import java.time.LocalDate;
import java.util.List;

public record ActivityWithDate(LocalDate date, List<ActivityData> activities) {

}
