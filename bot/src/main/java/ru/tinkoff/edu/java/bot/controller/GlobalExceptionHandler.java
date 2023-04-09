package ru.tinkoff.edu.java.bot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.java.bot.dto.controller.ApiErrorResponse;

import java.util.Arrays;
import java.util.Objects;

@RestControllerAdvice(
        basePackages = "ru.tinkoff.edu.java.bot.controller"
)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final static String notImplementedResponse = "Error handler is not implemented yet";
    private final static String notImplementedCode = "400";

    @ExceptionHandler
    protected ResponseEntity<ApiErrorResponse> handleNullPointerException(Exception ex) {
        // TODO: implement
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        notImplementedResponse,
                        notImplementedCode,
                        ex.toString(),
                        ex.getMessage(),
                        Arrays.stream(ex.getStackTrace()).map(Objects::toString).toList()
                ),
                HttpStatus.I_AM_A_TEAPOT
        );
    }
}