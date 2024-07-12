package com.hiago.planner.dto;

import org.springframework.validation.FieldError;

public record ErrorBadRequestDTO(String field, String message) {
    public ErrorBadRequestDTO(FieldError error){
        this(error.getField(), error.getDefaultMessage());
    }
}

