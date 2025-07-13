package com.s4mz.eventbooking.controllers;

import com.s4mz.eventbooking.domain.dtos.ErrorDto;
import com.s4mz.eventbooking.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketNotFoundException(TicketNotFoundException exception) {
        log.error("Caught TicketNotFoundException: {}", exception.getMessage());
        ErrorDto errorDto = new ErrorDto("Ticket not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketSoldOutException.class)
    public ResponseEntity<ErrorDto> handleTicketSoldOutException(TicketSoldOutException exception){
        log.error("Caught TicketSoldOutException: {}",exception.getMessage());
        ErrorDto errorDto=new ErrorDto("Ticket sold out");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QrCodeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleQrCodeNotFoundException(QrCodeNotFoundException exception){
        log.error("Caught QrCodeNotFoundException: {}",exception.getMessage());
        ErrorDto errorDto=new ErrorDto("Qr code not found");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<ErrorDto> handleQrCodeGenerationException(QrCodeGenerationException exception){
        log.error("Caught QrCodeGenerationException: {}",exception.getMessage());
        ErrorDto errorDto=new ErrorDto("Unable to generate QR code");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EventUpdateException.class)
    public ResponseEntity<ErrorDto> handleEventUpdateException(EventUpdateException exception){
        log.error("Caught EventUpdateException: {}",exception.getMessage());
        ErrorDto errorDto=new ErrorDto("Unable to update event");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketTypeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketTypeNotFoundException(TicketTypeNotFoundException exception){
        log.error("Caught TicketTypeNotFoundException: {}",exception.getMessage());
        ErrorDto errorDto=new ErrorDto("Ticket type not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEventNotFoundException(EventNotFoundException exception){
        log.error("Caught EventNotFoundException: {}",exception.getMessage());
        ErrorDto errorDto=new ErrorDto("Event not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException exception){
        log.error("Caught UserNotFoundException: {}",exception.getMessage());
        ErrorDto errorDto=new ErrorDto("User not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ){
        log.error("Caught method argument not valid exception: {}",exception.getMessage());
        String errorMessage=exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError->
                        fieldError.getField()+":"+fieldError.getDefaultMessage()
                ).orElse("Method argument not valid exception occurred");
        ErrorDto errorDto=new ErrorDto(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException exception){
        log.error("Caught constraint violation: {}",exception.getMessage());
        String errorMessage=exception
                .getConstraintViolations()
                .stream().findFirst()
                .map(violation->
                        violation.getPropertyPath()+":"+violation.getMessage()
                ).orElse("Constraint violation occurred");
        ErrorDto errorDto=new ErrorDto(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        log.error("Caught exception: {}",ex.getMessage());
        ErrorDto errorDto=new ErrorDto("An unknown error has occurred");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
