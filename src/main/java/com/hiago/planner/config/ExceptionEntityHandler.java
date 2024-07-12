package com.hiago.planner.config;

import com.hiago.planner.dto.ErrorBadRequestDTO;
import com.hiago.planner.dto.MessageErrorDTO;
import com.hiago.planner.exception.CompatibilityDateException;
import com.hiago.planner.exception.ParticipantNotFoundException;
import com.hiago.planner.exception.TripNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Stream;

@ControllerAdvice
public class ExceptionEntityHandler {
    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<MessageErrorDTO> handleTripNotFound(TripNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageErrorDTO(exception.getMessage()));
    }
    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<MessageErrorDTO> handleParticipantNotFound(ParticipantNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageErrorDTO(exception.getMessage()));
    }
    @ExceptionHandler(CompatibilityDateException.class)
    public ResponseEntity<MessageErrorDTO> handleDateIncompatibility(CompatibilityDateException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageErrorDTO(exception.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Stream<ErrorBadRequestDTO>> handleArgumentNotValid(MethodArgumentNotValidException exception){
        var errors= exception.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(ErrorBadRequestDTO::new));
    }

}
