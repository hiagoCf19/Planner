package com.hiago.planner.exception;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(String message){
        super(message);
    }
}
