package com.hiago.planner.config;

import com.hiago.planner.dto.MessageErrorDTO;
import com.hiago.planner.exception.ParticipantNotFoundException;
import com.hiago.planner.exception.TripNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

}
