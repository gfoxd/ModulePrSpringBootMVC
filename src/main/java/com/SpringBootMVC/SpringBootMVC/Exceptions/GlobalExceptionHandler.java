package com.SpringBootMVC.SpringBootMVC.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessageResponse> handleNoFoundException(NoSuchElementException e) {
        log.error("Got exception: " + e.getMessage());

        ErrorMessageResponse errorDto = new ErrorMessageResponse(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Got validation exception: " + e.getMessage());

        String detailedMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorMessageResponse errorDto = new ErrorMessageResponse(
                "Ошибка валидации запроса",
                detailedMessage,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleException(Exception e) {
        log.error("Server error: " + e.getMessage(), e);

        ErrorMessageResponse errorDto = new ErrorMessageResponse(
                "Внутренняя ошибка сервера",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
